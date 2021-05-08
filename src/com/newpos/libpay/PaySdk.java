package com.newpos.libpay;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import com.android.newpos.pay.R;
import com.bcp.inicializacion.trans_init.Initialization;
import com.bcp.login.LoginSupervisor;
import com.bcp.mdm_validation.ReadWriteFileMDM;
import com.bcp.transactions.certificado.CertificateTransaction;
import com.bcp.transactions.consulta.Consulta;
import com.bcp.transactions.deposito.Deposito;
import com.bcp.transactions.giros.Giros;
import com.bcp.transactions.pagoservicios.PagoServicios;
import com.bcp.transactions.settle.Settle;
import com.bcp.transactions.echotest.EchoTest;
import com.bcp.transactions.retiro.Retiro;
import com.bcp.transactions.ultimasoperaciones.LastOperations;
import com.newpos.libpay.device.card.CardManager;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.global.TMConstants;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.presenter.TransUIImpl;
import com.newpos.libpay.presenter.TransView;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.TransInputPara;
import com.pos.device.SDKManager;

import static com.android.newpos.pay.StartAppBCP.readWriteFileMDM;

/**
 * Created by zhouqiang on 2017/4/25.
 *
 * @author zhouqiang
 * 支付sdk管理者
 */
public class PaySdk {

    /**
     * 单例
     */
    private static PaySdk mInstance = null;

    /**
     * 上下文对象，用于获取相关资源和使用其相应方法
     */
    private Context mContext = null;

    /**
     * 获前端段activity对象，主要用于扫码交易
     */
    private Activity mActivity = null;

    /**
     * MVP交媾P层接口，用于对m和v的交互
     */
    private TransPresenter presenter = null;

    /**
     * 标记sdk环境前端是否进行初始化操作
     */
    private static boolean isInit = false;

    /**
     * 初始化PaySdk环境的回调接口
     */
    private PaySdkListener mListener = null;

    /**
     * PaySdk产生的相关文件的保存路径
     * 如代码不进行设置，默认使用程序data分区
     *
     * @link @{@link String}
     */
    private String cacheFilePath = null;

    /**
     * 终端参数文件路径,用于设置一些交易中的偏好属性
     * 如代码不进行设置，默认使用程序自带配置文件
     *
     * @link @{@link String}
     */
    private String paraFilepath = null;

    public Context getContext() throws PaySdkException {
        if (this.mContext == null) {
            throw new PaySdkException(PaySdkException.PARA_NULL);
        }
        return mContext;
    }

    public PaySdk setActivity(Activity activity) {
        this.mActivity = activity;
        return mInstance;
    }

    public PaySdk setParaFilePath(String path) {
        this.paraFilepath = path;
        return mInstance;
    }

    public String getParaFilepath() {
        return this.paraFilepath;
    }

    public PaySdk setCacheFilePath(String path) {
        this.cacheFilePath = path;
        return mInstance;
    }

    public String getCacheFilePath() {
        return this.cacheFilePath;
    }

    private PaySdk() {
    }

    public PaySdk setListener(PaySdkListener listener) {
        this.mListener = listener;
        return mInstance;
    }

    public static PaySdk getInstance() {
        if (mInstance == null) {
            mInstance = new PaySdk();
        }
        return mInstance;
    }

    public void init(Context context) throws PaySdkException {
        this.mContext = context;
        this.init();
    }

    public void init(Context context, PaySdkListener listener) throws PaySdkException {
        this.mContext = context;
        this.mListener = listener;
        this.init();
    }
    public static final String S ="/";

    public void init() throws PaySdkException {
        if (this.mContext == null) {
            throw new PaySdkException(PaySdkException.PARA_NULL);
        }

        if (this.paraFilepath == null || !this.paraFilepath.endsWith("properties")) {
            this.paraFilepath = TMConstants.DEFAULTCONFIG;
        }

        if (this.cacheFilePath == null) {
            this.cacheFilePath = mContext.getFilesDir() + S;
        } else if (!this.cacheFilePath.endsWith("/")) {
            this.cacheFilePath += "/";
        }

        TMConfig.setRootFilePath(this.cacheFilePath);
        Log.i("init->paras files path:" , this.paraFilepath);
        Log.i("","init->cache files will be saved in:" + this.cacheFilePath);
        Log.i("","init->pay sdk will run based on:" + (TMConfig.getInstance().getBankid() == 1 ? "UNIONPAY" : "CITICPAY"));
        SDKManager.init(mContext, () -> {
            isInit = true;
            Log.i("","init->success");
            if (mListener != null) {
                mListener.success();
            }
        });
    }

    /**
     * 释放卡片驱动资源
     */
    public void releaseCard() {
        if (isInit) {
            CardManager.getInstance(0).releaseAll();
        }
    }

    /**
     * 释放sdk环境资源
     */
    public void exit() {
        if (isInit) {
            SDKManager.release();
            isInit = false;
        }
    }

    public void startTrans(String transType, TransView tv) throws PaySdkException {
        if (this.mActivity == null) {
            throw new PaySdkException(PaySdkException.PARA_NULL);
        }
        TransInputPara para = new TransInputPara();
        para.setTransUI(new TransUIImpl(mActivity,tv));

        //se activa el flag cuando se empieza una transaccion
        try {
            readWriteFileMDM.writeFileMDM(readWriteFileMDM.getReverse(), readWriteFileMDM.getSettle(), readWriteFileMDM.getInitAuto(), ReadWriteFileMDM.TRANS_ACTIVE);
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }

        if (transType.equals(Trans.ECHO_TEST)) {
            para.setTransType(Trans.ECHO_TEST);
            para.setNeedOnline(true);
            para.setNeedPrint(false);
            para.setNeedPass(false);
            para.setEmvAll(false);
            presenter = new EchoTest(this.mContext, Trans.ECHO_TEST, para);
        }
        //se setea a false parametros para evitar la solicitud del pin por el kernel
        if (transType.equals(Trans.RETIRO)) {
            para.setTransType(Trans.RETIRO);
            para.setNeedOnline(true);
            para.setNeedPrint(true);
            para.setNeedConfirmCard(false);
            para.setNeedPass(false);
            para.setNeedAmount(true);
            para.setEmvAll(true);
            presenter = new Retiro(this.mContext, Trans.RETIRO, para);
        }
        if (transType.equals(Trans.DEPOSITO)) {
            para.setTransType(mActivity.getResources().getString(R.string.deposito));
            para.setNeedOnline(true);
            para.setNeedPrint(true);
            para.setNeedConfirmCard(false);
            para.setNeedPass(false);
            para.setNeedAmount(false);
            para.setEmvAll(true);
            presenter = new Deposito(this.mContext, Trans.DEPOSITO, para);
        }
        if (transType.equals(Trans.GIROS)) {
            para.setTransType(mActivity.getResources().getString(R.string.giros));
            para.setNeedOnline(true);
            para.setNeedPrint(true);
            para.setNeedConfirmCard(false);
            para.setNeedPass(false);
            para.setNeedAmount(false);
            para.setEmvAll(true);
            presenter = new Giros(this.mContext, Trans.GIROS, para);
        }

        if (transType.equals(Trans.PAGOSERVICIOS)) {
            para.setTransType(mActivity.getResources().getString(R.string.pagoServicios));
            para.setNeedOnline(true);
            para.setNeedPrint(true);
            para.setNeedConfirmCard(false);
            para.setNeedPass(false);
            para.setNeedAmount(false);
            para.setEmvAll(true);
            presenter = new PagoServicios(this.mContext, Trans.PAGOSERVICIOS, para);
        }

        if (transType.equals(Trans.CONSULTAS)){
            para.setTransType(Trans.CONSULTAS);
            para.setNeedOnline(true);
            para.setNeedPrint(true);
            para.setNeedConfirmCard(false);
            para.setNeedPass(false);
            para.setNeedAmount(false);
            para.setEmvAll(true);
            presenter = new Consulta(this.mContext,Trans.CONSULTAS,para);
        }
        if (transType.equals(Trans.SETTLE) || transType.equals(Trans.SETTLE_AUTO)) {
            para.setTransType(mActivity.getResources().getString(R.string.settle));
            para.setNeedOnline(false);
            para.setNeedPrint(true);
            para.setNeedPass(false);
            para.setEmvAll(false);
            para.setForcePrint(transType.equals(Trans.SETTLE_AUTO));
            presenter = new Settle(this.mContext, Trans.SETTLE, para);
        }
        if (transType.equals(Trans.INIT_T)) {
            para.setTransType(Trans.INIT_T);
            para.setNeedOnline(true);
            para.setNeedPrint(false);
            para.setNeedPass(false);
            para.setEmvAll(true);
            para.setNeedAmount(false);
            para.setTypeInit(Trans.IT);
            presenter = new Initialization(this.mContext, Trans.INIT_T, para);
        }
        if (transType.equals(Trans.INIT_P)) {
            para.setTransType(Trans.INIT_P);
            para.setNeedOnline(true);
            para.setNeedPrint(false);
            para.setNeedPass(false);
            para.setEmvAll(true);
            para.setNeedAmount(false);
            para.setTypeInit(Trans.IP);
            presenter = new Initialization(this.mContext, Trans.INIT_P, para);
        }
         if (transType.equals(Trans.LOGIN)) {
            para.setTransType(Trans.LOGIN);
            para.setNeedOnline(false);
            para.setNeedPrint(false);
            para.setNeedPass(false);
            para.setEmvAll(false);
            para.setNeedAmount(false);
            presenter = new LoginSupervisor(this.mContext, Trans.LOGIN, para);
        }
        if (transType.equals(Trans.CERTIFICADO)) {
            para.setTransType(Trans.CERTIFICADO);
            para.setNeedOnline(false);
            para.setNeedPrint(false);
            para.setNeedPass(false);
            para.setEmvAll(false);
            para.setNeedAmount(false);
            presenter = new CertificateTransaction(this.mContext, Trans.CERTIFICADO, para);
        }
        if (transType.equals(Trans.ULT_OPERACIONES)){
            para.setTransType(Trans.ULT_OPERACIONES);
            para.setNeedOnline(false);
            para.setNeedPrint(true);
            para.setNeedPass(false);
            para.setEmvAll(false);
            para.setForcePrint(transType.equals(Trans.ULT_OPERACIONES));
            presenter = new LastOperations(this.mContext, Trans.ULT_OPERACIONES, para);
        }
        if (isInit) {
            new Thread() {
                @Override
                public void run() {
                    presenter.start();
                }
            }.start();
        } else {
            para.getTransUI().showError(transType, "001",PaySdkException.NOT_INIT);
        }
    }
}
