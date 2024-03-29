package com.newpos.libpay.trans.translog;

import java.io.Serializable;

/**
 * 交易日志详情信息类
 *
 * @author zhouqiang
 */
public class TransLogData implements Serializable {

    private String nameTrade;
    private String phoneTrade;
    private String aid;
    private String aidname;
    private String addressTrade;
    private int numCuotas;
    private String datePrint;
    private String numCell;
    private String msgID;
    private boolean isTip;

    public boolean isTip() {
        return isTip;
    }

    public void setTip(boolean tip) {
        isTip = tip;
    }

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }

    public String getNumCell() {
        return numCell;
    }

    public void setNumCell(String numCell) {
        this.numCell = numCell;
    }

    public String getDatePrint() {
        return datePrint;
    }

    public void setDatePrint(String datePrint) {
        this.datePrint = datePrint;
    }

    public int getNumCuotas() {
        return numCuotas;
    }

    public void setNumCuotas(int numCuotas) {
        this.numCuotas = numCuotas;
    }

    public String getAddressTrade() {
        return addressTrade;
    }

    public void setAddressTrade(String addressTrade) {
        this.addressTrade = addressTrade;
    }


    public String getNameTrade() {
        return nameTrade;
    }

    public void setNameTrade(String nameTrade) {
        this.nameTrade = nameTrade;
    }

    public String getPhoneTrade() {
        return phoneTrade;
    }

    public void setPhoneTrade(String phoneTrade) {
        this.phoneTrade = phoneTrade;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAidname() {
        return aidname;
    }

    public void setAidname(String aidname) {
        this.aidname = aidname;
    }

    private String labelCard;

    public String getLabelCard() {
        return labelCard;
    }

    public void setLabelCard(String labelCard) {
        this.labelCard = labelCard;
    }

    public String getNameCard() {
        return nameCard;
    }

    public void setNameCard(String nameCard) {
        this.nameCard = nameCard;
    }

    private String nameCard;

    /**
     * 应用密文 此次交易是联机还是脱机
     */


    private String typeCoin;

    public String getTypeCoin() {
        return typeCoin;
    }

    public void setTypeCoin(String typeCoin) {
        this.typeCoin = typeCoin;
    }


    private int aac;

    public int getAac() {
        return aac;
    }

    public void setAac(int aac) {
        this.aac = aac;
    }

    /**
     * 标记此次用开方式是否是非接方式
     */
    private boolean isNFC;

    public boolean isNFC() {
        return isNFC;
    }

    public void setNFC(boolean isNFC) {
        this.isNFC = isNFC;
    }

    /**
     * 标记此次交易用卡方式是否是插卡
     */
    private boolean isICC;

    public boolean isICC() {
        return isICC;
    }

    public void setICC(boolean isICC) {
        this.isICC = isICC;
    }

    /**
     * 标记此次交易是否是扫码方式
     */
    private boolean isScan;

    public boolean isScan() {
        return isScan;
    }

    public void setScan(boolean scan) {
        isScan = scan;
    }

    private int recState;

    /**
     * 初始状态为0，已上送成功1，已上送但是失败2
     **/

    public long getTipAmout() {
        return tipAmout;
    }

    public void setTipAmout(long tipAmout) {
        this.tipAmout = tipAmout;
    }

    /**
     * 小费
     **/
    private long tipAmout = 0;
    /**
     * 原交易流水号
     **/
    private String batchNo;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    /**
     * 预授权交易是否已经完成，完成的交易不能再次完成
     */
    private boolean isPreComp;

    public boolean isPreComp() {
        return isPreComp;
    }

    public void setPreComp(boolean preComp) {
        isPreComp = preComp;
    }

    public int getRecState() {
        return recState;
    }

    public void setRecState(int recState) {
        this.recState = recState;
    }

    /**
     * 交易英文名称
     * 详见 @{@link com.newpos.libpay.trans.Trans}
     */
    private String transEName;

    public String getEName() {
        return transEName;
    }

    public void setEName(String eName) {
        transEName = eName;
    }


    /**
     * 第二域卡号，2 加了*号的字串
     */
    private String pan;

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }


    /**
     * 第三域，2 预处理码
     */
    private String panNormal;

    public String getPanNormal() {
        return panNormal;
    }

    public void setPanNormal(String panNormal) {
        this.panNormal = panNormal;
    }


    /**
     * 第三域，3 预处理码
     */
    private String procCode;

    public String getProcCode() {
        return procCode;
    }

    public void setProcCode(String procCode) {
        this.procCode = procCode;
    }

    /**
     * 第四域，4 标记此次交易的金额
     */
    private Long amount;

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 第11域 , 交易流水号
     */
    private String traceNo;

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    /**
     * 第12域，交易时间
     */
    private String localTime;

    public String getLocalTime() {
        return localTime;
    }

    public void setLocalTime(String localTime) {
        this.localTime = localTime;
    }

    /**
     * 第13域，交易日期
     */
    private String localDate;

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    /**
     * 第14域，卡片有效期
     */
    private String expDate;

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    /**
     * 第15域。交易日期
     */
    private String settleDate;

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    /**
     * 第22域，输入方式
     */
    private String entryMode;

    public String getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(String entryMode) {
        this.entryMode = entryMode;
    }

    /**
     * 第23域，卡序号
     */
    private String panSeqNo;

    public String getPanSeqNo() {
        return panSeqNo;
    }

    public void setPanSeqNo(String panSeqNo) {
        this.panSeqNo = panSeqNo;
    }

    /**
     * 第三域，24 预处理码
     */
    private String nii;

    public String getNii() {
        return nii;
    }

    public void setNii(String nii) {
        this.nii = nii;
    }

    /**
     * 第25域 ， 服务点条件吗
     */
    private String svrCode;

    public String getSvrCode() {
        return svrCode;
    }

    public void setSvrCode(String svrCode) {
        this.svrCode = svrCode;
    }

    /**
     * 第32域
     */
    private String acquirerID;

    public String getAcquirerID() {
        return acquirerID;
    }

    public void setAcquirerID(String acquirerID) {
        this.acquirerID = acquirerID;
    }

    /**
     * 第35域
     */
    private String track2;

    public String getTrack2() {
        return track2;
    }

    public void setTrack2(String track2) {
        this.track2 = track2;
    }


    /**
     * 第37域
     */
    private String rrn;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rRN) {
        rrn = rRN;
    }

    /**
     * 第38域，认证码
     */
    private String authCode;

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    /**
     * 第39域，响应码
     */
    private String rspCode;

    public String getRspCode() {
        return rspCode;
    }

    public void setRspCode(String rspCode) {
        this.rspCode = rspCode;
    }


    /**
     * 第41域，响应码
     */
    private String termID;

    public String getTermID() {
        return termID;
    }

    public void setTermID(String termID) {
        this.termID = termID;
    }

    /**
     * 第42域，响应码
     */
    private String merchID;

    public String getMerchID() {
        return merchID;
    }

    public void setMerchID(String merchID) {
        this.merchID = merchID;
    }

    /**
     * 第44域，发卡行和收单行
     */
    private String field44;

    public String getField44() {
        return field44;
    }

    public void setField44(String field44) {
        this.field44 = field44;
    }


    /**
     * 第49域，交易货币代码
     */
    private String currencyCode;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * 操作员号码
     */
    private int oprNo;

    public int getOprNo() {
        return oprNo;
    }

    public void setOprNo(int oprNo) {
        this.oprNo = oprNo;
    }

    /**
     * 备注 52 从第三位开始到最后
     */
    private String pin;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * 如果是卡交易，55 存储此次交易的卡片55域信息
     */
    private byte[] iccdata;

    public byte[] getIccdata() {
        return iccdata;
    }

    public void setIccdata(byte[] iCCData) {
        iccdata = iCCData;
    }

    /**
     * 交易60域
     */
    private String field60;

    public String getField60() {
        return field60;
    }

    public void setField60(String field60) {
        this.field60 = field60;
    }

    /**
     * 交易62域
     */
    private String field62;

    public String getField62() {
        return field62;
    }

    public void setField62(String field62) {
        this.field62 = field62;
    }

    /**
     * 备注 63 从第三位开始到最后
     */
    private String field63;

    public String getField63() {
        return field63;
    }

    public void setField63(String field63) {
        this.field63 = field63;
    }

    /**
     * 备注 63 从第三位开始到最后
     */
    private String refence;

    public String getRefence() {
        return refence;
    }

    public void setRefence(String refence) {
        this.refence = refence;
    }

    /**
     * 发卡组织 63域 前三位
     */
    private String issuerName;

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getTypeAccount() {
        return typeAccount;
    }

    public void setTypeAccount(String typeAccount) {
        this.typeAccount = typeAccount;
    }

    private String typeAccount;

    /**
     *
     */
    private boolean fallback;

    public boolean isFallback() {
        return fallback;
    }

    public void setFallback(boolean fallback) {
        this.fallback = fallback;
    }

    private String adquirerName;

    public String getAdquirerName() {
        return adquirerName;
    }

    public void setAdquirerName(String adquirerName) {
        this.adquirerName = adquirerName;
    }


    /**
     * bcp
     */
    private long ammountXX;
    private long ammount0;
    private long ammountIVA;
    private long ammountService;
    private long ammountTip;
    private long ammountCashOver;
    private long montoFijo;
    private String nii2;
    private String track1;
    private String cvv;
    private String field54;
    private boolean isField55;
    private String field55;
    private String field57;
    private String field58;
    private String field59;
    private String field59Print;
    private String field61;
    private String arqc;
    private String tc;
    private String tvr;
    private String tsi;
    private String addRespData;
    private String msgType;
    private String typeDeferred;
    private String field57Print;
    private String ott;
    private String token;
    private String idVendedor;
    private String cedula;
    private String telefono;
    private String idPreAutAmpl;
    private String typeTransElectronic;
    private String pagoVarioSeleccionado;
    private String pagoVarioSeleccionadoNombre;
    private boolean alreadyPrinted = false;
    private String promptsPrinter;
    private String promptsAmountPrinter;
    private String tipoMontoFijo;
    private boolean multicomercio;
    private String idComercio;
    private String nameMultAcq;
    private String midmultacq;
    private String midInteroper;
    private String panPE;

    public long getAmmountXX() {
        return ammountXX;
    }

    public void setAmmountXX(long ammountXX) {
        this.ammountXX = ammountXX;
    }

    public long getAmmount0() {
        return ammount0;
    }

    public void setAmmount0(long ammount0) {
        this.ammount0 = ammount0;
    }

    public long getAmmountIVA() {
        return ammountIVA;
    }

    public void setAmmountIVA(long ammountIVA) {
        this.ammountIVA = ammountIVA;
    }

    public long getAmmountService() {
        return ammountService;
    }

    public void setAmmountService(long ammountService) {
        this.ammountService = ammountService;
    }

    public long getAmmountTip() {
        return ammountTip;
    }

    public void setAmmountTip(long ammountTip) {
        this.ammountTip = ammountTip;
    }

    public long getAmmountCashOver() {
        return ammountCashOver;
    }

    public void setAmmountCashOver(long ammountCashOver) {
        this.ammountCashOver = ammountCashOver;
    }

    public long getMontoFijo() {
        return montoFijo;
    }

    public void setMontoFijo(long montoFijo) {
        this.montoFijo = montoFijo;
    }

    public String getNii2() {
        return nii2;
    }

    public void setNii2(String nii) {
        this.nii2 = nii;
    }

    public String getTrack1() {
        return track1;
    }

    public void setTrack1(String track1) {
        this.track1 = track1;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public boolean isField55() {
        return isField55;
    }

    public void setIsField55(boolean field55) {
        isField55 = field55;
    }

    public String getField55() {
        return field55;
    }

    public void setField55(String field55) {
        this.field55 = field55;
    }

    public String getField54() {
        return field54;
    }

    public void setField54(String field54) {
        this.field54 = field54;
    }

    public String getField57() {
        return field57;
    }

    public void setField57(String field57) {
        this.field57 = field57;
    }

    public String getField58() {
        return field58;
    }

    public void setField58(String field58) {
        this.field58 = field58;
    }

    public String getField59() {
        return field59;
    }

    public void setField59(String field59) {
        this.field59 = field59;
    }

    public String getField59Print() {
        return field59Print;
    }

    public void setField59Print(String field59Print) {
        this.field59Print = field59Print;
    }

    public String getField61() {
        return field61;
    }

    public void setField61(String field61) {
        this.field61 = field61;
    }

    public String getArqc() {
        return arqc;
    }

    public void setArqc(String arqc) {
        this.arqc = arqc;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getTvr() {
        return tvr;
    }

    public void setTvr(String tvr) {
        this.tvr = tvr;
    }

    public String getTsi() {
        return tsi;
    }

    public void setTsi(String tsi) {
        this.tsi = tsi;
    }

    public String getAddRespData() {
        return addRespData;
    }

    public void setAddRespData(String addRespData) {
        this.addRespData = addRespData;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getTypeDeferred() {
        return typeDeferred;
    }

    public void setTypeDeferred(String typeDeferred) {
        this.typeDeferred = typeDeferred;
    }

    public String getField57Print() {
        return field57Print;
    }

    public void setField57Print(String field57Print) {
        this.field57Print = field57Print;
    }

    public String getOtt() {
        return ott;
    }

    public void setOtt(String ott) {
        this.ott = ott;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setTransEName(String transEName) {
        this.transEName = transEName;
    }

    public String getIdPreAutAmpl() {
        return idPreAutAmpl;
    }

    public void setIdPreAutAmpl(String idPreAutAmpl) {
        this.idPreAutAmpl = idPreAutAmpl;
    }

    public String getTypeTransElectronic() {
        return typeTransElectronic;
    }

    public void setTypeTransElectronic(String typeTransElectronic) {
        this.typeTransElectronic = typeTransElectronic;
    }

    public String getPagoVarioSeleccionado() {
        return pagoVarioSeleccionado;
    }

    public void setPagoVarioSeleccionado(String pagoVarioSeleccionado) {
        this.pagoVarioSeleccionado = pagoVarioSeleccionado;
    }

    public String getPagoVarioSeleccionadoNombre() {
        return pagoVarioSeleccionadoNombre;
    }

    public void setPagoVarioSeleccionadoNombre(String pagoVarioSeleccionadoNombre) {
        this.pagoVarioSeleccionadoNombre = pagoVarioSeleccionadoNombre;
    }

    public boolean isAlreadyPrinted() {
        return alreadyPrinted;
    }

    public void setAlreadyPrinted(boolean alreadyPrinted) {
        this.alreadyPrinted = alreadyPrinted;
    }

    public String getPromptsPrinter() {
        return promptsPrinter;
    }

    public void setPromptsPrinter(String promptsPrinter) {
        this.promptsPrinter = promptsPrinter;
    }

    public String getPromptsAmountPrinter() {
        return promptsAmountPrinter;
    }

    public void setPromptsAmountPrinter(String promptsAmountPrinter) {
        this.promptsAmountPrinter = promptsAmountPrinter;
    }

    public String getTipoMontoFijo() {
        return tipoMontoFijo;
    }

    public void setTipoMontoFijo(String tipoMontoFijo) {
        this.tipoMontoFijo = tipoMontoFijo;
    }

    public boolean isMulticomercio() {
        return multicomercio;
    }

    public void setMulticomercio(boolean multicomercio) {
        this.multicomercio = multicomercio;
    }

    public String getIdComercio() {
        return idComercio;
    }

    public void setIdComercio(String idComercio) {
        this.idComercio = idComercio;
    }

    public String getMidInteroper() {
        return midInteroper;
    }

    public void setMidInteroper(String midInteroper) {
        this.midInteroper = midInteroper;
    }

    public String getPanPE() {
        return panPE;
    }

    public void setPanPE(String panPE) {
        this.panPE = panPE;
    }

    public String getNameMultAcq() {
        return nameMultAcq;
    }

    public void setNameMultAcq(String nameMultAcq) {
        this.nameMultAcq = nameMultAcq;
    }

    public String getMidmultacq() {
        return midmultacq;
    }

    public void setMidmultacq(String midmultacq) {
        this.midmultacq = midmultacq;
    }

    public boolean getMode() {
        return false;
    }
}
