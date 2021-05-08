package com.bcp.rest.httpclient;

import android.content.Context;
import android.content.ContextWrapper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.bcp.settings.BCPConfig;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import static com.bcp.definesbcp.Definesbcp.NAME_FOLDER_CERTIFICATE;


/**
 * Created by jhonmorantes on 14/10/16.
 */

public class MySingleton {

    private static final String CLIENT_AGREEMENT = "TLSv1.2";
    private static final String CLIENT_KEY_KEYSTORE = "PKCS12";

    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private Context context;
    private boolean tls;

    public MySingleton(Context context, boolean tls) {
        this.context = context;
        this.tls = tls;
        requestQueue = getRequestQueue();
    }

    /*
    * @return The volley request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue(){

        if (requestQueue == null){
            try {
                if (tls){
                    Logger.logLine(Logger.LOG_GENERAL,"Instanciando RequestQueue");
                    requestQueue = Volley.newRequestQueue(context.getApplicationContext(), new HurlStack(null, newSslSocketFactory()));
                    Logger.logLine(Logger.LOG_GENERAL,"Finalizando Instancia RequestQueue");
                }
                else
                    requestQueue = Volley.newRequestQueue(context.getApplicationContext());
            } catch ( Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
            }
        }
        return requestQueue;
    }

    private SSLSocketFactory newSslSocketFactory() throws
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, IOException, UnrecoverableKeyException {

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(NAME_FOLDER_CERTIFICATE, Context.MODE_PRIVATE);

        File file = new File(directory + File.separator + PolarisUtil.TLS);
        KeyStore keyStore;
        try (FileInputStream inputStream = new FileInputStream(file)) {

            keyStore = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
            keyStore.load(inputStream, BCPConfig.getInstance(context).getPwdKeystoreHttps(BCPConfig.PWDKEYSTOREHTTPSKEY).toCharArray());
        }

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
        kmf.init(keyStore, BCPConfig.getInstance(context).getPwdKeystoreHttps(BCPConfig.PWDKEYSTOREHTTPSKEY).toCharArray());

        KeyManager[] keyManagers = kmf.getKeyManagers();
        SSLContext sslContext = SSLContext.getInstance(CLIENT_AGREEMENT);
        sslContext.init(keyManagers, null, null);

        return sslContext.getSocketFactory();
    }


    public static synchronized MySingleton getmInstance(Context context, boolean tls){

        if (mInstance == null){
            mInstance = new MySingleton(context, tls);
        }

        return mInstance;
    }

    public <T>void addToRequestque(Request<T> request){
        try {
            Logger.logLine(Logger.LOG_GENERAL, "Enviando petición");
            requestQueue.add(request);
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }
}
