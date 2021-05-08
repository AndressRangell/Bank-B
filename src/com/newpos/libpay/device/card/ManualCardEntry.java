package com.newpos.libpay.device.card;

import com.android.desert.keyboard.InputManager;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.user.OnUserResultListener;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.TcodeError;

import org.json.JSONObject;

import java.util.Map;

public class ManualCardEntry {

    private static ManualCardEntry mInstance = null;
    private int mRet = 0;
    private InputManager.Style payStyle;

    public static ManualCardEntry getInstance() {
        if (mInstance==null){
            mInstance = new ManualCardEntry();
        }

        return mInstance;
    }

    /**
     * object lock
     */
    private Object o = new byte[0] ;



    /**
     * block
     */
    private void funWait(){
        synchronized (o){
            try {
                o.wait();
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "error" + e);
                Thread.currentThread().interrupt();
            }
        }
    }

    private final OnUserResultListener listener = new OnUserResultListener() {

        /**
         * Notify
         */
        private void listenNotify(){
            synchronized (o){
                o.notifyAll();
            }
        }

        @Override
        public void confirm(InputManager.Style style) {
            mRet = 0;
            payStyle = style;
            listenNotify();
        }

        @Override
        public void cancel() {
            mRet = 1;
            listenNotify();
        }

        @Override
        public void cancel(int codeError) {
            mRet = 1;
            listenNotify();
        }

        @Override
        public void cancel(JSONObject jsonObject, Map<String, String> header, int statusCode) {
            mRet = 1;
            listenNotify();
        }

        @Override
        public void confirm(int applistselect) {
            mRet = applistselect;
            listenNotify();
        }

        @Override
        public void back() {
            mRet = 1;
            listenNotify();
        }
    };


    /**
     *
     * Not used timeout
     * @param transView
     * @param handleCardCallback
     */
    public void startSearchCard(TransView transView, final HandleCardManager handleCardCallback){

        transView.getResultListener(listener);

        final CardInfo cInfo = new CardInfo();

        funWait();

        if (mRet == 1) {
            cInfo.setResultFalg(false);
            cInfo.setErrno(TcodeError.T_USER_CANCEL);
            handleCardCallback.onSearchResult(HandleCardManager.USER_CANCEL,cInfo);
        }else {
            cInfo.setResultFalg(true);
            cInfo.setCardType(CardManager.TYPE_HAND);
            handleCardCallback.onSearchResult(HandleCardManager.SUCCESS,cInfo);
        }
    }
}
