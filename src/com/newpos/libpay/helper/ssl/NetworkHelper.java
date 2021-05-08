package com.newpos.libpay.helper.ssl;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.android.newpos.pay.R;
import com.newpos.libpay.Logger;
import com.newpos.libpay.utils.ISOUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * 网络助手类
 *
 * @author zhouqiang
 */
public class NetworkHelper {

    private Socket socket;//SSLSocket对象
    private InputStream is; // 输入流
    private OutputStream os; // 输出流
    private String ip;//连接IP地址
    private int port;//连接端口号
    private Context tcontext;//上下文对象
    private int timeoutRsp; //超时时间
    private int timeoutCon; //超时时间
    private int protocol; // 协议 0: 2字节长度+数据 1:stx协议
    private static final String CLIENT_AGREEMENT = "TLSv1.2"; // 使用协议
    private static final String CLIENT_KEY_PASS = "Wposs.2020";// 密码
    private static final String EXCEPTION = "EXCEPTION";
    private String tls;

    /**
     * @param ip 初始化连接的IP
     * @throws IOException
     * @throws UnknownHostException
     */
    public NetworkHelper(String ip, int port, int timeoutRsp, int timeoutCon, String tls, Context context) {
        this.ip = ip;
        this.port = port;
        this.timeoutRsp = timeoutRsp;
        this.timeoutCon = timeoutCon;
        this.tls = tls;
        this.tcontext = context;
    }

    /**
     * 连接socket
     *
     * @return
     * @throws IOException
     */
    public int connect() {
        try {

            Logger.logLine(Logger.LOG_GENERAL, "connect - NetworkHelper");


            if (ISOUtil.stringToBoolean(tls)) {

                Logger.logLine(Logger.LOG_GENERAL, "TLS - Activo");


                Resources res = tcontext.getResources();
                InputStream tlsKeyStore = res.openRawResource(R.raw.tslpolaris);
                AndroidSocketFactory sf = new AndroidSocketFactory(tlsKeyStore);
                sf.setAlgorithm(CLIENT_AGREEMENT);
                sf.setKeyPassword(CLIENT_KEY_PASS);
                sf.setPassword(CLIENT_KEY_PASS);
                sf.setServerAuthNeeded(false);
                sf.setClientAuthNeeded(false);
                socket = sf.createSocket(ip, port);

                socket.setSoTimeout(timeoutCon);
                Logger.logLine(Logger.LOG_GENERAL, "setSoTimeout : "+ timeoutCon);
            } else {
                socket = new Socket(ip, port);
                socket.setSoTimeout(timeoutRsp);

                Logger.logLine(Logger.LOG_GENERAL, "setSoTimeout : "+timeoutRsp);
            }


            Logger.logLine(Logger.LOG_GENERAL, "IP : "+ ip + " puerto : " + port);


            is = socket.getInputStream();
            os = socket.getOutputStream();
        } catch (Exception e) {

            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), EXCEPTION + e.toString());
            return -1;
        }

        Logger.logLine(Logger.LOG_GENERAL, "return 0");
        return 0;
    }

    /**
     * 关闭socket
     */
    public int close() {
        try {
            Logger.logLine(Logger.LOG_GENERAL, "socket.close");
            socket.close();
        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, " NetwarokHelper " +  e.getMessage());
            return -1;
        }
        return 0;
    }

    /**
     * 发送数据包
     *
     * @param data
     * @return
     */
    public int send(byte[] data) {
        byte[] newData = null;
        if (protocol == 0) {
            newData = new byte[data.length + 2];
            newData[0] = (byte) (data.length >> 8);
            newData[1] = (byte) data.length;// 丢失高位
            System.arraycopy(data, 0, newData, 2, data.length);
        }
        try {
            os.write(newData);
            os.flush();
        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " NetwaorkHelper 1 " +  e.getMessage());
            return -1;
        }
        return 0;
    }

    /**
     * 接受数据包
     *
     * @return
     * @throws IOException
     */
    public byte[] recive(int max) throws IOException {
        ByteArrayOutputStream byteOs;
        byte[] resP = null;
        if (protocol == 0) {
            byte[] packLen = new byte[2];
            int len;
            byte[] bb = new byte[2 + max];
            int i;
            byteOs = new ByteArrayOutputStream();
            i = is.read(packLen);
            try {
                if (i != -1) {
                    len = ISOUtil.byte2int(packLen);
                    while (len > 0 && (i = is.read(bb)) != -1) {
                        byteOs.write(bb, 0, i);
                        len -= i;
                    }
                }
            } catch (InterruptedIOException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " NetwaorkHelper 2 " +  e.getMessage());
                // 读取超时处理
                Log.w("PAY_SDK", "recive：读取流数据超时异常");
                return new byte[0];
            }
            resP = byteOs.toByteArray();
        }
        return resP;
    }
}
