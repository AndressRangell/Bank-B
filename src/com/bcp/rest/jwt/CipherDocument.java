package com.bcp.rest.jwt;

import com.newpos.libpay.Logger;
import com.newpos.libpay.device.pinpad.PinpadManager;
import com.newpos.libpay.utils.ISOUtil;

public class CipherDocument {

    private String pan = "9898989898989898";
    private String document;

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    private String prepareDocument(){
        int len = document.length();
        String tmpDoc;

        if (len>9){
            tmpDoc = ISOUtil.zeropad(Integer.toHexString(len).toUpperCase(), 2);
        }else {
            tmpDoc = ISOUtil.zeropad(String.valueOf(len), 2);
        }

        document = ISOUtil.padright(document+"", 14, 'F');
        return tmpDoc + document;

    }

    private String preparePan(){
        pan = pan.substring(pan.length() - 13, pan.length() - 1);
        pan = ISOUtil.padleft(pan, pan.length() + 4, '0');
        return pan;
    }

    public boolean generatePinblock(){

        try {
            String tmp = ISOUtil.hexor(preparePan(), prepareDocument());
            document = PinpadManager.getInstance().getDocCipher(0, tmp);
            return true;
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return false;
        }
    }
}
