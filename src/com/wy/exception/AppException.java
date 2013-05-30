package com.wy.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.os.Environment;

/** 
 * ������Ӧ�ó����쳣�ࣺ���ڲ����쳣����ʾ������Ϣ
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-1-16
 */
public abstract class AppException extends Exception implements IAppException{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * �����쳣��־
	 * @param excp
	 */
	public void saveErrorLog(Exception excp,String savePath) {
		String errorlog = "exception_log"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".txt";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//�ж��Ƿ������SD��
			String storageState = Environment.getExternalStorageState();		
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			//û�й���SD�����޷�д�ļ�
			if(logFilePath == ""){
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile,true);
			pw = new PrintWriter(fw);
			pw.println("--------------------"+(new Date().toLocaleString())+"---------------------");	
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();		
		}finally{ 
			if(pw != null){ pw.close(); } 
			if(fw != null){ try { fw.close(); } catch (IOException e) { }}
		}
	}
	
	
}
