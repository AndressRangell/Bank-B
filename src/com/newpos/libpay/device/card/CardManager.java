package com.newpos.libpay.device.card;

import android.os.ConditionVariable;

import com.newpos.bypay.EmvL2;
import com.newpos.bypay.EmvL2App;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.contactless.LedManager;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;
import com.pos.device.SDKException;
import com.pos.device.icc.ContactCard;
import com.pos.device.icc.IccReader;
import com.pos.device.icc.OperatorMode;
import com.pos.device.icc.SlotType;
import com.pos.device.icc.VCC;
import com.pos.device.magcard.MagCardReader;
import com.pos.device.magcard.MagneticCard;
import com.pos.device.magcard.TrackInfo;
import com.pos.device.picc.EmvContactlessCard;
import com.pos.device.picc.PiccReader;

import static com.newpos.libpay.trans.finace.FinanceTrans.INMODE_HAND;

/**
 * Created by zhouqiang on 2017/3/14.
 * @author zhouqiang
 * 寻卡管理者
 */

public class CardManager {

    public static final int TYPE_MAG = 1 ;
    public static final int TYPE_ICC = 2 ;
    public static final int TYPE_NFC = 3 ;
    public static final int TYPE_HAND= 4 ;

    public static final int INMODE_MAG = 0x02;
    public static final int INMODE_IC = 0x08;
    public static final int INMODE_NFC = 0x10;

    private static CardManager instance ;

    private static int mode ;

    private CardManager(){}

    public static CardManager getInstance(int m){
        mode = m ;
        if(null == instance){
            instance = new CardManager();
        }
        return instance ;
    }

    private MagCardReader magCardReader ;
    private IccReader iccReader ;
    private PiccReader piccReader ;
    private ManualCardEntry manualCardEntry;
    private EmvContactlessCard mEmvContactlessCard;

    public EmvContactlessCard getmEmvContactlessCard() {
        return mEmvContactlessCard;
    }

    public void setmEmvContactlessCard(EmvContactlessCard mEmvContactlessCard) {
        this.mEmvContactlessCard = mEmvContactlessCard;
    }

    public int getMode(){
        return mode;
    }

    public void setMode(int pmode){
        mode=pmode;
    }

    public void piccReset() {
        try {
            piccReader.reset();
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private void init(){
        if( (mode & INMODE_MAG ) != 0 ){
            magCardReader = MagCardReader.getInstance();
        }
        if( (mode & INMODE_IC) != 0 ){
            iccReader = IccReader.getInstance(SlotType.USER_CARD);
        }
        if( (mode & INMODE_NFC) != 0 ){
            piccReader = PiccReader.getInstance();
        }
        if( (mode & INMODE_HAND) != 0 ){
            manualCardEntry = ManualCardEntry.getInstance();
        }
        isEnd = false ;
    }

    private void stopMAG(){
        try {
            if(magCardReader!=null){
                magCardReader.stopSearchCard();
            }
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private void stopICC(){
        if(iccReader!=null){
            try {
                iccReader.stopSearchCard();
            } catch (SDKException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
        }
    }

    public void stopPICC(){
        if(piccReader!=null){
            LedManager.getInstance().turnOffAll();
            piccReader.stopSearchCard();
            try {
                piccReader.release();
            } catch (SDKException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
        }
    }

    public void releaseAll(){
        isEnd = true ;
        try {
            if(magCardReader!=null){
                magCardReader.stopSearchCard();
                Logger.debug("mag stop");
            }
            if(iccReader!=null){
                iccReader.stopSearchCard();
                iccReader.release();
                Logger.debug("icc stop");
            }
            if(piccReader!=null){
                piccReader.stopSearchCard();
                piccReader.release();
                Logger.debug("picc stop");
            }
        } catch (SDKException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private CardListener listener ;

    private boolean isEnd = false ;

    public void getCard(final int timeout , CardListener l){
        Logger.debug("CardManager>>getCard>>timeout="+timeout);
        init();
        final CardInfo info = new CardInfo() ;
        if(null == l){
            info.setResultFalg(false);
            info.setErrno(TcodeError.T_INVOKE_PARA_ERR);
            listener.callback(info);
        }else {
            this.listener = l ;
            new Thread(){
                @Override
                public void run(){
                    try{
                        if( (mode & INMODE_MAG) != 0 ){
                            Logger.debug("CardManager>>getCard>>MAG");
                            magCardReader.startSearchCard(timeout, (i, magneticCard) -> {
                                if(!isEnd){
                                    Logger.debug("CardManager>>getCard>>MAG>>i="+i);
                                    isEnd = true ;
                                    stopICC();
                                    stopPICC();
                                    if( 0 == i ){
                                        listener.callback(handleMAG(magneticCard));
                                    }
                                    else {
                                        info.setResultFalg(false);
                                        if (2==i)
                                            info.setErrno(TcodeError.T_WAIT_TIMEOUT);
                                        else
                                            info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                                        listener.callback(info);
                                    }
                                }
                            });
                        }if( (mode & INMODE_IC) != 0 ){
                            Logger.debug("CardManager>>getCard>>ICC");
                            iccReader.startSearchCard(timeout, i -> {
                                if(!isEnd){
                                    Logger.debug("CardManager>>getCard>>ICC>>i="+i);
                                    isEnd = true ;
                                    stopMAG();
                                    stopPICC();
                                    if( 0 == i ){
                                        try {
                                            listener.callback(handleICC());
                                        } catch (SDKException e) {
                                            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                                            info.setResultFalg(false);
                                            info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                                            listener.callback(info);
                                        }
                                    }else {
                                        info.setResultFalg(false);
                                        if (2==i)
                                            info.setErrno(TcodeError.T_WAIT_TIMEOUT);
                                        else
                                            info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                                        listener.callback(info);
                                    }
                                }
                            });
                        }if( (mode & INMODE_NFC) != 0 ){
                            Logger.debug("CardManager>>getCard>>NFC");
                            piccReader.startSearchCard(timeout, (i, i1) -> {
                                try {
                                    Thread.sleep(400);
                                } catch (InterruptedException e) {
                                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                                    Thread.currentThread().interrupt();
                                }
                                if(!isEnd){
                                    Logger.debug("CardManager>>getCard>>NFC>>i="+i);
                                    isEnd = true ;
                                    stopICC();
                                    stopMAG();
                                    if( 0 == i ){
                                        listener.callback(handlePICC(i1));
                                    }else {
                                        info.setResultFalg(false);
                                        if (2==i)
                                            info.setErrno(TcodeError.T_WAIT_TIMEOUT);
                                        else
                                            info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                                        listener.callback(info);
                                    }
                                }
                            });
                        }
                    }catch (SDKException sdk){
                        Logger.logLine(Logger.LOG_EXECPTION, sdk.getStackTrace(), "SDKException="+sdk.getMessage());
                        releaseAll();
                        info.setResultFalg(false);
                        info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                        listener.callback(info);
                    }
                }
            }.start();
        }
    }

    public void getCard(final int timeout, final TransView transView, CardListener l){
        Logger.debug("CardManager>>getCard>>timeout="+timeout);
        init();
        final CardInfo info = new CardInfo() ;
        if(null == l){
            info.setResultFalg(false);
            info.setErrno(TcodeError.T_INVOKE_PARA_ERR);
            listener.callback(info);
        }else {
            this.listener = l ;
            new Thread(){
                @Override
                public void run(){
                    try{
                        if( (mode & INMODE_MAG) != 0 ){
                            Logger.debug("CardManager>>getCard>>MAG");
                            magCardReader.startSearchCard(timeout, (i, magneticCard) -> {
                                if(!isEnd){
                                    Logger.debug("CardManager>>getCard>>MAG>>i="+i);
                                    isEnd = true ;
                                    stopICC();
                                    stopPICC();
                                    if( 0 == i ){
                                        listener.callback(handleMAG(magneticCard));
                                    }
                                    else {
                                        info.setResultFalg(false);
                                        if (2==i)
                                            info.setErrno(TcodeError.T_WAIT_TIMEOUT);
                                        else
                                            info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                                        listener.callback(info);
                                    }
                                }
                            });
                        }if( (mode & INMODE_IC) != 0 ){
                            Logger.debug("CardManager>>getCard>>ICC");
                            iccReader.startSearchCard(timeout, i -> {
                                if(!isEnd){
                                    Logger.debug("CardManager>>getCard>>ICC>>i="+i);
                                    isEnd = true ;
                                    stopMAG();
                                    stopPICC();
                                    if( 0 == i ){
                                        try {
                                            listener.callback(handleICC());
                                        } catch (SDKException e) {
                                            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                                            info.setResultFalg(false);
                                            info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                                            listener.callback(info);
                                        }
                                    }else {
                                        info.setResultFalg(false);
                                        if (2==i)
                                            info.setErrno(TcodeError.T_WAIT_TIMEOUT);
                                        else
                                            info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                                        listener.callback(info);
                                    }
                                }
                            });
                        }if( (mode & INMODE_NFC) != 0 ){
                            Logger.debug("CardManager>>getCard>>NFC");
                            piccReader.startSearchCard(timeout, (i, i1) -> {
                                try {
                                    Thread.sleep(400);
                                } catch (InterruptedException e) {
                                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                                    Thread.currentThread().interrupt();
                                }
                                if(!isEnd){
                                    Logger.debug("CardManager>>getCard>>NFC>>i="+i);
                                    isEnd = true ;
                                    stopICC();
                                    stopMAG();
                                    if( 0 == i ){
                                        listener.callback(handlePICC(i1));
                                    }else {
                                        info.setResultFalg(false);
                                        if (2==i)
                                            info.setErrno(TcodeError.T_WAIT_TIMEOUT);
                                        else
                                            info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                                        listener.callback(info);
                                    }
                                }
                            });
                        }if( (mode & INMODE_HAND) != 0 ){
                            Logger.debug("CardManager>>getCard>>HAND");
                            manualCardEntry.startSearchCard(transView, (var1, cardInfo) -> {
                                if(!isEnd) {
                                    Logger.debug("CardManager>>getCard>>HAND>>i="+var1);
                                    isEnd = true ;
                                    stopICC();
                                    stopMAG();
                                    stopPICC();

                                    if (0 == var1) {
                                        listener.callback(cardInfo);
                                    } else {
                                        info.setResultFalg(false);
                                        if (1 == var1) {
                                            info.setErrno(TcodeError.T_USER_CANCEL);
                                            listener.callback(info);
                                        }
                                    }
                                }
                            });
                        }
                    }catch (SDKException sdk){
                        Logger.logLine(Logger.LOG_EXECPTION, sdk.getStackTrace(), "SDKException="+sdk.getMessage());
                        releaseAll();
                        info.setResultFalg(false);
                        info.setErrno(TcodeError.T_SEARCH_CARD_ERR);
                        listener.callback(info);
                    }
                }
            }.start();
        }
    }

    private CardInfo handleMAG(MagneticCard card){
        CardInfo info = new CardInfo() ;
        info.setResultFalg(true);
        info.setCardType(CardManager.TYPE_MAG);
        TrackInfo ti1 = card.getTrackInfos(MagneticCard.TRACK_1);
        TrackInfo ti2 = card.getTrackInfos(MagneticCard.TRACK_2);
        TrackInfo ti3 = card.getTrackInfos(MagneticCard.TRACK_3);
        info.setTrackNo(new String[]{ti1.getData() , ti2.getData() , ti3.getData()});
        return info ;
    }

    private CardInfo handleICC() throws SDKException {
        CardInfo info = new CardInfo();
        info.setCardType(CardManager.TYPE_ICC);
        if (iccReader.isCardPresent()) {
            ContactCard contactCard = iccReader.connectCard(VCC.VOLT_5 , OperatorMode.EMV_MODE);
            byte[] atr = contactCard.getATR() ;
            if (atr.length != 0) {
                info.setResultFalg(true);
                info.setCardAtr(atr);
            } else {
                info.setResultFalg(false);
                info.setErrno(TcodeError.T_IC_POWER_ERR);
            }
        } else {
            info.setResultFalg(false);
            info.setErrno(TcodeError.T_IC_NOT_EXIST_ERR);
        }
        return info;
    }

    private CardInfo handlePICC(int nfcType){
        CardInfo info = new CardInfo();
        info.setResultFalg(true);
        info.setCardType(CardManager.TYPE_NFC);
        info.setNfcType(nfcType);
        return info ;
    }

    private int timeout= 60*1000;
    public int detectCards(EmvL2App param){
        ConditionVariable mVariable;
        mVariable = new ConditionVariable();
        final int[] ret = {0};
        releaseAll();
        if (param.DetectMagStripe) {
            //Not support
        }
        if (param.DetectContact) {
            //Not Support
        }
        if (param.DetectContactLess) {
            piccReader = PiccReader.getInstance();
            Logger.debug("use contactless card");
            piccReader.startSearchCard(timeout, (i, i1) -> {
                Logger.debug("get contactless card i = "+i+" i1 = "+i1);
                if (i == 0) {
                    ret[0] = EmvL2.L2_CS_PRESENT;
                    try {
                        mEmvContactlessCard = EmvContactlessCard.connect();
                    } catch (SDKException e) {
                        Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                    }
                    mode = TYPE_NFC;
                }else {
                    Logger.debug("get picc error error");

                }
                stopICC();
                stopMAG();
                mVariable.open();
            });
        }

        mVariable.block();
        return ret[0];
    }
}
