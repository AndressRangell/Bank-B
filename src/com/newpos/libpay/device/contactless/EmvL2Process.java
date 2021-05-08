package com.newpos.libpay.device.contactless;

import android.content.Context;

import com.bcp.file_management.FilesManagement;
import com.newpos.bypay.EmvL2;
import com.newpos.bypay.IEmvInitLibCallBack;
import com.newpos.bypay.IEmvL2CallBack;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.card.CardManager;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;
import java.io.File;

import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.transaction;
import static com.bcp.definesbcp.Definesbcp.ENTRY_POINT;
import static com.bcp.definesbcp.Definesbcp.PROCESSING;
import static com.bcp.definesbcp.Definesbcp.TERMINAL;
import static com.newpos.libpay.trans.finace.FinanceTrans.DOLAR;
import static com.newpos.libpay.trans.finace.FinanceTrans.EURO;
import static com.newpos.libpay.trans.finace.FinanceTrans.LOCAL;
import static com.newpos.libpay.utils.ISOUtil.padleft;


public class EmvL2Process {
    public EmvL2Process(Context context, TransInputPara p){
        mContext =context;
        para = p;
        tParam = p.getTransUI();
    }
    protected TransUI tParam;
    private Context mContext;
    private EmvL2 emvL2 =null;
    private CallBackHandle emvl2CallBack;
    private ParamEmvL2 param;
    private long cl2Amount;
    private long cl2OtherAmount;
    protected boolean clMode;
    protected byte[] iccData; // 55*
    protected byte[]varAid;
    protected byte[]track1;
    protected String track2;
    protected String panSeqNo;
    protected String cardno;
    protected byte[]expDate;
    protected int cvmType;
    protected String lable="";
    protected String varTvr;
    protected String varTsi;
    protected String varArqc;
    protected String random ;//随机数
    protected String traceNo;
    protected String typeCoin;
    protected String holderName;
    protected String typeTrans;

    private TransInputPara para;
    private byte[] payGenericTag55 = {
            (byte) 0x9F, (byte)0x26, // AC (Application Cryptogram)
            (byte) 0x9F, (byte)0x27, // CID
            (byte) 0x9F, (byte)0x10, // IAD (Issuer Application Data)
            (byte) 0x9F, (byte)0x37, // Unpredicatable Number
            (byte) 0x9F, (byte)0x36, // ATC (Application Transaction Counter)
            (byte) 0x95, // TVR
            (byte) 0x9A, // Transaction date
            (byte) 0x9C, // Transaction Type
            (byte) 0x9F, (byte)0x02, // Amount Authorised
            (byte) 0x5F,(byte)0x2A, // Transaction Currency Code
            (byte) 0x82, // AIP
            (byte) 0x9F,(byte)0x1A, // Terminal Country Code
            (byte) 0x9F,(byte)0x03, // Amount Other
            (byte) 0x9F,(byte)0x33, // Terminal Capabilities
            // opt
            (byte) 0x9F,(byte)0x34, // CVM Result
            (byte) 0x9F,(byte)0x35, // Terminal Type
            (byte) 0x9F,(byte)0x1E, // IFD Serial Number
            (byte) 0x84, // Dedicated File Name
            (byte) 0x9F,(byte)0x09, // Application Version #
            //(byte) 0x9F,(byte)0x41, // Transaction Sequence Counter
            //0x4F,
            (byte) 0x5F,(byte)0x34, // PAN Sequence Number
            //0x50//Application label
            (byte) 0x9F,(byte)0x06,
            (byte) 0x9F,(byte)0x07,
            (byte) 0x9F,(byte)0x53,
            (byte) 0x9F,(byte)0x71,
            (byte) 0x9F,(byte)0x6E
    };
    public int emvl2ParamInit(){
        String path;
        String[] modules;
        CardManager detects;
        setDataEmpty();
        ParamEmvL2 paramEmvL2 = new ParamEmvL2();
        Logger.debug("begin to initialize emv l2");
        path=mContext.getFilesDir().getPath()+ File.pathSeparator;
        modules=new String[]{path+"libPayPass.so",path+"libPayWave.so",path+"libXPressPay.so",path+"libDPAS.so"};
        //replace the above string using the following string after loading kernel in the system path.
        detects = CardManager.getInstance(0x10);
        param = paramEmvL2;//transaction parameters.
        emvL2 = new EmvL2();
        EmvL2.EMVL2Init(new IEmvInitLibCallBack() {
            @Override
            public void InitCallback(boolean isAllSu) {
                if (isAllSu){
                    Logger.debug("init bypay sdk successfully");
                }else
                    Logger.debug("init bypay fail");
            }
        });
        File tempFile = new File(mContext.getFilesDir().getPath()+"/libPayPass.so");
        if (!tempFile.exists()){
            PAYUtils.copyAssetsToData(mContext,"libPayPass.so");
            PAYUtils.copyAssetsToData(mContext,"libPayWave.so");
            PAYUtils.copyAssetsToData(mContext,"libXPressPay.so");
            PAYUtils.copyAssetsToData(mContext,"libDPAS.so");
        }
        emvl2CallBack = new CallBackHandle(tParam,param,detects);
        emvl2CallBack.setmCtx(mContext);
        emvL2.EmvL2CallBackSet(emvl2CallBack);//set callback .just need initialize one time.
        emvL2.LoadKernels(modules);//make sure this method called after callback set.very important.just need initialize one time.
        emvL2.EmvL2transCounterSet(Long.parseLong(traceNo));//Read transaction counter from your parameter and set  JM
        emvL2.EmvL2OptionsSet(param.l2Options);//set transaction'S options list
        emvL2.EmvL2AidClear();//clear aid
        emvL2.EmvL2ClParamClear();//clear cl param.

        //load param
        byte[] su = FilesManagement.readFileBin(TERMINAL, mContext);
        if (su!=null){
            Logger.debug("terminfs: "+ ISOUtil.byte2hex(su));
            emvL2.EmvL2TerminfSet(su, su.length);//load terminal configuration
        }else{
            return 1;
        }
        byte[] aid = FilesManagement.readFileBin(PROCESSING, mContext);
        if (aid!=null){
            Logger.debug("aids' len: "+aid.length+"\naids: "+ISOUtil.byte2hex(aid));
            int index =0;
            int aidsTalLen = aid.length;
            index +=(20+1+2);//1hash mark + 20hash value
            int count = 1;
            while (index<aidsTalLen){
                if((aid[index]&0xff) == 0x81) {
                    index += 1;//81 for more than 128
                    Logger.logLine(Logger.LOG_GENERAL, "get 81 tag , increase index ,curent: "+index);
                }
                int aidLen = aid[index]&0xff;
                index +=1;//one byte for aid'S len
                byte[] tempAid = new byte[aidLen+1];
                System.arraycopy(aid,index,tempAid,0,aidLen);
                Logger.debug("aid : "+ISOUtil.byte2hex(tempAid)+"\nlength: "+tempAid.length+"actual len : "+aidLen);
                int ret = emvL2.EmvL2AidAdd(tempAid,aidLen,count);//load aids
                if (ret !=0)
                    Logger.debug("load aid "+ISOUtil.byte2hex(tempAid)+"fail");
                index+=aidLen;
                index +=2;// 2 byte for aid mark
                count++;
            }
        }else{
            return 2;
        }
        byte[] entrypoint = FilesManagement.readFileBin(ENTRY_POINT, mContext);
        if (entrypoint!=null){
            Logger.debug("length: "+entrypoint.length+"\nentrypoint : "+ISOUtil.byte2hex(entrypoint));
            int index = 20+1+2;//1hash mark + 20hash value
            int epTalLen = entrypoint.length;
            while (index<epTalLen){
                if((entrypoint[index]&0xff) == 0x81) {
                    index += 1;//81 for more than 128
                    Logger.debug("get 81 tag , increase index ,curent: "+index);
                }
                int signalLen = entrypoint[index]&0xff;
                Logger.debug("cl signal len: "+signalLen);
                index +=1; // len value
                byte[] tempEP = new byte[signalLen+1];
                System.arraycopy(entrypoint,index,tempEP,0,signalLen);
                Logger.debug("length: "+signalLen+"\nEPsignal : "+ISOUtil.byte2hex(tempEP));
                int ret = emvL2.EmvL2ClParamAdd((byte) param.tranType, tempEP, signalLen);//load entry point parameters
                if (ret != 0){
                    Logger.debug("load cl param "+ISOUtil.byte2hex(tempEP)+" fail");
                }
                index += signalLen;
                index +=2;// 2 byte for aid mark
                Logger.debug("index: "+index);
            }
        } else{
            return 3;
        }
        Logger.debug("load param done");
        return 0;
    }
    public void setAmount(long amount,long otheramount){
        cl2Amount=amount;
        cl2OtherAmount=otheramount;
    }
    public int start(){
        param.amount = cl2Amount;

        /**
         * Aerolineas Diners
         * Evita que el kernel solicite monto ya que para aerolinea inicialmente ira en cero
         */
        if (Long.parseLong(polarisUtil.getTransCurrent().getAmountMinimun())==0) {
            para.setNeedAmount(false);
        }

        if (para.isNeedAmount() && param.amount <= 0) {
            emvL2.EmvL2TransactionClose();
            return -1;
        }
        if(param.tranType == EmvL2.L2_TT_WITH_CASHBACK ){
            param.setOtherAmount(cl2OtherAmount);
            if (param.getOtherAmount()<=0) {
                emvL2.EmvL2TransactionClose();
                return -1;
            }
        }


        transactionSetData(param);
        int cs;
        int tranResult = -1;

        param.DetectContactLess = param.l2Options.Contactless;
        param.DetectContact = param.l2Options.Contact;
        param.DetectMagStripe = param.l2Options.Magstripe;

        if (!param.DetectContactLess){//Contact
            Logger.debug("use icc card");
            cs = emvl2CallBack.FnCardDetect(param);
            switch (cs){
                case EmvL2.L2_CS_SWIPED:
                    tranResult =IEmvL2CallBack.L2_FAILED;
                    break;
                case EmvL2.L2_CS_INSERTED:
                    tranResult = emvL2.EmvL2TransactionExecute(1);
                    if (tranResult == IEmvL2CallBack.L2_ALTER_OTHER_INTERFACE){
                        param.DetectContact = false;
                        if (!param.DetectMagStripe)
                            tranResult = IEmvL2CallBack.L2_END_APPLICATION;
                        else {
                            tranResult = validateRetunr(emvl2CallBack.FnCardDetect(param), true);
                        }
                    }
                    break;
                case EmvL2.L2_CS_CANCELED:
                    break;
                case EmvL2.L2_CS_TIMEOUT:
                    break;
                default:
                    tranResult = IEmvL2CallBack.L2_TRANSACTION_CANCELLED;
                    break;
            }
        }else {

            tranResult = emvL2.EmvL2TransactionExecute(0);

            Logger.debug("emvL2.EmvL2TransactionExecute get return "+tranResult);
            if (tranResult == IEmvL2CallBack.L2_NOT_EMV_CARD_POOLED){
                tranResult = IEmvL2CallBack.L2_FAILED;
                Logger.debug("L2_NOT_EMV_CARD_POOLED ");
            }else if(tranResult == IEmvL2CallBack.L2_ALTER_OTHER_INTERFACE||
                    emvL2.EmvL2NoContactlessSupported()) {
                param.DetectContactLess = false;
                if (!param.DetectContact && !param.DetectMagStripe)
                    tranResult = IEmvL2CallBack.L2_END_APPLICATION;
                else {
                    tranResult = validateRetunr(emvl2CallBack.FnCardDetect(param), false);
                }
            }
        }
        Logger.debug("transaction close with "+tranResult);
        if(tranResult==IEmvL2CallBack.L2_APPROVED){

            Logger.debug("EmvL2GetMode="+ISOUtil.bcd2int(emvL2.EmvL2GetMode()));
            clMode = ISOUtil.bcd2int(emvL2.EmvL2GetMode())==91;
            if(setTransData()!=IEmvL2CallBack.L2_NONE)
                tranResult=IEmvL2CallBack.L2_FAILED;
        }
        emvL2.EmvL2TransactionClose();
        if(tranResult==IEmvL2CallBack.L2_APPROVED)
            return  0;
        else if (tranResult==IEmvL2CallBack.L2_DECLINED || tranResult==IEmvL2CallBack.L2_END_APPLICATION){
            return 7;
        } else if (tranResult==IEmvL2CallBack.L2_TRY_ANOTHER_INTERFACE) {
            return 8;
        }
        else
            return -1;
    }
    private int validateRetunr(int cs, boolean val) {
        int tranResult;
        switch (cs){
            case EmvL2.L2_CS_SWIPED:
                tranResult = IEmvL2CallBack.L2_FAILED;
                break;
            case EmvL2.L2_CS_INSERTED:
                tranResult = val ? emvL2.EmvL2TransactionExecute(1) : IEmvL2CallBack.L2_FAILED;
                break;
            case EmvL2.L2_CS_CANCELED:
            case EmvL2.L2_CS_TIMEOUT:
                tranResult = cs;
                break;
            default:
                tranResult = IEmvL2CallBack.L2_TRANSACTION_CANCELLED;
                break;
        }
        return tranResult;
    }
    public boolean getClMode(){
        return clMode;
    }
    public byte [] getEmvOnlineData(){
        return iccData;
    }
    public String getCardNo(){
        return cardno;
    }
    public String getPanSeqNo(){
        return panSeqNo;
    }
    public String getTrack2data(){
        return track2;
    }
    public String getAid(){return ISOUtil.hexString(varAid);}
    public String getLable(){return lable;}
    public String getTVR(){return varTvr;}
    public String getTSI(){return varTsi;}
    public String getARQC(){return varArqc;}
    public String getRandom(){return random;}
    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public void setTypeCoin(String typeCoin) {
        this.typeCoin = typeCoin;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setTypeTrans(String typeTrans) {
        this.typeTrans = typeTrans;
    }

    public String deleteCharString0(String sourceString, char chElemData) {
        StringBuilder deleteString = new StringBuilder();
        for (int i = 0; i < sourceString.length(); i++) {
            if (sourceString.charAt(i) != chElemData) {
                deleteString.append(sourceString.charAt(i));
            }
        }
        return deleteString.toString();
    }
    public int getCVMType(){
        return cvmType;
    }
    private int setTransData(){
        byte []tPanSN;
        expDate = emvL2.EmvL2DataGetByTag(0x5F24);
        if(expDate!=null)
            Logger.debug("expDate="+ISOUtil.hexString(expDate));
        // 1磁道
        track1 = emvL2.EmvL2DataGetByTag(0x9F1F);
        if(track1!=null)
            Logger.debug("track1="+ISOUtil.hexString(track1));
        // 卡序号
        tPanSN = emvL2.EmvL2DataGetByTag(0x5F34);
        if(tPanSN!=null) {
            Logger.debug("tPanSN="+ISOUtil.hexString(tPanSN));
            panSeqNo = tPanSN.length == 1 ? padleft(ISOUtil.bcd2int(tPanSN, 0, tPanSN.length) + "", 3, '0') : null;
        }
        varAid=emvL2.EmvL2DataGetByTag(0x4F);
        if(varAid==null)
            varAid=emvL2.EmvL2DataGetByTag(0x9F06);
        if(varAid != null) {
            Logger.debug("AID="+ISOUtil.hexString(varAid));
            iccData = emvL2.EmvL2DataGetByTagList(payGenericTag55, payGenericTag55.length);
            Logger.debug("iccdata="+ISOUtil.hexString(iccData));
        }
        if (llenarTrack2() != 0){
            return IEmvL2CallBack.L2_FAILED;
        }
        cvmType=emvL2.EmvL2GetCvm().getCvmValue();
        Logger.debug("CVM type="+cvmType);
//for print
        byte[] temp;
        //随机数
        temp = emvL2.EmvL2DataGetByTag(0x9F37);
        if(temp!=null){
            random = ISOUtil.byte2hex(temp);
            Logger.debug("get Random = " + random);
        }
        // 应用标签
        temp = emvL2.EmvL2DataGetByTag(0x50);
        if(temp!=null) {
            lable = ISOUtil.hex2AsciiStr(ISOUtil.byte2hex(temp));
            Logger.debug("get Lable = " + lable);
        }

        temp = emvL2.EmvL2DataGetByTag(0x9F26);
        if(temp!=null) {
            varArqc = ISOUtil.byte2hex(temp);
            Logger.debug("get ARQC = " + varArqc);
        }

        temp = emvL2.EmvL2DataGetByTag(0x9B);
        if(temp!=null) {
            varTsi = ISOUtil.byte2hex(temp);
            Logger.debug("get TSI = " + varTsi);
        }

        temp = emvL2.EmvL2DataGetByTag(0x95);
        if(temp!=null) {
            varTvr = ISOUtil.byte2hex(temp);
            Logger.debug("get TVR = " + varTvr);
        }

        temp = emvL2.EmvL2DataGetByTag(0x5F20);
        holderName = temp != null ? ISOUtil.byte2hex(temp) : "---";
        Logger.debug("get holderName = " + holderName);

        return IEmvL2CallBack.L2_NONE;
    }

    private int llenarTrack2(){
        byte []tTrack2;
        tTrack2 = emvL2.EmvL2DataGetByTag(0x57);
        if(tTrack2==null) {
            tTrack2 = emvL2.EmvL2DataGetByTag(0x9F6B);
        }
        if(tTrack2!=null) {
            if(ISOUtil.hexString(tTrack2).endsWith("F")||ISOUtil.hexString(tTrack2).endsWith("f")) {
                if(ISOUtil.hexString(tTrack2).endsWith("F"))
                    track2=deleteCharString0(ISOUtil.hexString(tTrack2),'F') ;
                else if(ISOUtil.hexString(tTrack2).endsWith("f"))
                    track2=deleteCharString0(ISOUtil.hexString(tTrack2),'f') ;
            } else{
                track2=ISOUtil.hexString(tTrack2);
            }
            if (track2.length() > 7) {
                if(track2.length()>37){
                    track2=track2.substring(0, 37);
                }
                cardno = track2.split("D")[0];
                return IEmvL2CallBack.L2_NONE;
            }
        }
        return IEmvL2CallBack.L2_FAILED;
    }

    private void setDataEmpty(){
        clMode=false;   //false means EMV mode
        iccData=null;
        varAid=null;
        panSeqNo=null;
        cardno=null;
        track1=null;
        track2=null;
        expDate=null;
        cl2Amount=0;
        cl2OtherAmount=0;
        cvmType=0;
    }
    private void transactionSetData(ParamEmvL2 param){
        byte[] transTypes = {
                0x00,
                0x01,
                0x09,
                0x20,
                0x21,
                0x31,
                0x50,
                0x60,
                0x70,
                0x78,
                0x79,
                (byte) 0x90
        };
        byte[] transData=new byte[32];
        int transDataLen=0;
        transData[transDataLen++] = (byte) 0x9C;//transaction type
        transData[transDataLen++] = 0x01;
        transData[transDataLen++] = transTypes[param.tranType];

        transData[transDataLen++] = (byte) 0x9F;//transaction amount
        transData[transDataLen++] = 0x02;
        transData[transDataLen++] = 0x06;

        byte[] amount = ISOUtil.str2bcd(String.valueOf(param.amount),true);
        System.arraycopy(amount,0,transData,transDataLen+6-amount.length,amount.length);
        transDataLen += 6;

        transData[transDataLen++] = (byte) 0x9F;//transaction other amount
        transData[transDataLen++] = 0x03;
        transData[transDataLen++] = 0x06;
        byte[] otheramount = ISOUtil.str2bcd(String.valueOf(param.getOtherAmount()),false);
        System.arraycopy(otheramount,0,transData,transDataLen,otheramount.length);
        transDataLen += 6;
        Logger.debug("set transaction data: "+ISOUtil.byte2hex(transData)+"\n length: "+transDataLen);

        transData[transDataLen++] = (byte) 0x5F;//transaction currency code
        transData[transDataLen++] = 0x2A;
        transData[transDataLen++] = 0x02;
        byte[] tranCode;
        switch (typeCoin){//JM
            case LOCAL:
            case DOLAR:
                tranCode = new byte[]{0x08, 0x40};
                break;
            case EURO:
                tranCode = new byte[]{0x09, 0x78};
                break;
            default:
                tranCode = new byte[]{0x08, 0x40};
                break;
        }
        System.arraycopy(tranCode,0,transData,transDataLen,2);
        transDataLen += 2;

        emvL2.EmvL2TransDataSet(transData,transDataLen);

    }
}

