package com.bcp.transactions.certificado;

import android.content.Context;
import android.content.ContextWrapper;
import com.android.newpos.pay.R;
import com.bcp.file_management.FilesManagement;
import com.bcp.inicializacion.trans_init.trans.UnpackFile;
import com.newpos.libpay.Logger;
import com.newpos.libpay.helper.iso8583.ISO8583;
import com.newpos.libpay.presenter.TransPresenter;
import com.newpos.libpay.trans.TcodeError;
import com.newpos.libpay.trans.TcodeSucces;
import com.newpos.libpay.trans.TransInputPara;
import com.newpos.libpay.trans.finace.FinanceTrans;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static com.android.newpos.pay.ProcessingCertificate.certificate;
import static com.android.newpos.pay.ProcessingCertificate.polarisUtil;
import static com.bcp.definesbcp.Definesbcp.NAME_FOLDER_CERTIFICATE;


public class CertificateTransaction extends FinanceTrans implements TransPresenter {

    public CertificateTransaction(Context ctx, String transEname, TransInputPara p) {
        super(ctx, transEname, p);
        this.transUI = p.getTransUI();
        this.transEName = transEname;
        isReversal = false;
        isProcPreTrans = false;
        isSaveLog = false;
        isDebit = false;

        filesManagement = new FilesManagement(context);
        fileNameInit=termID+".zip";
        offsetInit = 0;
    }

    @Override
    public void start() {

        filesManagement.calculateOffset(fileNameInit, true);
        connecting();

        if(tryConnect()!=0) {
            endTrans(false);
            return;
        }

        if ((retVal = onlineTransInit()) == 0){
            if (!"970100".equals(procCode)) {
                endTrans(true);
                return;
            }

            processZip();

        }else
            endTrans(true);

    }

    private void endTrans(boolean isopened){
        Logger.debug("TransaccionCertificado>>OnlineTrans=" + retVal);
        if (retVal == 0) {
            msgAprob(TcodeSucces.CERTIFICATESUCCES,context.getString(R.string.exitosoCerti));
        }else {
            if (field60 == null || field60.equals(""))
                transUI.showError(transEName, retVal);
            else
                transUI.showError(transEName, String.valueOf(retVal), field60);
        }
        if (isopened)
            netWork.close();
    }

    private boolean processFile(String aFileName, String pwdKeystore) {
        Logger.debug("procesando... " + aFileName);
        File fileLocation = new File(filesManagement.getPathDefault() + File.separator + aFileName);

        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(NAME_FOLDER_CERTIFICATE, Context.MODE_PRIVATE);

        File file = new File(directory + File.separator + aFileName);

        if (fileLocation.exists()) {
            try (FileInputStream inputRead = new FileInputStream(fileLocation)){

                byte[] inputBuffer = new byte[(int) fileLocation.length()];
                int charRead;

                try(FileOutputStream outWrite = new FileOutputStream(file)){
                    while ((charRead = inputRead.read(inputBuffer)) > 0) {
                        outWrite.write(inputBuffer,0,charRead);
                    }
                    outWrite.flush();

                    if (certificate.readCertificate(context,aFileName,pwdKeystore)==0)
                        polarisUtil.setCertificate(true);
                    else
                        polarisUtil.setCertificate(false);

                    return polarisUtil.isCertificate();
                }
            } catch (Exception e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                return false;
            }
        }
        return false;
    }

    private void processZip() {
        File file = new File(filesManagement.getPathDefault() + File.separator + fileNameInit);
        if (file.exists()) {
            transUI.handlingBCP(TcodeSucces.HANDLING ,TcodeSucces.CERTIFICATE,false);
            if (fileNameInit.endsWith(".zip")) {
                UnpackFile unpackFile = new UnpackFile(context, fileNameInit, filesManagement.getPathDefault() + File.separator, false, false, okUnpack -> {
                    if (okUnpack) {
                        Thread t = new Thread() {
                            @Override
                            public void run() {
                                int i = 0;
                                String[] nameFile = certificate.getNameKeystore(i, context);
                                while (nameFile.length > 0){

                                    if (!processFile(nameFile[0], nameFile[1])){
                                        retVal = TcodeError.T_ERR_SAVE_KEYSTORE;
                                        endTrans(true);
                                        return;
                                    }

                                    i++;
                                    nameFile = certificate.getNameKeystore(i,context);
                                }
                                if (retVal == 0)
                                    filesManagement.deleteFolderContent();

                                if (!polarisUtil.isCertificate())
                                    retVal = TcodeError.T_ERR_READ_CERTIFICATE;

                                endTrans(true);
                            }
                        };
                        t.start();
                    } else {
                        retVal = TcodeError.T_ERR_UNPACK_FILE;
                    }
                });
                unpackFile.execute();
            }else {
                retVal = TcodeError.T_ERR_NOT_FILE;
            }
        }
    }

    @Override
    public ISO8583 getISO8583() {
        return null;
    }
}
