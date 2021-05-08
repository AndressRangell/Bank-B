package com.newpos.libpay.trans.translog;

import com.bcp.mdm_validation.ReadWriteFileMDM;
import com.bcp.tools_bacth.ToolsBatch;
import com.newpos.libpay.Logger;
import com.newpos.libpay.global.TMConfig;
import com.newpos.libpay.utils.PAYUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.android.newpos.pay.StartAppBCP.readWriteFileMDM;

public class TransLogWs implements Serializable {

    private static final String TRAN_LOG_PATH = "batch.dat";
    private static final String REVERSAL_PATH = "reversal.dat";

    private List<TransLogDataWs> transLogDataWs = new ArrayList<>();
    private static TransLogWs tranLogWs;

    private static final String MSG_FILE_NOT_FOUND = "save translog file not found";
    private static final String MSG_IOEXCEPTION = "save translog IOException";
    private static final String MSG_EXCEPTION = "Exception";
    private static final String MSG_ERROR = "error";

    private TransLogWs() {
    }

    public static TransLogWs getInstance() {
        if (tranLogWs == null) {
            String filepath = TMConfig.getRootFilePath() + TRAN_LOG_PATH;
            try {
                tranLogWs = ((TransLogWs) PAYUtils.file2Object(filepath));
            } catch (ClassNotFoundException | IOException e) {
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                tranLogWs = null;
            }
            if (tranLogWs == null) {
                tranLogWs = new TransLogWs();
            }
        }
        return tranLogWs;
    }

    public List<TransLogDataWs> getData() {
        return transLogDataWs;
    }

    public int getSize() {
        return transLogDataWs.size();
    }

    public TransLogDataWs get(int position) {
        if ((position <= getSize())) {
            return transLogDataWs.get(position);
        }
        return null;
    }

    /**
     * 清除交易记录的二进制文件
     */
    public void clearAll() {
        transLogDataWs.clear();
        String fullName = TMConfig.getRootFilePath() + TRAN_LOG_PATH;
        File file = new File(fullName);
        if (file.exists()) {
            Logger.debug("Archivo eliminado - clearAll" + file.delete());
            //MDM
            if(ToolsBatch.statusTrans()){
                try {
                    readWriteFileMDM.writeFileMDM(readWriteFileMDM.getReverse(), ReadWriteFileMDM.SETTLE_DEACTIVE, readWriteFileMDM.getInitAuto(),readWriteFileMDM.getTrans());
                }catch (Exception e){
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e.getMessage());
                }
            }
        }
    }

    /**
     * 获取上一条交易记录
     */
    public TransLogDataWs getLastTransLogDataWs() {
        if (getSize() >= 1) {
            return transLogDataWs.get(getSize() - 1);
        }
        return null;
    }

    /**
     * 保存交易记录
     * @return
     */
    public boolean saveLogWs(TransLogDataWs data) {
        transLogDataWs.add(data);
        Logger.debug("transLogData size " + transLogDataWs.size());
        try {
            PAYUtils.object2File(tranLogWs, TMConfig.getRootFilePath()+ TRAN_LOG_PATH);

            //MDM
            if(!ToolsBatch.statusTrans()){
                try {
                    readWriteFileMDM.writeFileMDM(readWriteFileMDM.getReverse(), ReadWriteFileMDM.SETTLE_ACTIVE, readWriteFileMDM.getInitAuto(),readWriteFileMDM.getTrans());
                }catch (Exception e){
                    Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_FILE_NOT_FOUND + e.getMessage());
            return false;
        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_IOEXCEPTION + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 更新交易记录
     * @param logIndex 交易记录索引
     * @param newData 更新后的数据
     * @return 更新结果
     */
    public boolean updateTransLogWs(int logIndex, TransLogDataWs newData) {
        if (getSize() > 0) {
            transLogDataWs.set(transLogDataWs.indexOf(transLogDataWs.get(logIndex)), newData);
            return true;
        }
        return false;
    }

    /**
     * 获取当前交易的索引号
     * @param data
     * @return
     */
    public int getCurrentIndex(TransLogData data){
        int current = -1 ;
        for (int i = 0 ; i < transLogDataWs.size() ; i++){
            if(transLogDataWs.get(i).getTransactionNumber().equals(data.getTraceNo())){
                current = i ;
            }
        }
        return current ;
    }

    /**
     * 根据索引获取交易记录
     * @param logIndex 交易记录索引
     * @return 交易对象
     */
    public TransLogDataWs searchTransLogByIndex(int logIndex) {
        if (getSize() > 0 && getSize() - 1 >= logIndex) {
            return transLogDataWs.get(logIndex);
        }
        return null;
    }

    /**
     * 根据流水号获取交易记录
     * @param traceNo 交易流水号
     * @return 交易记录
     */
    public TransLogDataWs searchTransLogByTraceNo(String traceNo) {
        if (getSize() > 0) {
            for (int i = 0; i < getSize(); i++) {
                if (!PAYUtils.isNullWithTrim(transLogDataWs.get(i).getTransactionNumber()) && transLogDataWs.get(i).getTransactionNumber().equals("" + traceNo)) {
                    return transLogDataWs.get(i);
                }
            }
        }
        return null;
    }

    //========== Reversal ===========

    /**
     * 保存冲正信息
     * @return
     */
    public static boolean saveReversal(TransLogDataWs data ) {
        try {
            PAYUtils.object2File(data, TMConfig.getRootFilePath()+ REVERSAL_PATH);

        } catch (IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION + e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 获取冲正信息
     * @return
     */
    public static TransLogDataWs getReversal() {
        try {
            return (TransLogDataWs) PAYUtils.file2Object(TMConfig.getRootFilePath() + REVERSAL_PATH);
        } catch (ClassNotFoundException | IOException e) {
            Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION + e.getMessage());
        }
        return null;
    }

    /**
     * 清除冲正
     * @return
     */
    public static boolean clearReveral() {
        File file = new File(TMConfig.getRootFilePath() + REVERSAL_PATH);
        if (file.exists() && file.isFile()) {
            Logger.debug("Archivo eliminado - clearReveral" + file.delete());
            try {
                readWriteFileMDM.writeFileMDM(ReadWriteFileMDM.REVERSE_DEACTIVE, readWriteFileMDM.getSettle(), readWriteFileMDM.getInitAuto(),readWriteFileMDM.getTrans());
            }catch (Exception e){
                Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR + e);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
