package com.bcp.inicializacion.trans_init;

import android.content.Context;
import android.media.ToneGenerator;

import com.android.newpos.pay.R;
import com.bcp.cambio_clave.UtilChangePwd;
import com.bcp.file_management.FilesManagement;
import com.bcp.inicializacion.sqlite.frompackage.DBHelper;
import com.bcp.inicializacion.tools.PolarisUtil;
import com.bcp.inicializacion.trans_init.trans.UnpackFile;
import com.bcp.mdm_validation.ReadWriteFileMDM;
import com.bcp.settings.BCPConfig;
import com.bcp.transactions.common.CommonFunctionalities;
import com.newpos.libpay.Logger;
import com.newpos.libpay.device.card.CardInfo;
import com.newpos.libpay.device.card.CardManager;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.process.EmvTransaction;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTrans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import cn.desert.newpos.payui.UIUtils;

import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.android.newpos.pay.StartAppBCP.readWriteFileMDM;
import static com.android.newpos.pay.StartAppBCP.variables;
import static com.bcp.definesbcp.Definesbcp.GERCARD_MSG_ICC1;
import static com.bcp.definesbcp.Definesbcp.GERCARD_MSG_ICC2;
import static com.bcp.definesbcp.Definesbcp.NAME_FOLDER_DB;
import static com.bcp.definesbcp.Definesbcp.PATH_DATA;
import static com.bcp.definesbcp.Variables.FALLBACK;
import static com.bcp.inicializacion.tools.PolarisUtil.AGENTE;
import static com.bcp.inicializacion.tools.PolarisUtil.CAPKS;
import static com.bcp.inicializacion.tools.PolarisUtil.CARDS;
import static com.bcp.inicializacion.tools.PolarisUtil.CATEGORY;
import static com.bcp.inicializacion.tools.PolarisUtil.COMPANNY;
import static com.bcp.inicializacion.tools.PolarisUtil.CREDENTIALS;
import static com.bcp.inicializacion.tools.PolarisUtil.EMVAPPS;
import static com.bcp.inicializacion.tools.PolarisUtil.GENERAL;
import static com.bcp.inicializacion.tools.PolarisUtil.NAME_DB;
import static com.bcp.inicializacion.tools.PolarisUtil.TRANSACCIONES;
import static com.bcp.inicializacion.tools.PolarisUtil.URL;

public class Initialization extends FinanceTrans implements TransPresenter {

    public Initialization(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
        this.transUI = p.getTransUI();
        this.transEName = transEname;
        isReversal = false;
        isProcPreTrans = false;
        isSaveLog = false;
        isDebit = false;
        currencyName = CommonFunctionalities.tipoMoneda()[0];
        typeCoin = CommonFunctionalities.tipoMoneda()[1];

        filesManagement = new FilesManagement(context);
        fileNameInit=termID+".zip";
        offsetInit = 0;
    }

    @Override
    public void start() {

        if(transEName.equals(INIT_T) && !cardProcess())
            return;

        filesManagement.calculateOffset(fileNameInit, true);

        Logger.logLine(Logger.LOG_GENERAL, "===============Start Inicializacion===============");

        connecting();

        if(tryConnect()!=0) {
            Logger.logLine(Logger.LOG_GENERAL, "tryConnect()!=0");
            endTrans(false);
            return;
        }

        if(!jweEncryptDecrypt(BCPConfig.getInstance(context).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY)  +"", true, false)) {
            retVal = TcodeError.T_ERR_ENCRIPT_DATA;
            endTrans(false);
            return;
        }

        retVal = onlineTransInit();

        Logger.logLine(Logger.LOG_GENERAL, "retVal onlineTransInit : " + retVal);

        if (retVal == 0 || retVal == 95){
            if (!"960100".equals(procCode)) {
                endTrans(true);
                return;
            }

            if (jweDataEncryptDecrypt.getDataDecrypt() == null || jweDataEncryptDecrypt.getDataDecrypt().equals("")){
                field60 = "";
                retVal = TcodeError.T_ERR_KEY;
                endTrans(true);
                return;
            }

            if(transEName.equals(INIT_T) && retVal == 0) {
                retVal=CommonFunctionalities.setChangePassword(timeout,transUI, "Crear nueva clave", 0, 6, transEName);
                if (retVal != 0) {
                    field60 = "";
                    endTrans(true);
                    return;
                }

                BCPConfig.getInstance(context).setLoginPassword(BCPConfig.LOGINPASSWORDKEY,CommonFunctionalities.getNewPassword());
                UtilChangePwd.saveDateChange(context);
            }

            if (retVal!=95) {//Si la respuesta es 95 no se debe actualizar la BD
                if (netWork!=null)
                    netWork.close();
                processFile();
            }
            else
                endTrans(true);
        }else{
            endTrans(true);
        }

    }

    private void readDBInit(){
        polarisUtil.setCallbackReadDB(null);
        polarisUtil.setCallbackReadDB(status -> {
            if (status) {
                if (!polarisUtil.isInit()) {
                    retVal = TcodeError.T_ERR_PROCESING_INIT;
                    field60 = "";
                }
                polarisUtil.setInit(true, BCPConfig.getInstance(context).getTrack2Agente(BCPConfig.TRACK2AGENTEKEY));
            }else {
                retVal = TcodeError.T_ERR_PROCESING_INIT;
                field60 = "";
            }
            endTrans(false);
        });
        transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.MSGEMPTY,false);
        polarisUtil.isInitPolaris(context);
    }

    private void endTrans(boolean isopened){

        Logger.logLine(Logger.LOG_GENERAL, "...endTrans");
        Logger.logLine(Logger.LOG_GENERAL, "retVal : " + retVal);

        if (retVal == 0 || retVal == 95) {
            PolarisUtil.dateSave(context);
            if(transEName.equals(INIT_T)){
                transUI.trannSuccess(TcodeSucces.INIT_SUCC, validateTrans(),"configuradas");
            }else {
                transUI.trannSuccess(TcodeSucces.INIT_PARCIAL, validateTrans(),"Exitosa");
            }
            checkAutoInit();
            BCPConfig.getInstance(context).setAmntMaxAgent(BCPConfig.AMOUNTMAXAGENTKEY,(long) 0);
            UIUtils.beep(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD);
        }else {
            if (field60 == null || field60.equals(""))
                transUI.showError(transEName, retVal);
            else
                transUI.showError(transEName, String.valueOf(retVal), field60);
        }

        Logger.logLine(Logger.LOG_GENERAL, "isopened : " + isopened);

        if (isopened)
            netWork.close();
    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }


    private boolean cardProcess() {

        CardInfo cardInfo = transUI.getCardUse(GERCARD_MSG_ICC1, GERCARD_MSG_ICC2, timeout, INMODE_IC);

        if (cardInfo.isResultFalg()) {
            int type = cardInfo.getCardType();
            if (type == CardManager.TYPE_ICC) {
                inputMode = ENTRY_MODE_ICC;
            } else {
                transUI.showError(transEName, TcodeError.T_NOT_ALLOW);
                return false;
            }
            para.setInputMode(inputMode);

            if(isICC()) {
                try {
                    transUI.verifyCard(context.getResources().getString(R.string.listo),true);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Initialization " +  e.getMessage());
                    Thread.currentThread().interrupt();
                }
                return true;
            }

        } else {
            retVal = cardInfo.getErrno();
            if(retVal == 107) {
                if (!CommonFunctionalities.validateCard(timeout, transUI)) {
                    transUI.showError(transEName, TcodeError.T_ERR_TIMEOUT);
                    return false;
                }
                variables.setContFallback(variables.getContFallback() + 1);
            }else{
                transUI.showError(transEName, retVal);
                return false;
            }
        }

        if (variables.getContFallback() == FALLBACK){
            variables.setContFallback(0);
            transUI.showError(transEName, TcodeError.T_ERR_DETECT_CARD_FAILED);
            return false;
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Initialization 2" +  e.getMessage());
            Thread.currentThread().interrupt();
        }

        if (retVal == 107 || retVal == 109){
            return cardProcess();
        }

        return false;
    }
    private boolean isICC() {
        para.setAmount(0);
        para.setOtherAmount(0);
        transUI.verifyCard(context.getResources().getString(R.string.verificando),false);
        emv = new EmvTransaction(para,transEName);
        emv.setTraceNo(traceNo);
        retVal = emv.start();

        if (retVal == 1 || retVal == 0) {
            track2 = emv.getTrack2().replace('D', '=');
            pan = emv.getCardNo();

            //Del establecimiento
            track2 = "4557885500076768=22022262840738300000";//TODO SOLO PARA PRUEBAS DEV
            pan = "4557885500076768";//TODO SOLO PARA PRUEBAS DEV
            //track2 = "4912466000039133=21112262365418800000";//TODO SOLO PARA PRUEBAS QA
            //pan = "4912466000039133";//TODO SOLO PARA PRUEBAS QA
            //track2 ="4912466000039158=21112262365418800000";//TODO SOLO PARA PRUEBAS QA JOSE
            //pan ="4912466000039158";//TODO SOLO PARA PRUEBAS QA JOSE

            BCPConfig.getInstance(context).setTrack2Agente(BCPConfig.TRACK2AGENTEKEY,track2);
            BCPConfig.getInstance(context).setPanAgente(BCPConfig.PANAGENTEKEY,pan);
            return true;
        } else {
            transUI.showError(transEName, retVal);
            return false;
        }
    }

    private void processFile() {
        File file = new File(filesManagement.getPathDefault() + File.separator + fileNameInit);
        if (file.exists()) {
            transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.MSGEMPTY,false);
            if (fileNameInit.endsWith(".zip")) {
                UnpackFile unpackFile = new UnpackFile(context, fileNameInit, filesManagement.getPathDefault() + File.separator, false, false, okUnpack -> {
                    if (okUnpack) {
                        int i = 0;
                        String nameFile = getNameTableById(i);
                        while (!nameFile.equals("")){

                            processData(nameFile, "init.db");

                            i++;
                            nameFile = getNameTableById(i);
                        }
                        if (retVal == 0) {
                            filesManagement.deleteFolderContent();
                            readDBInit();
                        }
                    } else {
                        transUI.showError(transEName,TcodeError.T_ERR_UNPACK_FILE);
                    }
                });
                unpackFile.execute();
            }else {
                transUI.showError(transEName,TcodeError.T_ERR_NOT_FILE);
            }
        }
    }

    /**
     * Lee la BD y copia en una ruta interna dentro del
     * package de la aplicacion (acceso solo desde la app), posterior a esto se eliminan del
     * SD
     * @param aFileName
     * @param aFileWithOutExt
     * @return
     */
    private void processData(String aFileName, String aFileWithOutExt) {
        File fileLocation = new File(filesManagement.getPathDefault() + File.separator + aFileName);

        String packageName = context.getPackageName();
        String directory =  File.separator + PATH_DATA + File.separator +
                            PATH_DATA + File.separator+ packageName +
                            File.separator +NAME_FOLDER_DB + File.separator;
        File file = new File(directory + File.separator + aFileWithOutExt);

        if (fileLocation.exists()) {

            if (aFileName.endsWith(".db")||aFileName.endsWith(".DB")) {
                try (FileInputStream inputRead = new FileInputStream(fileLocation)){

                    byte[] inputBuffer = new byte[4096];
                    int charRead;

                    try(FileOutputStream outWrite = new FileOutputStream(file)){
                        while ((charRead = inputRead.read(inputBuffer)) > 0) {
                            outWrite.write(inputBuffer,0,charRead);
                        }
                        outWrite.flush();
                        retVal = 0;
                    }
                } catch (Exception e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Initialization 3 " +  e.getMessage());
                }
            }else if (aFileName.endsWith(".sql")){
                DBHelper dbAccess = new DBHelper(context, NAME_DB, null, 1);
                dbAccess.openDb(NAME_DB);
                try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))){
                    String sentencia = br.readLine();

                    while(sentencia != null) {
                        dbAccess.execSql(sentencia);
                        sentencia = br.readLine();
                    }
                    retVal = 0;
                } catch (FileNotFoundException e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Error: Fichero no encontrado" + e.getMessage());
                } catch(Exception e) {
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "Error de lectura del fichero" + e.getMessage());
                }
            }
        }
    }

    private String getNameTableById(int id)
    {
        String ret="";
        switch (id)
        {
            case 0:
                ret = GENERAL;
                break;
            case 1:
                ret=AGENTE;
                break;
            case 2:
                ret=CAPKS;
                break;
            case 3:
                ret=CARDS;
                break;
            case 4:
                ret=EMVAPPS;
                break;
            case 5:
                ret=TRANSACCIONES;
                break;
            case 6:
                ret=URL;
                break;
            case 7:
                ret = CREDENTIALS;
                break;
            case 8:
                ret = COMPANNY;
                break;
            case 9:
                ret = CATEGORY;
                break;
            default:
                break;
        }
        return ret;
    }

    private void checkAutoInit(){
        try {
            if (readWriteFileMDM.getInitAuto().equals(ReadWriteFileMDM.INITAUTOACTIVE)) {
                readWriteFileMDM.writeFileMDM(readWriteFileMDM.getReverse(), readWriteFileMDM.getSettle(), ReadWriteFileMDM.INITAUTODEACTIVE, readWriteFileMDM.getTrans());
            }
        } catch (Exception e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " Initialization 4 " + e.getMessage());
        }
    }
}
