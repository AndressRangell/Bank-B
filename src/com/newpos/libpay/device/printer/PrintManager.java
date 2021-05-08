package com.newpos.libpay.device.printer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.android.newpos.pay.R;
import com.bcp.definesbcp.Definesbcp;
import com.bcp.document.ClassArguments;
import com.bcp.printer.DetailPrinter;
import com.bcp.rest.ultimasoperaciones.response.RspViewLastOperations;
import com.bcp.tools.Estadisticas;
import com.bcp.tools.UtilNetwork;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.presenter.TransUI;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.Trans;
import com.newpos.libpay.trans.translog.TransLogDataWs;
import com.newpos.libpay.trans.translog.TransLogLastSettle;
import com.newpos.libpay.trans.translog.TransLogWs;
import com.newpos.libpay.utils.ISOUtil;
import com.newpos.libpay.utils.PAYUtils;
import com.pos.device.config.DevConfig;
import com.pos.device.printer.PrintCanvas;
import com.pos.device.printer.PrintTask;
import com.pos.device.printer.Printer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import cn.desert.newpos.payui.UIUtils;
import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.agente;
import static com.android.newpos.pay.StartAppBCP.estadisticas;
import static com.android.newpos.pay.StartAppBCP.variables;


/**
 * Created by zhouqiang on 2017/3/14.
 *
 * @author zhouqiang
 * 打印管理类
 */
public class PrintManager {

    private static PrintManager mInstance;
    private static Context mContext;
    private static TransUI transUI;
    private static final int S_SMALL = 15;
    private static final int S_MEDIUM = 23;
    private static final int S_BIG = 29;
    private static final int MAX_CHAR_SMALL = 42;
    private static final int MAX_CHAR_MEDIUM = 28;//28
    private static final int MAX_CHAR_BIG = 22;
    private boolean boldOn = true;
    private Printer printer = null;
    private PrintTask printTask = null;
    protected PackageInfo packageInfo;

    private long granTotalEntry = 0;
    private long granTotalExit = 0;
    private long totalOperEntry = 0;
    private long totlOperExit = 0;

    private static final String MSG_EXCEPTION = "Exception";
    private static final String SPACEPLUS = ":    ";


    //variables para voucher BCP
    private static final String CANAL = "AGENTE BCP";

    private static final String DATE = "FECHA: ";
    private static final String TIME = " HORA: ";
    private static final String NUMOPE = "NO.OPE: ";
    private static final String NUMTAR = " NRO: ";
    private static final String VARCARD = "TARJETA: ";
    private static final String SPACE = " ";
    private static final String SPACENUMOP = "          ";
    private static final String TOTAL = "TOTAL: ";
    private static final String TYPECHANGE = "TIPO DE CAMBIO:";
    public static final String TOTALPAGAR = "TOTAL PAGADO: ";
    public static final String WITHCARD = "CON TARJETA";
    private String tyQuery;
    private TransLogDataWs dataWs;
    private static final int LONG10 = 10;
    private static final int LONG15 = 15;
    private static final int LONG20 = 20;

    public void setTyQuery(String tyQuery) {
        this.tyQuery = tyQuery;
    }

    private PrintManager() {
    }

    public static PrintManager getmInstance(Context c, TransUI tui) {
        mContext = c;
        transUI = tui;
        if (null == mInstance) {
            mInstance = new PrintManager();
        }
        return mInstance;
    }

    /**
     * print
     *
     * @param data   dataTrans
     * @param isCopy isCopy
     * @return return
     */
    public int print(final TransLogDataWs data,boolean isCopy, boolean duplicate) {
        Logger.logLine(Logger.LOG_GENERAL, "impresion " + duplicate);
        dataWs = data;
        int ret = -1;
        this.printTask = new PrintTask();
        this.printTask.setGray(150);
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION + e.toString());
        }

        if (TransLogWs.getInstance().getSize() > 0){
            printer = Printer.getInstance();
            if (printer == null) {
                ret = TcodeError.T_SEARCH_CARD_ERR;
            } else {

                switch (data.getNameTrans()) {
                    case Trans.RETIRO:
                        ret = printRetiro(data, false, isCopy);
                        break;
                    case Trans.DEPOSITO:
                        ret = printDeposito(data, false, isCopy);
                        break;
                    case Trans.SETTLE:
                        ret = printDetailReport();
                        break;
                    case Trans.CONSULTAS:
                        if(tyQuery.equals("Consulta Saldo")){
                            ret = printSaldos(data,false,isCopy);
                        }else
                            ret = printMovimientos(data,false,isCopy);
                        break;
                    case Trans.GIROS:
                        ret = printGiros(data,false,isCopy);
                        break;
                    case Trans.PAGOSERVICIOS:
                        ret = printPagoServicios(data,false,isCopy);
                        break;
                    default:
                        break;
                }
            }
        }
        return ret;
    }

    public int printLastOpe(RspViewLastOperations rspViewLastOperations, boolean tipOpertations) {
        int ret = -1;
        this.printTask = new PrintTask();
        this.printTask.setGray(150);

        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION + e.toString());
        }

        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();

        setTextPrint(CANAL, paint, true, canvas, S_BIG);

        nameAgent(paint, canvas);
        setTextPrint(setTextColumn(DATE+rspViewLastOperations.getTransactionDate()+TIME+rspViewLastOperations.getTransactionTime(), UIUtils.checkNull(agente.getMerchantId()), S_SMALL), paint, true, canvas, S_SMALL);
        setTextPrint("NRO.SOLICITUD: " + UIUtils.checkNull(rspViewLastOperations.getTransactionNumber()), paint, true, canvas, S_SMALL);

        println(paint, canvas);

        int length = 40;
        int offset = 0;
        String rspViewLast = null;

        if (tipOpertations){
            rspViewLast = rspViewLastOperations.getContentTheeLast();
            subStringLast(rspViewLast, length, offset, S_SMALL, paint,true,canvas);
        }else {
            rspViewLast = rspViewLastOperations.getContent();
            subStringLast(rspViewLast, length, offset, S_SMALL, paint,true,canvas);
        }

        println(paint, canvas);

        ret = printData(canvas,false);

        if (printer != null) {
            printer = null;
        }

        return ret;
    }

    private void subStringLast(String rspViewLast, int length, int offset, int size, Paint paint, boolean bold, PrintCanvas canvas) {
        String temp = "";
        for(int i =rspViewLast.length()-1 ; i>0 ; i = i-length){
            if(i > length){
                temp = rspViewLast.substring(offset,offset+length);
            }else {
                temp = rspViewLast.substring(offset);
            }
            offset+=length;
            setTextPrint(temp, paint, bold, canvas, size);
        }
    }


    private void nameAgent(Paint paint, PrintCanvas canvas){
        try {
            if (UIUtils.checkNull(agente.getAgentName()).length() < 22){
                setTextPrint(UIUtils.checkNull(agente.getAgentName()), paint, true, canvas, S_BIG);
            }else if (UIUtils.checkNull(agente.getAgentName()).length() < 28){
                setTextPrint(UIUtils.checkNull(agente.getAgentName()), paint, true, canvas, S_MEDIUM);
            }else {
                setTextPrint(UIUtils.checkNull(agente.getAgentName()), paint, true, canvas, S_SMALL);
            }
        } catch (Exception e){
            setTextPrint(UIUtils.checkNull("---"), paint, true, canvas, S_BIG);
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }
    }

    private int printDeposito(TransLogDataWs logData, boolean isRePrint, boolean isCopy) {
        Logger.debug("PrintManager>>start>>printDeposito>>");
        Logger.debug("" + isRePrint);

        this.printTask = new PrintTask();
        this.printTask.setGray(200);

        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();
        if (!isCopy) {
            setTextPrint(CANAL, paint, true, canvas, S_BIG);
        }
        nameAgent(paint, canvas);
        setTextPrint(setTextColumn(DATE+logData.getTransactionDate()+TIME+logData.getTransactionTime(), UIUtils.checkNull(agente.getMerchantId()), S_SMALL), paint, true, canvas, S_SMALL);
        setTextPrint(NUMOPE + UIUtils.checkNull(logData.getTransactionNumber()), paint, true, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(setCenterText(fillLine(UIUtils.checkNull(logData.getNameTrans()),S_SMALL), S_SMALL), paint, true, canvas, S_SMALL);
        if (!isCopy) {
            setTextPrint(setTextColumn(
                    mContext.getResources().getString(R.string.acta) +
                            SPACE +
                            UIUtils.checkNull(logData.getFamilyDescription()).toUpperCase() +
                            SPACE +
                            UIUtils.checkNull(logData.getCurrencySymbol()),
                    NUMTAR +
                            UIUtils.checkNull(logData.getAccountId())
                    , S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(mContext.getResources().getString(R.string.name) + UIUtils.checkNull(logData.getFullName()), paint, true, canvas, S_SMALL);
        }else {
            println(paint, canvas);
            println(paint, canvas);
        }

        setTextPrint(setTextColumn("MONTO RECIBIDO:  ", logData.getCurrencySymbol() + ISOUtil.padleft(formatAmount(logData.getAmount()), LONG10,' '), S_SMALL), paint, true, canvas, S_SMALL);

        if (!isCopy){
            separatorFld(paint, canvas, S_SMALL);
            separador(logData.getLeadDescription(),S_SMALL,paint,false,canvas);
        }
        println(paint, canvas);

        int ret = printData(canvas,true);

        if (printer != null) {
            printer = null;
        }

        return ret;
    }


    private int printRetiro(TransLogDataWs logData, boolean isRePrint, boolean isCopy) {
        Logger.debug("PrintManager>>start>>printRetiro>>");
        Logger.debug("" + isRePrint);

        this.printTask = new PrintTask();
        this.printTask.setGray(200);

        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();
        if (!isCopy) {
            setTextPrint(CANAL, paint, true, canvas, S_BIG);
        }

        nameAgent(paint, canvas);
        setTextPrint(setTextColumn(DATE+logData.getTransactionDate()+TIME+logData.getTransactionTime(), UIUtils.checkNull(agente.getMerchantId()), S_SMALL), paint, true, canvas, S_SMALL);
        setTextPrint(NUMOPE + UIUtils.checkNull(logData.getTransactionNumber()) + SPACE + (!isCopy ? VARCARD + (UIUtils.checkNull(logData.getCardId())) : ""), paint, true, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(setCenterText(fillLine(UIUtils.checkNull(logData.getNameTrans()),S_SMALL), S_SMALL), paint, true, canvas, S_SMALL);
        if (!isCopy) {
            setTextPrint(setTextColumn(
                    mContext.getResources().getString(R.string.decta) +
                            SPACE +
                            UIUtils.checkNull(logData.getFamilyDescription()).toUpperCase() +
                            SPACE +
                            UIUtils.checkNull(logData.getCurrencySymbol()),
                    NUMTAR +
                            UIUtils.checkNull(logData.getAccountId())
                    , S_SMALL), paint, true, canvas, S_SMALL);
        }else {
            println(paint, canvas);
            println(paint, canvas);
        }

        setTextPrint(setTextColumn("MONTO PAGADO: ", "S/" + ISOUtil.padleft(formatAmount(logData.getAmount()), LONG10,' '), S_SMALL), paint, true, canvas, S_SMALL);

        if (logData.getCurrencyCode().equals("USD") && !isCopy){
            setTextPrint(setTextColumn("TIPO DE CAMBIO:", UIUtils.checkNull(logData.getExchangeRate()), S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(setTextColumn("MONTO CARGADO:", logData.getCurrencySymbol() + ISOUtil.padleft(UIUtils.checkNull(logData.getAmountExchangeRate()), LONG10,' '), S_SMALL), paint, true, canvas, S_SMALL);
        }

        if (!isCopy){
            separatorFld(paint, canvas, S_SMALL);
            separador(logData.getLeadDescription(),S_SMALL,paint,false,canvas);
        }
        println(paint, canvas);

        int ret = printData(canvas,true);

        if (printer != null) {
            printer = null;
        }

        return ret;
    }

    private int printSaldos(TransLogDataWs logData, boolean isRePrint, boolean isCopy) {
        Logger.debug("PrintManager>>start>>printSalebcp>>");
        Logger.debug("" + isRePrint);

        this.printTask = new PrintTask();
        this.printTask.setGray(200);

        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();

        if (!isCopy) {
            setTextPrint(CANAL, paint, true, canvas, S_BIG);
        }
        nameAgent(paint, canvas);
        setTextPrint(DATE+logData.getTransactionDate()+TIME+logData.getTransactionTime() + " " + UIUtils.checkNull(agente.getMerchantId()), paint, true, canvas, S_SMALL);
        setTextPrint(NUMOPE + UIUtils.checkNull(logData.getTransactionNumber()) + SPACE + (!isCopy ? VARCARD + (UIUtils.checkNull(logData.getCardId())) : ""), paint, true, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(setCenterText(fillLine(mContext.getResources().getString(R.string.saldos),S_SMALL), S_SMALL), paint, true, canvas, S_SMALL);
        if (!isCopy) {
            setTextPrint(setTextColumn(
                    mContext.getResources().getString(R.string.cta) +
                            SPACE +
                            UIUtils.checkNull(logData.getFamilyDescription()).toUpperCase() +
                            SPACE +
                            UIUtils.checkNull(logData.getCurrencySymbol()),
                    NUMTAR +
                            UIUtils.checkNull(logData.getAccountId())
                    , S_SMALL), paint, true, canvas, S_SMALL);

            println(paint, canvas);

            setTextPrint(setTextColumn(mContext.getResources().getString(R.string.saldoCtb) , logData.getCurrencySymbol() + ISOUtil.padleft(logData.getCountableBalance(), LONG15,' '), S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(setTextColumn(mContext.getResources().getString(R.string.saldoDsp) , logData.getCurrencySymbol() +  ISOUtil.padleft(logData.getAvailableBalance(), LONG15,' '), S_SMALL), paint, true, canvas, S_SMALL);

            separatorFld(paint, canvas, S_SMALL);

            if(!isCopy){
                separador(logData.getLeadDescription(),S_SMALL,paint,false,canvas);
            }
        }

        println(paint, canvas);

        int ret = printData(canvas,true);

        if (printer != null) {
            printer = null;
        }

        return ret;
    }

    private int printMovimientos(TransLogDataWs logData, boolean isRePrint, boolean isCopy) {
        Logger.debug("PrintManager>>start>>printSalebcp>>");
        Logger.debug("" + isRePrint);

        this.printTask = new PrintTask();
        this.printTask.setGray(200);

        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();

        if (!isCopy) {
            setTextPrint(CANAL, paint, true, canvas, S_BIG);
        }
        nameAgent(paint, canvas);
        setTextPrint(DATE+logData.getTransactionDate()+TIME+logData.getTransactionTime() + " " + UIUtils.checkNull(agente.getMerchantId()), paint, true, canvas, S_SMALL);
        setTextPrint(NUMOPE + UIUtils.checkNull(logData.getTransactionNumber()) + SPACE + (!isCopy ? VARCARD + (UIUtils.checkNull(logData.getCardId())) : ""), paint, true, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(setCenterText(fillLine(mContext.getResources().getString(R.string.movimientos),S_SMALL), S_SMALL), paint, true, canvas, S_SMALL);
        if (!isCopy) {
            setTextPrint(setTextColumn(
                    mContext.getResources().getString(R.string.cta) +
                    SPACE +
                    UIUtils.checkNull(logData.getFamilyDescription()).toUpperCase() +
                    SPACE +
                    UIUtils.checkNull(logData.getCurrencySymbol()),
                    NUMTAR +
                    UIUtils.checkNull(logData.getAccountId())
                    , S_SMALL), paint, true, canvas, S_SMALL);

            println(paint, canvas);

            if (logData.getProductsTransactions() != null){
                for (int i = 0; i < logData.getProductsTransactions().length; i++) {
                    setTextPrint(setTextColumn(logData.getProductsTransactions()[i].getData() + " " + logData.getProductsTransactions()[i].getReference(), logData.getProductsTransactions()[i].getAmount(), S_SMALL), paint, true, canvas, S_SMALL);
                }
            }

            println(paint, canvas);

            setTextPrint(setTextColumn(mContext.getResources().getString(R.string.saldoCtb) , logData.getCurrencySymbol() + ISOUtil.padleft(logData.getAvailableBalance(), LONG15,' '), S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(setTextColumn(mContext.getResources().getString(R.string.saldoDsp) , logData.getCurrencySymbol() + ISOUtil.padleft(logData.getCountableBalance(), LONG15,' '), S_SMALL), paint, true, canvas, S_SMALL);

            separatorFld(paint, canvas, S_SMALL);
            if(!isCopy){
                separador(logData.getLeadDescription(),S_SMALL,paint,false,canvas);
            }
        }

        println(paint, canvas);

        int ret = printData(canvas,true);

        if (printer != null) {
            printer = null;
        }

        return ret;
    }

    private int printGiros(TransLogDataWs logData, boolean isRePrint, boolean isCopy) {
        Logger.debug("PrintManager>>start>>printRetiro>>");
        Logger.debug("" + isRePrint);

        this.printTask = new PrintTask();
        this.printTask.setGray(200);

        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();
        if (!isCopy) {
            setTextPrint(CANAL, paint, true, canvas, S_BIG);
        }

        nameAgent(paint, canvas);
        setTextPrint(setTextColumn(DATE+logData.getTransactionDate()+TIME+logData.getTransactionTime(), UIUtils.checkNull(agente.getMerchantId()), S_SMALL), paint, true, canvas, S_SMALL);
        setTextPrint(NUMOPE + UIUtils.checkNull(logData.getTransactionNumber()) + SPACE + ((!isCopy && logData.getCardId() != null) ? VARCARD + (UIUtils.checkNull(logData.getCardId())) : ""), paint, true, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(setCenterText(fillLine(UIUtils.checkNull((logData.getMoneyOut().equals("1") ? "EMISION " : "COBRO " ) + "DE GIRO NACIONAL"),S_SMALL), S_SMALL), paint, true, canvas, S_SMALL);

        if (!isCopy){
            setTextPrint("NRO REF : " + UIUtils.checkNull(logData.getReference()), paint, true, canvas, S_SMALL);

            println(paint, canvas);

            setTextPrint("A FAVOR DE ", paint, true, canvas, S_SMALL);
            setTextPrint(UIUtils.checkNull(logData.getNameBenficiary()), paint, true, canvas, S_SMALL);
            setTextPrint(logData.getDocTypeBeneficiary() + " - " + UIUtils.checkNull(logData.getDocNumBeneficiary()), paint, true, canvas, S_SMALL);

            println(paint, canvas);

            setTextPrint(setTextColumn("POR: ", logData.getCurrencySymbol() + ISOUtil.padleft(formatAmount(logData.getAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);

            println(paint, canvas);

            if ((logData.getDocNumSender() != null && !logData.getDocNumSender().equals("")) && (logData.getDocNumRemitter() != null && !logData.getDocNumRemitter().equals(""))){
                if (!logData.getDocNumSender().equals(logData.getDocNumRemitter())){
                    setTextPrint("POR CUENTA DE", paint, true, canvas, S_SMALL);
                    setTextPrint(UIUtils.checkNull(logData.getNameRemitter()), paint, true, canvas, S_SMALL);
                    setTextPrint(logData.getDocTypeRemitter() + " - " + UIUtils.checkNull(logData.getDocNumRemitter()), paint, true, canvas, S_SMALL);

                    println(paint, canvas);

                    setTextPrint("A SOLICITUD DE", paint, true, canvas, S_SMALL);
                    setTextPrint(UIUtils.checkNull(logData.getNameSender()), paint, true, canvas, S_SMALL);
                    setTextPrint(logData.getDocTypeSender() + " - " + UIUtils.checkNull(logData.getDocNumSender()), paint, true, canvas, S_SMALL);
                }else {
                    setTextPrint("POR CUENTA Y SOLICITUD DE", paint, true, canvas, S_SMALL);
                    setTextPrint(UIUtils.checkNull(logData.getNameRemitter()), paint, true, canvas, S_SMALL);
                    setTextPrint(logData.getDocTypeRemitter() + " - " + UIUtils.checkNull(logData.getDocNumRemitter()), paint, true, canvas, S_SMALL);
                }
            }else {
                setTextPrint("POR CUENTA " + (logData.getMoneyOut().equals("1") ? "Y SOLICITUD DE" : "DE"), paint, true, canvas, S_SMALL);
                setTextPrint(UIUtils.checkNull(logData.getNameRemitter()), paint, true, canvas, S_SMALL);
                setTextPrint(logData.getDocTypeRemitter() + " - " + UIUtils.checkNull(logData.getDocNumRemitter()), paint, true, canvas, S_SMALL);
            }

            println(paint, canvas);

            setTextPrint(UIUtils.checkNull(logData.getPaymentMethod()) + SPACE + (!UIUtils.checkNull(logData.getFamilyDescription()).equals("---") ? logData.getFamilyDescription().toUpperCase() + SPACE + logData.getCurrencyDescription().toUpperCase() + SPACE + logData.getAccountId() : "") , paint, true, canvas, S_SMALL);

            println(paint, canvas);

            setTextPrint(setTextColumn("MONTO GIRO: ",logData.getCurrencySymbol() + ISOUtil.padleft(UIUtils.checkNull(logData.getAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(setTextColumn("COMISION GIRO: ", logData.getCommissionCurrencySymbol() + ISOUtil.padleft(UIUtils.checkNull(logData.getCommissionAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
        }

        if (!isCopy && logData.getExchangeRate() != null){
            setTextPrint(setTextColumn(TOTAL, logData.getSymbolExchangeRate() + ISOUtil.padleft(UIUtils.checkNull(logData.getAmountExchangeRate()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(setTextColumn(TYPECHANGE, UIUtils.checkNull(ISOUtil.padleft(UIUtils.checkNull(logData.getExchangeRate()), LONG10,'*')), S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(setTextColumn(TOTALPAGAR, logData.getTotalAmountCurrency() + ISOUtil.padleft(UIUtils.checkNull(logData.getTotalAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
        }else {
            if (isCopy){
                if (logData.getCardId() != null)
                    setTextPrint(WITHCARD, paint, true, canvas, S_SMALL);
                else
                    setTextPrint(setTextColumn(TOTAL, logData.getTotalAmountCurrency() + ISOUtil.padleft(UIUtils.checkNull(logData.getTotalAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
            }else {
                setTextPrint(setTextColumn(TOTAL, logData.getTotalAmountCurrency() + ISOUtil.padleft(UIUtils.checkNull(logData.getTotalAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
            }
        }

        if (!isCopy && logData.getLeadDescription() != null && !logData.getLeadDescription().equals("")){
            separatorFld(paint, canvas, S_SMALL);
            separador(logData.getLeadDescription(),S_SMALL,paint,false,canvas);
        }

        if (!isCopy)
            separatorFld(paint, canvas, S_SMALL);

        int ret = printData(canvas,true);

        if (printer != null) {
            printer = null;
        }

        return ret;
    }

    private int printPagoServicios(TransLogDataWs logData, boolean isRePrint, boolean isCopy) {
        Logger.debug("PrintManager>>start>>printPagoDeServicios>>");
        Logger.debug("" + isRePrint);

        this.printTask = new PrintTask();
        this.printTask.setGray(200);

        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();
        if (!isCopy) {
            setTextPrint(CANAL, paint, true, canvas, S_BIG);
        }
        nameAgent(paint, canvas);
        setTextPrint(setTextColumn(DATE+logData.getTransactionDate()+TIME+logData.getTransactionTime(), UIUtils.checkNull(agente.getMerchantId()), S_SMALL), paint, true, canvas, S_SMALL);
        setTextPrint(NUMOPE + UIUtils.checkNull(logData.getTransactionNumber()) + SPACE + ((logData.getCardId() != null) ? VARCARD + (UIUtils.checkNull(logData.getCardId())) : ""), paint, true, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(setCenterText(fillLine(UIUtils.checkNull(logData.getNameTrans()).replace("_"," DE "),S_SMALL), S_SMALL), paint, true, canvas, S_SMALL);

        if (logData.getCategory() != null){
            setTextPrint(setTextColumn("GIRO/RUBRO:",UIUtils.checkNull(logData.getCategory()),S_SMALL),paint,true,canvas,S_SMALL);
            setTextPrint(setTextColumn("EMPRESA:",UIUtils.checkNull(logData.getCompany()),S_SMALL),paint,true,canvas,S_SMALL);

            if (!isCopy) {
                setTextPrint(setTextColumn("CTA. A ABONAR:",UIUtils.checkNull(logData.getPaymentAccount()),S_SMALL),paint,true,canvas,S_SMALL);
                setTextPrint(setTextColumn("COD. ID. USUARIO:",UIUtils.checkNull(logData.getDepositCode()),S_SMALL),paint,true,canvas,S_SMALL);
                if (logData.getDepositName() != null){
                    setTextPrint(mContext.getResources().getString(R.string.name) + UIUtils.checkNull(logData.getDepositName()), paint, true, canvas, S_SMALL);
                    println(paint, canvas);
                }

                if (logData.getPaymentMethod() != null && logData.getCardId() != null)
                    setTextPrint("DE CTA. " + UIUtils.checkNull(logData.getFamilyDescription()) + UIUtils.checkNull(logData.getCurrencySymbol()) + UIUtils.checkNull(logData.getAccountId()),paint,true,canvas,S_SMALL);
                else
                    setTextPrint(UIUtils.checkNull(logData.getPaymentMethod()),paint,true,canvas,S_SMALL);

                println(paint, canvas);

                setTextPrint("DESCRIPCION",paint,true,canvas,S_SMALL);
                separador(UIUtils.checkNull(logData.getDescription()),S_SMALL,paint,false,canvas);

                if (logData.getImporName() != null)
                    setTextPrint(logData.getImporName(),paint,true,canvas,S_SMALL);

                println(paint, canvas);
                println(paint, canvas);

                if (logData.getDueDate() != null){
                    setTextPrint("FECHA VENCIM:    " + UIUtils.checkNull(logData.getDueDate()), paint, true, canvas, S_SMALL);
                    setTextPrint(setTextColumn("IMPORTE CUOTA:", UIUtils.checkNull(logData.getAmountCurrencySymbol()) + ISOUtil.padleft(UIUtils.checkNull(logData.getAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
                }else if (logData.getImporName() != null)
                    setTextPrint(setTextColumn("IMPORTE CUOTA:", UIUtils.checkNull(logData.getAmountCurrencySymbol()) + ISOUtil.padleft(UIUtils.checkNull(logData.getAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
                else
                    setTextPrint(setTextColumn("IMPORTE PAGO:", UIUtils.checkNull(logData.getAmountCurrencySymbol()) + ISOUtil.padleft(UIUtils.checkNull(logData.getAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);

                setTextPrint(setTextColumn("CARGO FIJO:", UIUtils.checkNull(logData.getFixedChargeCurrencySymbol()) + ISOUtil.padleft(UIUtils.checkNull(logData.getFixedChargeAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
                setTextPrint(setTextColumn("MORA:", UIUtils.checkNull(logData.getDelayCurrencySymbol()) + ISOUtil.padleft(UIUtils.checkNull(logData.getDelayAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);

                println(paint, canvas);

                setTextPrint(setTextColumn("TOTAL " + (logData.getDueDate() != null || logData.getImporName() != null ? "CUOTA:" : "DEUDA:"), UIUtils.checkNull(logData.getTotalDebtCurrencySymbol()) + ISOUtil.padleft(UIUtils.checkNull(logData.getTotalDebtAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
            }
        }else {
            setTextPrint(UIUtils.checkNull(logData.getDescription()),paint,true,canvas,S_SMALL);
            setTextPrint("NRO. DE TELF./CLIENTE" + UIUtils.checkNull(logData.getDepositCode()),paint,true,canvas,S_SMALL);
            setTextPrint(mContext.getResources().getString(R.string.name) + UIUtils.checkNull(logData.getDepositName()), paint, true, canvas, S_SMALL);
            if (logData.getPaymentMethod() != null && logData.getCardId() != null)
                setTextPrint("RETIRO CTA. " + UIUtils.checkNull(logData.getFamilyDescription()) + UIUtils.checkNull(logData.getCurrencySymbol()) + UIUtils.checkNull(logData.getAccountId()),paint,true,canvas,S_SMALL);
            else
                setTextPrint(UIUtils.checkNull(logData.getPaymentMethod()),paint,true,canvas,S_SMALL);

            if (logData.getListsDebts() != null){
                setTextPrint(setTextColumn("FECHA      CONSUMO", "IMP      TOTAL", S_SMALL), paint, true, canvas, S_SMALL);
                for (int i = 0; i < logData.getListsDebts().length; i++) {
                    setTextPrint(setTextColumn(logData.getListsDebts()[i].getDueDate() + " " + logData.getListsDebts()[i].getConsumption(), logData.getListsDebts()[i].getImportName() + " " +logData.getListsDebts()[i].getAmount(), S_SMALL), paint, true, canvas, S_SMALL);
                }
            }
        }


        if (!isCopy)
            setTextPrint(setTextColumn("COMISION:", UIUtils.checkNull(logData.getCommissionCurrencySymbol()) + ISOUtil.padleft(UIUtils.checkNull(logData.getCommissionAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);

        if (isCopy){
            if (logData.getCardId() != null)
                setTextPrint(WITHCARD, paint, true, canvas, S_SMALL);
            else
                setTextPrint(setTextColumn("TOTAL A PAGAR:", UIUtils.checkNull(logData.getTotalAmountCurrency()) + ISOUtil.padleft(UIUtils.checkNull(logData.getTotalAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
        }else
            setTextPrint(setTextColumn("TOTAL A PAGAR:", UIUtils.checkNull(logData.getTotalAmountCurrency()) + ISOUtil.padleft(UIUtils.checkNull(logData.getTotalAmount()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);

        if (!isCopy && logData.getExchangeRate() != null){
            setTextPrint(setTextColumn(TYPECHANGE, UIUtils.checkNull(ISOUtil.padleft(UIUtils.checkNull(logData.getExchangeRate()), LONG10,'*')), S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(setTextColumn("TOTAL AL CAMBIO:", UIUtils.checkNull(logData.getSymbolExchangeRate()) + ISOUtil.padleft(UIUtils.checkNull(logData.getAmountExchangeRate()), LONG15,'*'), S_SMALL), paint, true, canvas, S_SMALL);
        }

        if (logData.getCategory() != null && !isCopy){
            separatorFld(paint, canvas, S_SMALL);
            separador(logData.getLeadDescription(),S_SMALL,paint,false,canvas);
        }

        println(paint, canvas);

        int ret = printData(canvas,true);

        if (printer != null) {
            printer = null;
        }

        return ret;
    }

    private String fillLine(String tittle, int length){
        StringBuilder data = new StringBuilder();
        int len;

        switch (length){
            case S_BIG:
                len = MAX_CHAR_BIG;
                break;
            case S_MEDIUM:
                len = MAX_CHAR_MEDIUM;
                break;
            case S_SMALL:
                len = MAX_CHAR_SMALL;
                break;
            default:
                len = 0;
                break;
        }

        len = len - tittle.length();

        if (len > 0){
            data.append(ISOUtil.padleft("",len/2,'-'));
            data.append(tittle);
            data.append(ISOUtil.padright("",len/2,'-'));
        }

        return data.toString();
    }

    public int printParamInit() {

        this.printTask = new PrintTask();
        this.printTask.setGray(150);
        PrintCanvas canvas = new PrintCanvas();
        Paint paint = new Paint();
        TransLogLastSettle.getInstance(true).getData();

        paint.setTextSize(20);

        setTextPrint(setCenterText("REPORTE DE INICIALIZACION", S_MEDIUM), paint, boldOn, canvas, S_MEDIUM);

        setTextPrint(setCenterText(agente.getAgentName(), S_MEDIUM), paint, boldOn, canvas, S_MEDIUM);

        println(paint, canvas);

        String auxText;
        setTextPrint(checkNumCharacters("ID Comercio :  " + agente.getMerchantId(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("ID Terminal :  " + TMConfig.getInstance().getTermID(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Tipo de POS :  " + DevConfig.getMachine(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Version     :  " + variables.getVersion(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Hardware ID :  " + DevConfig.getSN(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("APP Name    :  bcp", S_SMALL), paint, boldOn, canvas, S_SMALL);

        println(paint, canvas);
        setTextPrint(checkNumCharacters("Consecutivo Recibo: " + TMConfig.getInstance().getTraceNo(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Lote Vigente      : " + ISOUtil.zeropad(TMConfig.getInstance().getBatchNo(), 6), S_SMALL), paint, boldOn, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(checkNumCharacters("Tipo de Conex. : IP", S_SMALL), paint, boldOn, canvas, S_SMALL);

        setTextPrint(checkNumCharacters("APN            : ", S_SMALL), paint, boldOn, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(checkNumCharacters("Ip Lan      : " + UIUtils.checkNull(UtilNetwork.getIPAddress(true)), S_SMALL), paint, boldOn, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(setCenterText("PARAMETROS INICIALIZACION", S_MEDIUM), paint, boldOn, canvas, S_MEDIUM);

        setTextPrint(checkNumCharacters("Tipo de comunicacion : IP", S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("NII                  : " + TMConfig.getInstance().getNii(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Direccion IP         : " + TMConfig.getInstance().getIp(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Puerto               : " + TMConfig.getInstance().getPort(), S_SMALL), paint, boldOn, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(setCenterText("OPCIONES PARA EL TERMINAL", S_MEDIUM), paint, boldOn, canvas, S_MEDIUM);

        setTextPrint(checkNumCharacters("RECIBO", S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Linea 2 : " + agente.getAgentName(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Linea 3 : " + agente.getAddress(), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Linea 4 : " + agente.getPhone(), S_SMALL), paint, boldOn, canvas, S_SMALL);

        println(paint, canvas);

        setTextPrint(checkNumCharacters("Fecha Ult. Act.      : " + UIUtils.checkNull(getFechaCierre("fechaUltAct")), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Dias Para Cierre     : " + UIUtils.checkNull(agente.getSettleDays()), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Fecha Sig. Cierre.   : " + UIUtils.checkNull(getFechaCierre("fechaSigCierre")), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Fecha Ult. Cierre.   : " + UIUtils.checkNull(getFechaCierre("fechaUltimoCierre")), S_SMALL), paint, boldOn, canvas, S_SMALL);
        setTextPrint(checkNumCharacters("Fecha Sig. Echo Test : " + UIUtils.checkNull(getFechaCierre("fechaSigEchoTest")), S_SMALL), paint, boldOn, canvas, S_SMALL);

        println(paint, canvas);
        println(paint, canvas);

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        BitMatrix bitMatrix = null;

        try {
            bitMatrix = multiFormatWriter.encode("https://www.viabcp.com", BarcodeFormat.QR_CODE, 275, 275);
        } catch (WriterException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION + e.toString());
        }

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

        canvas.setX(50);
        canvas.drawBitmap(bitmap, paint);
        canvas.setX(0);

        println(paint, canvas);

        setTextPrint(setCenterText("FECHA Y HORA REPORTE", S_SMALL), paint, boldOn, canvas, S_SMALL);

        auxText = PAYUtils.getDay() + "/" + PAYUtils.getMonth() + "/" + (PAYUtils.getYear()) + " " + PAYUtils.formatoHora(PAYUtils.getLocalTime(PAYUtils.TIMEWITHOUTPOINT));
        setTextPrint(setCenterText(auxText, S_SMALL), paint, boldOn, canvas, S_SMALL);

        println(paint, canvas);

        int ret = printData(canvas,true);

        if (printer != null) {
            printer = null;
        }

        return ret;
    }

    public int printDetailReport(){
        Logger.debug("PrintManager>>start>>printReport>>");

        int ret = TcodeError.T_ERR_PRINT_DATA;

        try {
            this.printTask = new PrintTask();
            this.printTask.setGray(200);

            PrintCanvas canvas = new PrintCanvas();
            Paint paint = new Paint();

            List<TransLogDataWs> list = TransLogWs.getInstance().getData();

            setTextPrint(CANAL, paint, true, canvas, S_BIG);
            nameAgent(paint, canvas);
            setTextPrint(setTextColumn(DATE + PAYUtils.getLocalDate("dd/MM/yy")+"  "+ TIME + PAYUtils.getLocalTime(PAYUtils.TIMEWITHPOINT), UIUtils.checkNull(agente.getMerchantId()), S_SMALL), paint, true, canvas, S_SMALL);
            setTextPrint(mContext.getResources().getString(R.string.lastSettle) + SPACE + UIUtils.checkNull(getFechaCierre("fechaUltimoCierre")), paint, true, canvas, S_SMALL);
            setTextPrint(mContext.getResources().getString(R.string.settleOper), paint, true, canvas, S_SMALL);
            setTextPrint(mContext.getResources().getString(R.string.dateFirstOper) + SPACE + list.get(0).getTransactionDate() + " " + list.get(0).getTransactionTime(), paint, true, canvas, S_SMALL);
            setTextPrint(mContext.getResources().getString(R.string.dateLastOper) + SPACE + SPACE + list.get(list.size() - 1).getTransactionDate() + " " + list.get(list.size() - 1).getTransactionTime(), paint, true, canvas, S_SMALL);
            println(paint, canvas);
            setTextPrint(mContext.getResources().getString(R.string.numOper) + SPACENUMOP + list.size(), paint, true, canvas, S_SMALL);
            println(paint, canvas);
            separatorFld(paint, canvas, S_SMALL);
            setTextPrint(setCenterText(mContext.getResources().getString(R.string.msg_ReporCierrreTotal),S_SMALL), paint, true, canvas, S_SMALL);
            println(paint, canvas);
            printTrans(list,paint,canvas);
            println(paint, canvas);
            setTextPrint(setCenterText(mContext.getResources().getString(R.string.endReportDetail),S_SMALL), paint, true, canvas, S_SMALL);

            ret = printData(canvas,true);

            if (printer != null) {
                printer = null;
            }

            if (ret == 0){
                TransLogWs.getInstance().clearAll();
                clearAmount();
            }
        }catch (Exception e){
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
        }

        if (polarisUtil.getCallbackIsPrinterSettle()!=null)
            polarisUtil.getCallbackIsPrinterSettle().getOkPrintSeatle(ret);

        if (polarisUtil.getCallBackSeatle() != null)
            polarisUtil.getCallBackSeatle().getRspSeatleReport(ret);

        return ret;
    }

    private void clearAmount(){
        granTotalEntry = 0;
        granTotalExit = 0;
        totalOperEntry = 0;
        totlOperExit = 0;
    }

    private void printTrans(List<TransLogDataWs> data, Paint paint, PrintCanvas canvas) {
        List<DetailPrinter> tempDeposito = new ArrayList<>();
        List<DetailPrinter> tempRetiro = new ArrayList<>();
        List<DetailPrinter> tempGirosEmision = new ArrayList<>();
        List<DetailPrinter> tempGirosCobros = new ArrayList<>();
        List<DetailPrinter> tempGirosEmisionCard = new ArrayList<>();
        List<DetailPrinter> tempGirosCobrosCard = new ArrayList<>();
        List<DetailPrinter> tempPagoServicios = new ArrayList<>();
        List<DetailPrinter> tempPagoServiciosCard = new ArrayList<>();
        int saldos = 0;
        int movimientos = 0;

        for (TransLogDataWs transaction : data) {
            switch (UIUtils.checkNull(transaction.getNameTrans())) {
                case Trans.DEPOSITO:
                    fillList(transaction, tempDeposito);
                    break;
                case Trans.RETIRO:
                    fillList(transaction, tempRetiro);
                    break;
                case Trans.GIROS:
                    if (transaction.getEntryMode().equals("5"))
                        fillList(transaction, UIUtils.checkNull(transaction.getMoneyOut()).equals("1") ? tempGirosEmisionCard : tempGirosCobrosCard);
                    else
                        fillList(transaction, UIUtils.checkNull(transaction.getMoneyOut()).equals("1") ? tempGirosEmision : tempGirosCobros);
                    break;
                case Trans.CONSULTAS:
                    if (UIUtils.checkNull(transaction.getTypeQuery()).equals(Definesbcp.SALDO)){
                        saldos++;
                    }else if (UIUtils.checkNull(transaction.getTypeQuery()).equals(Definesbcp.MOVIMIENTOS)){
                        movimientos++;
                    }
                    break;
                case Trans.PAGOSERVICIOS:
                    fillList(transaction, transaction.getEntryMode().equals("5") ? tempPagoServiciosCard : tempPagoServicios );
                    break;
                default:
                    break;
            }
        }

        printTittleLine(paint,canvas,mContext.getResources().getString(R.string.operEfectivo),true);
        println(paint, canvas);
        printTittleLine(paint, canvas, mContext.getResources().getString(R.string.tittleCargCta), true);
        println(paint, canvas);
        ///BLOQUE PARA LA IMPRESION DE LA DATA DE INGRESO EFECTIVO
        printDetail(paint, canvas, tempDeposito,false);
        printDetail(paint, canvas, tempPagoServicios,false);
        printDetail(paint, canvas, tempGirosEmision,false);

        separatorFld(paint, canvas, S_SMALL);
        setTextPrint(setTextColumn(mContext.getResources().getString(R.string.totIngEfec) ," S/"+ SPACE + ISOUtil.padleft(UIUtils.formatMiles(String.valueOf(granTotalEntry)), LONG10,' '),S_SMALL), paint, true, canvas, S_SMALL);
        setTextPrint(setTextColumn(mContext.getResources().getString(R.string.totOperIn), SPACE + totalOperEntry,S_SMALL), paint, true, canvas, S_SMALL);
        separatorFld(paint, canvas, S_SMALL);
        println(paint, canvas);
        ///FIN DEL BLOQUE DE INGRESO EFECTIVO

        printTittleLine(paint, canvas, mContext.getResources().getString(R.string.tittleAbnCta), true);
        println(paint, canvas);

        ///BLOQUE PARA LA IMPRESION DE LA DATA DE SALIDA EFECTIVO
        printDetail(paint, canvas, tempRetiro,false);
        printDetail(paint, canvas, tempGirosCobros,false);

        separatorFld(paint, canvas, S_SMALL);
        setTextPrint(setTextColumn(mContext.getResources().getString(R.string.totOutgEfec)," S/" + SPACE + ISOUtil.padleft(UIUtils.formatMiles(String.valueOf(granTotalExit)), LONG10,' '),S_SMALL), paint, true, canvas, S_SMALL);
        setTextPrint(setTextColumn(mContext.getResources().getString(R.string.totOperOut) ,SPACE + totlOperExit,S_SMALL), paint, true, canvas, S_SMALL);
        separatorFld(paint, canvas, S_SMALL);
        println(paint, canvas);

        ///FIN DEL BLOQUE DE SALIDA EFECTIVO

        if (!tempGirosEmisionCard.isEmpty() || !tempGirosCobrosCard.isEmpty() || !tempPagoServiciosCard.isEmpty()){
            printTittleLine(paint, canvas, mContext.getResources().getString(R.string.operWithCard), true);
            println(paint, canvas);
            ///BLOQUE PARA LA IMPRESION DE LA DATA DE OPERACIONES CON TARJETA
            printDetail(paint, canvas, tempPagoServiciosCard,true);
            printDetail(paint, canvas, tempGirosEmisionCard,true);
            printDetail(paint, canvas, tempGirosCobrosCard,true);

            ///FIN DEL BLOQUE DE OPERACIONES CON TARJETA
        }
        printTittleLine(paint, canvas, mContext.getResources().getString(R.string.operNoMoney), true);
        println(paint, canvas);

        ///BLOQUE PARA LA IMPRESION DE LA DATA DE OPERACIONES NO MONETARIAS

        fillNoMoney(paint,canvas,mContext.getResources().getString(R.string.saldos),saldos);
        fillNoMoney(paint,canvas,mContext.getResources().getString(R.string.movimientos),movimientos);


        ///FIN DEL BLOQUE DE OPERACIONES NO MONETARIAS
    }

    private void fillNoMoney(Paint paint, PrintCanvas canvas,String msg, int cant){
        setTextPrint(msg, paint, true, canvas, S_SMALL);
        setTextPrint(setTextColumn(ISOUtil.padleft(mContext.getResources().getString(R.string.totOper), LONG20,' '),String.valueOf(cant),S_SMALL),paint,true,canvas,S_SMALL);
    }

    private void fillList(TransLogDataWs transLogDataWs, List<DetailPrinter> detail){

        DetailPrinter detailPrinter = new DetailPrinter();
        detailPrinter.setDate(UIUtils.checkNull(transLogDataWs.getTransactionDate()));
        detailPrinter.setNoOpe(UIUtils.checkNull(transLogDataWs.getTransactionNumber()));
        detailPrinter.setHourOpe(UIUtils.checkNull(transLogDataWs.getTransactionTime()));
        detailPrinter.setAmount(UIUtils.checkNull(transLogDataWs.getAmount()));
        detailPrinter.setSymbol(UIUtils.checkNull(transLogDataWs.getCurrencySymbol()));
        detailPrinter.setEntryOrExit(UIUtils.checkNull(transLogDataWs.getMoneyOut()));
        detailPrinter.setTransEname(UIUtils.checkNull(transLogDataWs.getNameTrans()));
        detailPrinter.setInputMode(UIUtils.checkNull(transLogDataWs.getEntryMode()));
        detailPrinter.setSymbolExchange(UIUtils.checkNull(transLogDataWs.getTotalAmountCurrency()));
        detailPrinter.setAmountExchange(UIUtils.checkNull(transLogDataWs.getTotalAmount()));

        detail.add(detailPrinter);
    }

    private void printDetail(Paint paint, PrintCanvas canvas,List<DetailPrinter> detailPrinters, boolean printCard){

        if (detailPrinters.isEmpty())
            return;

        int totalAmount = 0;
        int totalOperWithCard = 0;

        setTextPrint(printNameTransSettle(UIUtils.checkNull(detailPrinters.get(0).getTransEname()),detailPrinters.get(0).getEntryOrExit(),printCard), paint, true, canvas, S_SMALL);

        for (DetailPrinter detail : detailPrinters){
            if (printCard){
                if (UIUtils.checkNull(detail.getInputMode()).equals("5")){
                    setTextPrint(setTextColumn(detail.getDate() + SPACE +  detail.getHourOpe()+ SPACE + detail.getNoOpe(), detail.getSymbolExchange() + SPACE + ISOUtil.padleft(UIUtils.checkNull(detail.getAmountExchange()), LONG10,' '), S_SMALL),paint,true,canvas,S_SMALL);
                    totalAmount += Integer.parseInt(UIUtils.checkNull(detail.getAmountExchange()).replaceAll("[,.]",""));
                    totalOperWithCard++;
                }
            }else {
                String symbolTrans;
                String amountTrans;
                if (detail.getTransEname().equals(Trans.GIROS) || detail.getTransEname().equals(Trans.PAGOSERVICIOS)){
                    symbolTrans = detail.getSymbolExchange();
                    amountTrans = detail.getAmountExchange();
                }else {
                    symbolTrans = detail.getSymbol();
                    amountTrans = detail.getAmount();
                }
                setTextPrint(setTextColumn(detail.getDate() + SPACE +  detail.getHourOpe()+ SPACE + detail.getNoOpe(), symbolTrans + SPACE + ISOUtil.padleft(UIUtils.checkNull(amountTrans), LONG10,' '), S_SMALL),paint,true,canvas,S_SMALL);
                totalAmount += Integer.parseInt(UIUtils.checkNull(amountTrans).replaceAll("[,.]",""));
            }
        }

        separatorFld(paint, canvas, S_SMALL);
        if (!printCard)
            setTextPrint(setTextColumn(ISOUtil.padleft(mContext.getResources().getString(R.string.totSoles), LONG20,' '),"S/" + SPACE +  ISOUtil.padleft(UIUtils.formatMiles(String.valueOf(totalAmount)), LONG10,' '),S_SMALL),paint,true,canvas,S_SMALL);
        setTextPrint(setTextColumn(ISOUtil.padleft(mContext.getResources().getString(R.string.totOper), LONG20,' '),String.valueOf(printCard ? totalOperWithCard : detailPrinters.size()),S_SMALL),paint,true,canvas,S_SMALL);

        if (UIUtils.checkNull(detailPrinters.get(0).getEntryOrExit()).equals(Definesbcp.ENTRYMONEY)){
            granTotalEntry += totalAmount;
            totalOperEntry += detailPrinters.size();
        }else if (UIUtils.checkNull(detailPrinters.get(0).getEntryOrExit()).equals(Definesbcp.EXITMONEY)){
            granTotalExit += totalAmount;
            totlOperExit += detailPrinters.size();
        }
        println(paint, canvas);
    }

    private String printNameTransSettle(String name, String moneyOut, boolean withCard){
        String ret;
        switch (name){
            case Trans.RETIRO:
                ret = "RETIROS";
                break;
            case Trans.DEPOSITO:
                ret = "DEPÓSITOS";
                break;
            case Trans.GIROS:
                if (withCard)
                    ret = mContext.getResources().getString(UIUtils.checkNull(moneyOut).equals("1") ? R.string.girosE : R.string.girosC);
                else
                    ret = mContext.getResources().getString(UIUtils.checkNull(moneyOut).equals("1") ? R.string.emGiros : R.string.coGiros);
                break;
            case Trans.PAGOSERVICIOS:
                ret = "PAGO DE SERVICIOS";
                break;
            default:
                ret = name;
                break;
        }

        return ret;
    }

    private void printTittleLine(Paint paint, PrintCanvas canvas, String msg, boolean lineOne){
        if (lineOne)
            separatorFld(paint, canvas, S_SMALL);

        setTextPrint(msg, paint, true, canvas, S_SMALL);

        separatorFld(paint, canvas, S_SMALL);
    }

    private void separatorFld(Paint paint, PrintCanvas canvas, int size){
        switch (size){
            case S_SMALL:
                setTextPrint("----------------------------------------------------------------------", paint, boldOn, canvas, S_SMALL);
                break;
            case S_MEDIUM:
                setTextPrint("--------------------------------------------------------", paint, boldOn, canvas, S_MEDIUM);
                break;
            case S_BIG:
                setTextPrint("------------------------------------", paint, boldOn, canvas, S_BIG);
                break;
            default:
                break;
        }
    }

    private String formatAmount(String amount) {
        String formatOut = "";

       try {

           String amnt =amount.replaceAll("[.,]","");

           if (amnt.length() > 2) {
               formatOut = amnt.substring(0, amnt.length() - 2) + "." + amnt.substring(amnt.length() - 2);
           } else {
               formatOut = "0." + amnt;
           }
           DecimalFormat formatMin = new DecimalFormat("###,##0.00");
           return (formatMin.format(Double.parseDouble(formatOut)));
       }catch (Exception e){
           Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
           return "0";
       }
    }

    private String setTextColumn(String columna1, String columna2, int size) {
        String aux = "";
        String auxText = columna2;
        auxText = setRightText(auxText, size);
        String auxText2 = columna1;

        if (auxText2.length() < auxText.length())
            aux = auxText.substring(auxText2.length());

        auxText2 += aux;

        return auxText2;
    }

    private String checkNumCharacters(String data, int size) {
        String dataPrint = "";
        int lenData = 0;

        lenData = data.length();

        switch (size) {
            case S_SMALL:
                if (lenData > MAX_CHAR_SMALL) {
                    dataPrint = data.substring(0, MAX_CHAR_SMALL);
                } else {
                    dataPrint = data;
                }
                break;

            case S_MEDIUM:
                if (lenData > MAX_CHAR_MEDIUM) {
                    dataPrint = data.substring(0, MAX_CHAR_MEDIUM);
                } else {
                    dataPrint = data;
                }
                break;

            case S_BIG:
                if (lenData > MAX_CHAR_BIG) {
                    dataPrint = data.substring(0, MAX_CHAR_BIG);
                } else {
                    dataPrint = data;
                }
                break;

            default:
                break;

        }
        return dataPrint;
    }

    private void println(Paint paint, PrintCanvas canvas) {
        setTextPrint("                                             ", paint, boldOn, canvas, S_SMALL);
    }

    private void setTextPrint(String data, Paint paint, boolean bold, PrintCanvas canvas, int size) {
        Typeface typeface = Typeface.create("monospace", Typeface.ITALIC);
        data = checkNumCharacters(data, size);
        canvas.drawBitmap(drawText(data, (float) size, bold, typeface), paint);
    }

    private String setCenterText(String data, int size) {
        data = padLeft(checkNumCharacters(data.trim(), size), size);
        return data;
    }

    private String setRightText(String data, int size) {
        StringBuilder dataFinal = new StringBuilder();
        int len1 = 0;
        switch (size) {
            case S_SMALL:
                len1 = MAX_CHAR_SMALL - data.length();
                break;
            case S_MEDIUM:
                len1 = MAX_CHAR_MEDIUM - data.length();
                break;
            case S_BIG:
                len1 = MAX_CHAR_BIG - data.length();
                break;
            default:
                break;
        }
        for (int i = 0; i < len1; i++) {
            dataFinal.append(" ");
        }
        dataFinal.append(data);
        return dataFinal.toString();
    }

    private String padLeft(String data, int size) {
        StringBuilder dataFinal = new StringBuilder();
        int len1 = 0;
        switch (size) {
            case S_SMALL:
                len1 = MAX_CHAR_SMALL - data.length();
                break;
            case S_MEDIUM:
                len1 = MAX_CHAR_MEDIUM - data.length();
                break;
            case S_BIG:
                len1 = MAX_CHAR_BIG - data.length();
                break;
            default:
                break;
        }
        for (int i = 0; i < len1 / 2; i++) {
            dataFinal.append(" ");
        }
        dataFinal.append(data);
        return dataFinal.toString();
    }

    private Bitmap drawText(String text, float textSize, boolean bold, Typeface typeface) {

        // Get text dimensions
        TextPaint textPaint = new TextPaint(ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(typeface);
        textPaint.setFakeBoldText(bold);

        StaticLayout mTextLayout = new StaticLayout(text, textPaint, 400, Layout.Alignment.ALIGN_NORMAL, 40.0f, 20.0f, false);

        // Create bitmap and canvas to draw to
        Bitmap b = Bitmap.createBitmap(400, mTextLayout.getHeight(), Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);

        // Draw background
        Paint paint = new Paint(ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        c.drawPaint(paint);

        // Draw text
        c.save();
        c.translate(0, 0);
        mTextLayout.draw(c);
        c.restore();

        return b;
    }

    private int printData(PrintCanvas pCanvas, boolean isMsg) {
        final CountDownLatch latch = new CountDownLatch(1);
        printer = Printer.getInstance();
        int ret = printer.getStatus();
        Logger.debug("打印机状态：" + ret);
        if (Printer.PRINTER_STATUS_PAPER_LACK == ret) {
            Logger.debug("打印机缺纸，提示用户装纸");
            transUI.handlingBCP(TcodeSucces.PRINTER_LACK_PAPER, TcodeSucces.MSGEMPTY,false);
            long start = SystemClock.uptimeMillis();
            while (printer.getStatus() != Printer.PRINTER_OK) {
                if (SystemClock.uptimeMillis() - start > 90 * 1000) {
                    estadisticas.writeEstadisticas(Estadisticas.TRANSIMPRE,dataWs.getNameTrans());
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(),"printer task interrupted" + e);
                }
            }
            ret = printer.getStatus() == Printer.PRINTER_OK ?  Printer.PRINTER_OK : Printer.PRINTER_STATUS_PAPER_LACK;
        }
        Logger.debug("开始打印");
        if (ret == Printer.PRINTER_OK) {
            if (isMsg)
                transUI.handlingBCP(TcodeSucces.PRINTING_RECEPT,TcodeSucces.MSGEMPTY,false);
            printTask.setPrintCanvas(pCanvas);
            printer.startPrint(printTask, (i, print2Task) -> latch.countDown());
            try {
                latch.await();
            } catch (InterruptedException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION + e.toString());
                Thread.currentThread().interrupt();
            }
        }
        return ret;
    }

    private String getFechaCierre(String cierre) {
        SharedPreferences prefs = mContext.getSharedPreferences("fecha-cierre", MODE_PRIVATE);
        return prefs.getString(cierre, null);
    }
    private void separador(String leadDescription,int size,Paint paint, boolean bold, PrintCanvas canvas) {
        String aux;
        int length = 0;
        int offset = 0;
        if(leadDescription != null && !leadDescription.equals("")){
            switch (size){
                case  S_SMALL:
                    length = MAX_CHAR_SMALL;
                break;
                case S_MEDIUM:
                    length = MAX_CHAR_MEDIUM;
                    break;
                case S_BIG:
                    length = MAX_CHAR_BIG;
                    break;
                default:
                    break;
            }
            for(int i =leadDescription.length()-1 ; i>0 ; i = i-length){
                if(i > length){
                    aux = leadDescription.substring(offset,offset+length);
                }else {
                    aux = leadDescription.substring(offset);
                }
                offset+=length;
               setTextPrint(aux,paint,bold,canvas,size);
            }
        }
    }
}
