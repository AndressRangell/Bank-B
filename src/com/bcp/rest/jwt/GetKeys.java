package com.bcp.rest.jwt;

import android.util.Base64;
import com.newpos.libpay.Logger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static com.android.newpos.pay.ProcessingCertificate.certificate;

public class GetKeys {

    private boolean isInitTrans;

    public GetKeys(boolean isInitTrans) {
        this.isInitTrans = isInitTrans;
    }

    public PublicKey getPublicKey(){

        String certB64;

        if (isInitTrans) {
            certB64 = getWpossInitPublicKey();
        } else {
            certB64 = getWpossTrxPublicKey();
        }


        byte[] keyBytes = Base64.decode(certB64, Base64.DEFAULT);
        X509EncodedKeySpec pkcs8EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

        try {
            return KeyFactory.getInstance("RSA").generatePublic(pkcs8EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return null;
        }
    }

    public PrivateKey getPrivateKey(){

        String certB64;

        if (isInitTrans) {
            certB64 = getWpossInitPrivateKey();
        } else {
            certB64 = getWpossTrxPrivateKey();
        }

        byte[] keyBytes = Base64.decode(certB64, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);

        try {
            return KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            return null;
        }
    }

    private String getWpossInitPublicKey(){
        return certificate.getPublicKeyInit();
    }

    private String getWpossInitPrivateKey(){
        return certificate.getPrivateKeyInit();
    }

    private String getWpossTrxPublicKey(){
        return certificate.getPublicKeyTrans();
    }

    private String getWpossTrxPrivateKey(){
        return certificate.getPrivateKeyTrans();
    }
}
