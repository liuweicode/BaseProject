package com.wy.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import android.os.Environment;

import com.wy.AppConstants;
import com.wy.utils.FileUtils;

/** 
 * 描述：应用程序异常类：用于捕获异常和提示错误信息
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-16
 */
public abstract class AppException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private static final String EXCEPTION_LOG_FILE_NAME = "ExceptionLog.txt";
	
	private static final long EXCEPTION_LOG_FILE_MAX_SIZE = 1*1024*1024;//日志文件最大1M
	
	/**
	 * 保存异常日志
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//判断是否挂载了SD卡
			if(FileUtils.checkSDCardExists()){
				String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstants.WY_LOG_PATH;
				if(!FileUtils.checkFileExists(savePath)){
					FileUtils.createDirectorys(savePath);
				}
				String logFilePath = savePath + EXCEPTION_LOG_FILE_NAME;
				File logFile = new File(logFilePath);
				if (logFile.exists()) {
					//日志文件存在 则检测其大小
					long fileSize = FileUtils.getFileSize(logFilePath);
					//如果日志文件超过1M 则删除再创建
					if(fileSize >= EXCEPTION_LOG_FILE_MAX_SIZE){
						if(logFile.delete()){
							logFile.createNewFile();
						}
					}
				}else{
					//日志文件不存在 则创建新文件
					logFile.createNewFile();
				}
				
				fw = new FileWriter(logFile,true);
				pw = new PrintWriter(fw);
				pw.println("--------------------"+(new Date().toLocaleString())+"---------------------");	
				excp.printStackTrace(pw);
				pw.close();
				fw.close();
			}
		} catch (Exception e) {
			e.printStackTrace();		
		}finally{ 
			if(pw != null){ pw.close(); } 
			if(fw != null){ try { fw.close(); } catch (IOException e) { }}
		}
	}
	
}
