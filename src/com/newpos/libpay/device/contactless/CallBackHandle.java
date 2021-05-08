package com.newpos.libpay.device.contactless;

import android.content.Context;

import com.bcp.file_management.FilesManagement;
import com.newpos.bypay.EmvL2App;
import com.newpos.bypay.EmvL2AppItem;
import com.newpos.bypay.EmvL2PubKey;
import com.newpos.bypay.EmvL2UiRequestData;
import com.newpos.bypay.IEmvL2CallBack;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.card.CardManager;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;
import com.pos.device.SDKException;
import com.pos.device.beeper.Beeper;
import com.pos.device.config.DevConfig;
import com.pos.device.picc.EmvContactlessCard;

import java.util.Arrays;
import java.util.Calendar;

import static com.bcp.definesbcp.Definesbcp.CAKEY;
import static com.bcp.definesbcp.Definesbcp.REVOK;

public class CallBackHandle implements IEmvL2CallBack {

    protected TransUI tparam;
    private EmvL2App param;
    private CardManager detects;
    private Context mCtx;
    private int idMsdCase11 = -1;

    private static final String EXCEPTION = "Exception";

    public CallBackHandle(TransUI p, EmvL2App emvL2Param, CardManager cm){
        param = emvL2Param;
        detects = cm;
        tparam = p;
    }

    public void setmCtx(Context mCtx) {
        this.mCtx = mCtx;
    }

    //     * this call back method is used to set transaction parameters,
//     * which will be transferred to each callback method.if you don't
 //    * need to read these parameters,just returning with null.
 //    * Those parameters object should extend the calss EmvL2App.java,
 //    * which has registered some parameters that may be used in
 //    * whole transaction.You can add other fields,such as otherAmount,which
//     * is added in this demo,in the class extending EmvL2App.java.

    @Override
    public EmvL2App SetEmvL2AppParam() {
        return param;
    }

//   *  Allocating memory for each transaction kernel.Those size are depended
//     *  on how many memory are needed in each transaction kernel.Technically it
//     *  should not less than 1024.

    @Override
    public void FnMemoryGet(EmvL2App handle, int memoryType, long[] size) {
        switch(memoryType){
            case 0://L2_MT_TAG
                size[0]=11500;//for emv tag store
                break;
            case 1://L2_MT_UNKNOW_TAG
                size[0]=10240;//for unknow tag store.
                break;
            case 2://L2_MT_TOTAL
                size[0]=11500L+2500L+4500L;//for all the store memory.add cache.
                break;
            case 4:
                size[0]=1024;
                break;
            case 5: //L2_MT_DATA_OBJECT
                size[0]=10240;
                break;
            default:
                break;
        }
    }
// allocating memory for paypass and JCB transaction.

    @Override
    public void FnMemoryForTornGet(EmvL2App handle, int slot, long[] size) {
        switch (slot){
            case 2://paypass
                size[0]=13200;
                break;
            case 8://JCB
                size[0]=13200;
                break;
            default:
                break;
        }
    }

//* set the beep sound according to the parameter ,success.

    @Override
    public void FnBeep(EmvL2App handle, boolean success) {
        try {
            if (!success)
                Thread.sleep(1000);

            Beeper.getInstance().beep(1000, 500);
        } catch (SDKException | InterruptedException e) {
            Thread.currentThread().interrupt();
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), EXCEPTION + e.toString());
        }
    }

// switch led status according to ledColor.
//     * ledColor:	0:LED_RED,
//     *            1:LED_BLUE,
//     *            2:LED_GREEN,
//     *            3:LED_YELLOW
//     * ledStatus:
 //    *           0:off
//     *           1:on
//     * num
    @Override
    public void FnLedSwitch(EmvL2App handle, int num, int ledStatus, int ledColor) {

        Logger.debug("java led witch: num: "+num+" ledStatus: "+ledStatus+" ledColor "+ledColor);
        switch (num){
            case 0:
                LedManager.getInstance().red(ledStatus);
                break;
            case 1:
                LedManager.getInstance().blue(ledStatus);
                break;
            case 2:
                LedManager.getInstance().green(ledStatus);
                break;
            case 3:
                LedManager.getInstance().yellow(ledStatus);
                break;
            default:
                break;
        }
    }
// This method will display the process status of transaction without any pause.It'S invoked when
//     * transaction is processing.
    @Override
    public void FnDisplay(EmvL2App handle, byte clearScreen, byte lan, byte msgid) {
        Logger.debug("FnDisplay call ,param: "+"clearScreen: "+clearScreen+" lan "+lan+" msgid : "+msgid);
    }
//This method will display the process status of transaction with pause.It'S invoked when
//     * transaction is processing.
    @Override
    public void FnDisplayAdvance(EmvL2App handle, EmvL2UiRequestData uiRequestData, int asOutcome) {
        String statuMsg = PAYUtils.getStatus(uiRequestData.mMessage,uiRequestData.mLanguage)+"\n";
        Logger.debug("FnDisplayAdvance"+"asOutcome= "+asOutcome+" statuMsg= "+statuMsg);

        if(idMsdCase11==-1) {
            if (uiRequestData.mMessage == 32)
                idMsdCase11 = uiRequestData.mMessage;
            tparam.handlingBCP(statuMsg,"",false);
        }else {
            if (uiRequestData.mMessage!=32 || idMsdCase11 != 32)
                tparam.handlingBCP(statuMsg,"",false);
            idMsdCase11 = -1;
        }

        Logger.debug("AsOutCome: "+asOutcome+" uiRequestData.mHoldTime "+uiRequestData.mHoldTime);
        if (asOutcome!=0){
            try {
                Thread.sleep(1000);//uiRequestData.mHoldTime*100
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), EXCEPTION + e.toString());
            }
        }else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "error" + e);
                Thread.currentThread().interrupt();
            }

            if(idMsdCase11==32) {
                statuMsg = PAYUtils.getStatus(140,uiRequestData.mLanguage);
                tparam.handlingBCP(statuMsg,"",false);
            }
        }
    }
// get current terminal'S time.
    @Override
    public void FnTimeGet(EmvL2App handle, byte[] time) {

        long cutime= System.currentTimeMillis();
        Calendar mCalendar= Calendar.getInstance();
        mCalendar.setTimeInMillis(cutime);
        int mYear = mCalendar.get(Calendar.YEAR);
        int mMonth = mCalendar.get(Calendar.MONTH)+1;
        int mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        int mHour=mCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinuts=mCalendar.get(Calendar.MINUTE);
        int mSecond = mCalendar.get(Calendar.SECOND);
        int mWeek = mCalendar.get(Calendar.DAY_OF_WEEK);
        Logger.debug("year: "+mYear+" month: "+mMonth+" day: "+mDay+" hour: "+mHour+" minuts: "+mMinuts+"second: "+mSecond+"week: "+mWeek);
        byte[] y = ISOUtil.int2bcd(mYear,2);
        if (y!=null)
            time[0]= y[1];
        byte[] m = ISOUtil.int2bcd(mMonth,1);
        if (m !=null)
            time[1]=m[0];
        byte[] d = ISOUtil.int2bcd(mDay,1);
        if (d !=null)
            time[2]=d[0];
        byte[] h = ISOUtil.int2bcd(mHour,1);
        if (h !=null)
            time[3]=h[0];
        byte[] mi = ISOUtil.int2bcd(mMinuts,1);
        if (mi!=null)
            time[4]=mi[0];
        byte[] s = ISOUtil.int2bcd(mSecond,1);
        if (s!=null)
            time[5]=s[0];
        byte[] w = ISOUtil.int2bcd(mWeek,1);
        if (w !=null)
            time[6] = w[0];
        Logger.debug("get time "+ISOUtil.byte2hex(time));
    }
//get device SN.

    @Override
    public void FnSNGet(EmvL2App handle, String[] buff, int buffSize) {
        String sn = DevConfig.getSN();
        buff[0]=sn;
        Logger.debug(" sn in java : "+buff[0] + "length: "+buff[0].length()+" buff size: "+buffSize);
    }

//get terminal ID
    @Override
    public void FnTerminalIDGet(EmvL2App handle, byte[] buff, int buffSize) {
        byte[] terID ={0x12,0x23,0x45,0x56,0x78,0x19,0x34,0x01};
        Logger.debug("terminal id in java: "+ISOUtil.byte2hex(terID)+" length: "+terID.length+" buffSize: "+buffSize);
        System.arraycopy(terID,0,buff,0,terID.length);
    }

    @Override
    public void FnKeyPadActive(EmvL2App handle, boolean actived) {
        //if there are Key Pad inserted in terminal,this method will be invoked to
        //    * active Key Pay.
    }


//check current Key Pad if it'S ok.
    @Override
    public boolean FnKeyPadActiveCheck(EmvL2App handle) {
        return false;
    }

// Power on Icc device.
    @Override
    public boolean FnICCardPowerOn(EmvL2App handle) {
        return false;
    }
//reset NFC device.when transaction read NFC fail,it'll be invoked.
    @Override
    public void FnNFCReset(EmvL2App handle) {
        detects.piccReset();
        detects.setMode(-1);
    }
//waiting for removing card in NFC detecting zone.
    @Override
    public void FnNFCCardRemove(EmvL2App handle) {
        try {
            detects.getmEmvContactlessCard().deactive(new EmvContactlessCard.DeactiveCallback() {
                @Override
                public void onFinish(int i) {
                    Logger.debug("remove card "+i);
                }
            });
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), EXCEPTION + e.toString());
        }
    }
//release NFC resource so that next time it can be used.
    @Override
    public void FnCardPowerOff(EmvL2App handle) {
        detects.stopPICC();
        detects.setMode( -1);
    }
//Detecting card.After transaction begin, this method will be revoked.
    @Override
    public int FnCardDetect(EmvL2App handle) {
        Logger.debug("begin to detect card---------------");
        return detects.detectCards(handle);
    }

//execute apdu exchanging .just need the part of NFC exchange.
    @Override
    public int FnAPDUExchange(EmvL2App handle, byte[] cmd, int cmdLen, byte[] resp, int[] respLen, byte allowAbort) {
        int ret =0;
        Logger.debug("FnAPDUExchange : "+allowAbort);
        if (detects.getMode() == CardManager.TYPE_NFC){
            Logger.debug("java apducmd: " + " length: " + cmdLen + " cmd " + ISOUtil.byte2hex(cmd));
            try {
                byte[] res = detects.getmEmvContactlessCard().transmit(cmd);

                byte[] failTemp = {0x11, 0x11};
                if (res != null && res.length != 2 && Arrays.equals(res, failTemp)) {
                    ret = -1;
                    Logger.debug("apdu response fail");
                } else {
                    assert res != null;
                    System.arraycopy(res, 0, resp, 0, res.length);
                    respLen[0] = res.length;
                    Logger.debug("java apduresp: " + "length: " + respLen[0] + " rsep " + ISOUtil.byte2hex(resp));
                    ret = 1;
                }
            } catch (SDKException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), EXCEPTION + e.toString());
            }
        }else {
           //error
        }
        return ret;
    }


    @Override
    public int FnDisplayListSelect(EmvL2App handle, byte lan, byte titleId, String[] menus, int menuConut) {
        Logger.debug("FnDisplayListSelect");
        for (int i=0;i<menuConut;i++)
            Logger.debug(menus[i]);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <menuConut; i++) {
            Logger.debug("应用名称：" + menus[i]);
            if (i == 0) {
                sb.append((menus[i]));
                Logger.debug("APP["+i+"]name"+"="+menus[i]);
            }
            else {
                sb.append("," + (menus[i]));
                Logger.debug("APP["+i+"]name"+"="+menus[i]);
            }
        }
        return 0;
    }

//get current language number supported by terminal.
    @Override
    public int FnLanguageCountGet(EmvL2App handle) {
        Logger.debug("FnLanguageCountGet");
        return 0;
    }
//selecting an language if card support multilple language.used in iccard.
    @Override
    public int FnLanguageSelect(EmvL2App handle, byte[] languages, int languagesLen) {
        return 1;
    }

//app selecting.used in iccard.If it'S NFC card.return the app NO which is the highest
 //    * priority.defualt parameter is 0.
    @Override
    public int FnAppSelect(EmvL2App handle, int appCount, EmvL2AppItem[] apps, boolean cl) {
        Logger.debug("FnAppSelect "+"app'S number: "+appCount);


        int temp=-1;
        int priorityInt=0;
        if (appCount == 1)//one application and contactless will select first one.
            return 0;
        //cl select app according to the priority of app
        priorityInt=apps[0].getM_priority_indicator();
        temp=0;
        for (int i=1;i<appCount;i++) {
            if(priorityInt>apps[i].getM_priority_indicator())    //caso de certificacion COM01 Test 01 Scenario 01
            {
                priorityInt=apps[i].getM_priority_indicator();
                temp=i;
            }

        }

        Logger.debug("java select app : "+temp);
        return temp;
    }

//get online pin
    @Override
    public int FnOnlinePinGet(EmvL2App handle, byte language, int timeout, String pan, byte[] pinBlock) {
        Logger.debug("FnOnlinePinGet ");
        return 0;
    }


// Off line pin verify.
//     * Following is the meaning of returning parameter:
 //    * 0: ENTERED,
 //    * 1: ABORTED,
 //    * 2: BYPASS,
//     * 3: FAILED
    @Override
    public int FnOffLinePinVerify(EmvL2App handle, byte language, int timeout, EmvL2PubKey key, byte[] rand, byte[] sw) {
        return -1;
    }

//Find CA key keeping in terminal according rid,index,which are read from card.
//     * If it'S successful,set those CA parameter
//     * in the parameter,key and return ture.otherwise return false.
    @Override
    public boolean FnCAPublicKeyGet(EmvL2App handle, byte[] rid, byte index, EmvL2PubKey[] key) {
        //file'S format: rid,index,expLen,modLen,exponent,module  (multiple public keys information in this file)
        Logger.debug("rid: "+ISOUtil.byte2hex(rid)+" index1: "+ISOUtil.byte2hex(new byte[]{index})+" capk: "+handle.CapkFileName);
        byte[] su = FilesManagement.readFileBin(CAKEY, mCtx);
        if (su!=null){
            int offset=0;
            int totalLen = su.length;
            Logger.debug("capk: "+ISOUtil.byte2hex(su)+"\n length: "+su.length);
            while (offset<totalLen){
                Logger.debug("index "+offset);
                byte[] tempRID =new byte[5];
                System.arraycopy(su,offset,tempRID,0,5);
                offset +=5;
                Logger.debug("current rid: "+ISOUtil.byte2hex(tempRID));
                Logger.debug("(int) index: "+ISOUtil.byte2int(new byte[]{index})+
                        " real: "+ISOUtil.byte2int(new byte[]{su[offset]}));
                if (Arrays.equals(rid,tempRID)&&index == su[offset]){
                    offset +=1;//explen
                    int len = su[offset]&0xff;
                    Logger.debug("get offset "+ len);
                    key[0].setExponentLn(len);
                    Logger.debug(" set explen: offset "+ offset);
                    offset +=1;//modulen
                    len = su[offset]&0xff;
                    Logger.debug("get offset "+ len);
                    key[0].setModulusLn(len);
                    byte[] tempExp = new byte[key[0].getExponentLn()];
                    offset+=1;//exp
                    System.arraycopy(su,offset,tempExp,0,key[0].getExponentLn());
                    key[0].setExponent(tempExp);
                    Logger.debug("exponent: "+ISOUtil.byte2hex(key[0].getExponent()));
                    offset += key[0].getExponentLn();//modu
                    byte[]temMod = new byte[key[0].getModulusLn()];
                    System.arraycopy(su,offset,temMod,0,key[0].getModulusLn());
                    key[0].setModulus(temMod);
                    Logger.debug("module: "+ISOUtil.byte2hex(key[0].getModulus()));
                    return true;
                }else {
                    offset +=1;//explen
                    int expLen = su[offset]&0xff;
                    offset += 1;//modulen
                    int modLen = su[offset]&0xff;
                    offset += modLen;
                    offset += expLen;
                    offset += 1;//index for next item
                }
            }
        }else
            Logger.debug("cakey is null");
        return false;
    }

// check certification revoke according to rid , index and sn.
    @Override
    public boolean FnCertRevokeCheck(EmvL2App handle, byte[] rid, byte index, byte[] sn) {
        Logger.debug("revoke file check : \nrid: "+ISOUtil.byte2hex(rid)+" index2: "+index+" sn: "+ISOUtil.byte2hex(sn));
        byte[] revoks = FilesManagement.readFileBin(REVOK, mCtx);
        if (revoks !=null){
            int toLen = revoks.length;
            int curIndex = 0;
            while (curIndex <toLen){
                byte[] tempRID = new byte[5];
                System.arraycopy(revoks,curIndex,tempRID,0,5);
                curIndex += 5;
                byte[] tempSN = new byte[3];
                System.arraycopy(revoks,curIndex+1,tempSN,0,3);
                if (Arrays.equals(tempRID, rid)&&index == revoks[curIndex]&& Arrays.equals(tempSN, sn)){
                    Logger.debug("find revoke\n rid: "+ISOUtil.byte2hex(tempRID)+" index3: "+revoks[curIndex]+" sn: "+ ISOUtil.byte2hex(tempSN));
                    return true;
                }
                curIndex += 3+1;
            }
        }
        return false;
    }

// this is online transaction method,which will be invoked when the ask to connect issue
//     * server to check transaction.You should develop this method. Download the issuer responding
//     * data and set them into kernel.
    @Override
    public int Fn_Online_Auth(EmvL2App handle, byte[] arc, byte[] issuerResponse, int[] issuerResponseLen) {
        int retval =0;
        Logger.debug("Fn_Online_Auth online return 0");
        arc[0] = 0x30;
        arc[1] = 0x30;
        return retval;
    }


    @Override
    public void FnBatchData(EmvL2App handle) {
        // get batch data method.
    }

    @Override
    public void FnReversal(EmvL2App handle) {
        // reverasl method.
    }

    @Override
    public void FnAdvice(EmvL2App handle) {
        //advice method.
    }

    @Override
    public void FnGetTagListData(EmvL2App handle) {
        // get tag list data.
    }

    @Override
    public boolean Fn_find_PAN_From_EFL(EmvL2App handle, byte[] pan, byte panLen, byte[] psn) {
        return true;
    }

}
