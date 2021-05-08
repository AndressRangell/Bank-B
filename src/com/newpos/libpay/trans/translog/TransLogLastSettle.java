package com.newpos.libpay.trans.translog;

import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.utils.PAYUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TransLogLastSettle implements Serializable{
    private static String lastSettlePath = "lastSettle.dat";

    private List<TransLogData> transLogData = new ArrayList<>();
    private static TransLogLastSettle transLogLastSettle;

    private TransLogLastSettle() {
    }

    public List<TransLogData> getData() {
        return transLogData;
    }
    public void setTransLogData(List<TransLogData> transLogData) {
        this.transLogData = transLogData;
    }

    public static TransLogLastSettle getInstance(boolean read) {

        if (!read){
            transLogLastSettle = null;
        }

        if (transLogLastSettle == null) {
            String filepath = TMConfig.getRootFilePath() + lastSettlePath;
            try {
                transLogLastSettle = ((TransLogLastSettle) PAYUtils.file2Object(filepath));
            } catch (ClassNotFoundException | IOException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " TransLog " +  e.getMessage());
                transLogLastSettle = null;
            }
            if (transLogLastSettle == null) {
                transLogLastSettle = new TransLogLastSettle();
            }
        }
        return transLogLastSettle;
    }

    public boolean saveLog() {

        clearLastSettle();

        try {
            PAYUtils.object2File(transLogLastSettle, TMConfig.getRootFilePath()+lastSettlePath);
        } catch (FileNotFoundException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), "save translog file not found" + e.getMessage());
            return false;
        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(),"save translog IOException" + e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean clearLastSettle() {
        File file = new File(TMConfig.getRootFilePath()+ transLogLastSettle);
        if (file.exists() && file.isFile()) {
            Logger.debug("Archivo eliminado - clearLastSettle" + file.delete());
            return false;
        } else {
            return true;
        }
    }
}
