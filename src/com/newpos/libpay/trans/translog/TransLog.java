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

/**
 * 交易日志管理类
 * @author
 */

public class TransLog implements Serializable {
	private static final String TRAN_LOG_PATH = "translog.dat";
	private static final String SCRIPT_PATH = "script.dat";
	private static final String REVERSAL_PATH = "reversal.dat";

	private List<TransLogData> transLogData = new ArrayList<>();
	private static TransLog tranLog;
	private static String idAcqTmp;

	private static final String MSG_FILE_NOT_FOUND = "save translog file not found";
	private static final String MSG_IOEXCEPTION = "save translog IOException";
	private static final String MSG_EXCEPTION = "Exception";
	private static final String MSG_ERROR = "error";

	private TransLog() {
	}

	public static TransLog getInstance() {
		if (tranLog == null) {
			String filepath = TMConfig.getRootFilePath() + TRAN_LOG_PATH;
			try {
				tranLog = ((TransLog) PAYUtils.file2Object(filepath));
			} catch (ClassNotFoundException | IOException e) {
				Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " TransLog " +  e.getMessage());
				tranLog = null;
			}
			if (tranLog == null) {
				tranLog = new TransLog();
			}
		}
		return tranLog;
	}

	public static TransLog getInstance(String acquirerId) {

		if (idAcqTmp != null && !idAcqTmp.equals(acquirerId)){
				tranLog = null;
		}

		if (tranLog == null) {
			idAcqTmp = acquirerId;
			String filepath = TMConfig.getRootFilePath() + acquirerId + TRAN_LOG_PATH;
			try {
				tranLog = ((TransLog) PAYUtils.file2Object(filepath));
			} catch (ClassNotFoundException | IOException e) {
				Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), " TransLog " +  e.getMessage());
				tranLog = null;
			}
			if (tranLog == null) {
				tranLog = new TransLog();
			}
		}
		return tranLog;
	}

	public List<TransLogData> getData() {
		return transLogData;
	}

	public int getSize() {
		return transLogData.size();
	}

	public TransLogData get(int position) {
		if ((position <= getSize())) {
			return transLogData.get(position);
		}
		return null;
	}

	/**
	 * 清除交易记录的二进制文件
	 */
	public void clearAll() {
		transLogData.clear();
		String fullName = TMConfig.getRootFilePath() + TRAN_LOG_PATH;
		File file = new File(fullName);
		if (file.exists()) {
			Logger.debug("Archivo eliminado - clearAll" + file.delete());
		}
	}

	/**
	 * 清除交易记录的二进制文件
	 */
	public void clearAll(String acquirerId) {
		transLogData.clear();
		String fullName = TMConfig.getRootFilePath() + acquirerId + TRAN_LOG_PATH;
		File file = new File(fullName);
		if (file.exists()) {
			Logger.debug("Archivo eliminado - clearAll" + file.delete());

			//MDM
			if(!ToolsBatch.transActive(acquirerId)){
				try {
					readWriteFileMDM.writeFileMDM(readWriteFileMDM.getReverse(), ReadWriteFileMDM.SETTLE_DEACTIVE, readWriteFileMDM.getInitAuto(),readWriteFileMDM.getTrans());
				}catch (Exception e){
					Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR +  e);
				}
			}
		}
	}

	/**
	 * 获取上一条交易记录
	 */
	public TransLogData getLastTransLog() {
		if (getSize() >= 1) {
			return transLogData.get(getSize() - 1);
		}
		return null;
	}

	/**
	 * 保存交易记录
	 * @return
	 */
	public boolean saveLog(TransLogData data) {
		transLogData.add(data);
		Logger.debug("transLogData size " + transLogData.size());
		try {
			PAYUtils.object2File(tranLog, TMConfig.getRootFilePath()+ TRAN_LOG_PATH);
		} catch (FileNotFoundException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_FILE_NOT_FOUND +  e);
			return false;
		} catch (IOException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_IOEXCEPTION +  e);
			return false;
		}
		return true;
	}

	public boolean saveLog(TransLogData data, String acquirerId) {
		transLogData.add(data);
		Logger.debug("transLogData size " + transLogData.size());
		try {
			PAYUtils.object2File(tranLog, TMConfig.getRootFilePath()+ acquirerId + TRAN_LOG_PATH);

		} catch (FileNotFoundException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_FILE_NOT_FOUND +  e);
			return false;
		} catch (IOException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_IOEXCEPTION +  e);
			return false;
		}
		return true;
	}

	public boolean saveLog(String acquirerId) {
		try {
			PAYUtils.object2File(tranLog, TMConfig.getRootFilePath()+ acquirerId + TRAN_LOG_PATH);
		} catch (FileNotFoundException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_FILE_NOT_FOUND +  e);
			return false;
		} catch (IOException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_IOEXCEPTION +  e);
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
	public boolean updateTransLog(int logIndex, TransLogData newData) {
		if (getSize() > 0) {
			transLogData.set(transLogData.indexOf(transLogData.get(logIndex)), newData);
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
		for (int i = 0 ; i < transLogData.size() ; i++){
			if(transLogData.get(i).getTraceNo().equals(data.getTraceNo())){
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
	public TransLogData searchTransLogByIndex(int logIndex) {
		if (getSize() > 0 && getSize() - 1 >= logIndex) {
			return transLogData.get(logIndex);
		}
		return null;
	}

	/**
	 * 根据流水号获取交易记录
	 * @param traceNo 交易流水号
	 * @return 交易记录
	 */
	public TransLogData searchTransLogByTraceNo(String traceNo) {
		if (getSize() > 0) {
			for (int i = 0; i < getSize(); i++) {
				if (!PAYUtils.isNullWithTrim(transLogData.get(i).getTraceNo()) && transLogData.get(i).getTraceNo().equals("" + traceNo)) {
						return transLogData.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 根据授权码及日期获取交易记录
	 * @param auth date
	 * @return 交易记录
	 */
	public TransLogData searchTransLogByAUTHDATE(String auth , String date) {
		if (getSize() > 0) {
			for (int i = 0; i < getSize(); i++) {
				TransLogData data = transLogData.get(i);
				if ((!PAYUtils.isNullWithTrim(data.getAuthCode()) && !PAYUtils.isNullWithTrim(data.getLocalDate())) && (data.getAuthCode().equals("" + auth) && data.getLocalDate().equals("" + date))) {
						return data;
				}
			}
		}
		return null;
	}

	/**
	 * 根据参考号及日期获取交易记录
	 * @param refer date
	 * @return 交易记录
	 */
	public TransLogData searchTransLogByREFERDATE(String refer , String date) {
		if (getSize() > 0) {
			for (int i = 0; i < getSize(); i++) {
				TransLogData data = transLogData.get(i);
				if ((!PAYUtils.isNullWithTrim(data.getRrn()) && !PAYUtils.isNullWithTrim(data.getLocalDate())) && (data.getRrn().equals("" + refer) && data.getLocalDate().equals("" + date))) {
						return data;
				}
			}
		}
		return null;
	}


	/**
	 * 保存脚本结果
	 * @return
	 */
	public static boolean saveScriptResult(TransLogData data) {
		try {
			PAYUtils.object2File(data, TMConfig.getRootFilePath()+ SCRIPT_PATH);
		} catch (IOException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION +  e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 保存冲正信息
	 * @return
	 */
	public static boolean saveReversal(TransLogData data ) {
		try {
			PAYUtils.object2File(data, TMConfig.getRootFilePath()+ REVERSAL_PATH);

		} catch (IOException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION +  e.toString());
			return false;
		}
		return true;
	}

	/**
	 * 获取冲正信息
	 * @return
	 */
	public static TransLogData getReversal() {
		try {
			return (TransLogData) PAYUtils.file2Object(TMConfig.getRootFilePath() + REVERSAL_PATH);
		} catch (ClassNotFoundException | IOException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION +  e.toString());
		}
		return null;
	}

	/**
	 * 获取脚本信息
	 * @return
	 */
	public static TransLogData getScriptResult() {
		try {
			return (TransLogData) PAYUtils.file2Object(TMConfig.getRootFilePath()+ SCRIPT_PATH);
		} catch (ClassNotFoundException | IOException e) {
			Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_EXCEPTION +  e.toString());
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
				readWriteFileMDM.writeFileMDM(ReadWriteFileMDM.REVERSE_DEACTIVE, readWriteFileMDM.getSettle(), readWriteFileMDM.getInitAuto(), readWriteFileMDM.getTrans());
			}catch (Exception e){
				Logger.logLine(Logger.LOG_EXECPTION, e.getStackTrace(), MSG_ERROR +  e.toString());
			}
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 清除脚本执行结果
	 * @return
	 */
	public static boolean clearScriptResult() {
		File file = new File(TMConfig.getRootFilePath()+ SCRIPT_PATH);
		if (file.exists() && file.isFile()) {
			Logger.debug("Archivo eliminado - clearScriptResult" + file.delete());
			return false;
		} else {
			return true;
		}
	}
}