package com.bcp.inicializacion.configuracioncomercio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;

import com.bcp.inicializacion.tools.PolarisUtil;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.trans.TcodeError;

import org.jose4j.base64url.internal.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static com.bcp.definesbcp.Definesbcp.NAME_FOLDER_CERTIFICATE;
import static com.bcp.inicializacion.tools.PolarisUtil.LLAVE;
import static com.bcp.inicializacion.tools.PolarisUtil.NUMERO_TARJETA;
import static com.bcp.inicializacion.tools.PolarisUtil.TLS;
import static com.bcp.inicializacion.tools.PolarisUtil.TRANSACCIONPRIV;
import static com.bcp.inicializacion.tools.PolarisUtil.TRANSACCIONPUB;
import static org.jose4j.base64url.internal.apache.commons.codec.binary.Base64.decodeBase64;

public class Certificate {

    private static final String CLIENT_KEY_KEYSTORE = "PKCS12";
    private final static String algoritmo = "AES";

    private String publicKeyInit;
    private String privateKeyInit;
    private String publicKeyTrans;
    private String privateKeyTrans;

    public int readCertificate(Context context, String aFileName, String pwdKeystore) {

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(NAME_FOLDER_CERTIFICATE, Context.MODE_PRIVATE);

        File file = new File(directory + File.separator + aFileName);

        try {
            if (file.exists()) {
                KeyStore keyStore = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    keyStore.load(inputStream, pwdKeystore.toCharArray());
                }

                Enumeration<String> enumeration = keyStore.aliases();
                while(enumeration.hasMoreElements()) {
                    String publicKeyString = "";
                    String privateKeyString = "";
                    String aliasKey = enumeration.nextElement();
                    Key key = keyStore.getKey(aliasKey, pwdKeystore.toCharArray());
                    // Get certificate of public key
                    java.security.cert.Certificate cert = keyStore.getCertificate(aliasKey);
                    // Get public key
                    PublicKey publicKey = cert.getPublicKey();

                    publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());

                    // Get private key
                    if (!aFileName.equals(PolarisUtil.TRANSACCIONPUB)) {
                        if (keyStore.isKeyEntry(aliasKey)){
                            if (key instanceof PrivateKey)
                                privateKeyString = Base64.encodeBase64String(key.getEncoded());
                            else
                                return TcodeError.T_ERR_KEY_PRV;
                        }
                    }

                    switch (aFileName){
                        case PolarisUtil.TRANSACCIONPUB:
                            setPublicKeyTrans(publicKeyString);
                            break;
                        case PolarisUtil.TRANSACCIONPRIV:
                            setPrivateKeyTrans(privateKeyString);
                            break;
                        case PolarisUtil.NUMERO_TARJETA:
                            setPublicKeyInit(publicKeyString);
                            break;
                        case PolarisUtil.LLAVE:
                            setPrivateKeyInit(privateKeyString);
                            break;
                        default:
                            break;
                    }
                }
                return 0;
            }else {
                return TcodeError.T_ERR_FILE_NOT_EXIST;
            }
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException ex) {
            Logger.logLine(Logger.LOG_EXECPTION, ex.getStackTrace(), " certificate 1 " +  Objects.requireNonNull(ex.getMessage()));
            return TcodeError.T_ERR_SAVE_KEYSTORE;
        }
    }

    public String getPublicKeyInit() {
        return publicKeyInit;
    }

    public void setPublicKeyInit(String publicKeyInit) {
        this.publicKeyInit = publicKeyInit;
    }

    public String getPrivateKeyInit() {
        return privateKeyInit;
    }

    public void setPrivateKeyInit(String privateKeyInit) {
        this.privateKeyInit = privateKeyInit;
    }

    public String getPublicKeyTrans() {
        return publicKeyTrans;
    }

    public void setPublicKeyTrans(String publicKeyTrans) {
        this.publicKeyTrans = publicKeyTrans;
    }

    public String getPrivateKeyTrans() {
        return privateKeyTrans;
    }

    public void setPrivateKeyTrans(String privateKeyTrans) {
        this.privateKeyTrans = privateKeyTrans;
    }

    public String[] getNameKeystore(int id, Context context)
    {
        String[] ret= new String[2];
        switch (id) {
            case 0:
                ret[0] =NUMERO_TARJETA;
                ret[1] = BCPConfig.getInstance(context).getPwdKeyStorePolaris(BCPConfig.PWDKEYSTOREPOLARISKEY);
                break;
            case 1:
                ret[0] = TRANSACCIONPUB;
                ret[1] = BCPConfig.getInstance(context).getPwdKeystoreTransPub(BCPConfig.PWDKEYSTORETRANSPUBKEY);
                break;
            case 2:
                ret[0] =LLAVE;
                ret[1] = BCPConfig.getInstance(context).getPwdKeystoreBcp(BCPConfig.PWDKEYSTOREBCPKEY);
                break;
            case 3:
                ret[0] = TRANSACCIONPRIV;
                ret[1] =  BCPConfig.getInstance(context).getPwdKeystoreTransPriv(BCPConfig.PWDKEYSTORETRANSPRIVKEY);
                break;
            case 4:
                ret[0] =TLS;
                ret[1] = BCPConfig.getInstance(context).getPwdKeystoreHttps(BCPConfig.PWDKEYSTOREHTTPSKEY);
                break;
            default:
                ret = new String[0];
                break;
        }
        return ret;
    }

    public String decrypt(String data, byte[] key) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {

        byte[] encryptedData  = decodeBase64(data);
        @SuppressLint("GetInstance") Cipher c = Cipher.getInstance(algoritmo);
        SecretKeySpec k = new SecretKeySpec(key, algoritmo);
        c.init(Cipher.DECRYPT_MODE, k);
        byte[] decrypted = c.doFinal(encryptedData);
        return new String(decrypted);
    }
}
