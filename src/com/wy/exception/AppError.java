package com.wy.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

import com.wy.AppConstants;
import com.wy.AppContext;
import com.wy.R;
import com.wy.mail.MailUtil;
import com.wy.utils.FileUtils;
import com.wy.utils.ui.ActivityTaskManager;

/** 
 * 描述：系统严重异常（如程序异常退出）
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-16
 */
public class AppError implements UncaughtExceptionHandler{

	private static final long ERROR_LOG_FILE_MAX_SIZE = 1*1024*1024;//日志文件最大1M
	
	private static final String ERROR_LOG_FILE_NAME = "ThrowableLog.txt";
	
	//系统默认的UncaughtException处理类 
	protected Thread.UncaughtExceptionHandler mDefaultHandler;
	
	private AppError(){}
	
	private static AppError appError;
	
	public static AppError getAppError(){
		if(appError == null)
		{
			appError = new AppError();
		}
		return appError;
	}
	
	/*初始化*/
	public void initUncaught() {
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/*当UncaughtException发生时会转入该函数来处理*/
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				//AppException.exc(e).makeToast(AppContext.getContext());
			}
		}
		//退出程序
		ActivityTaskManager.getInstance().AppExit(AppContext.getContext());
	}
	
	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(AppContext.getContext(), "很抱歉,程序出现异常,即将退出!", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();
		AppContext.getInstance().setProperty(AppConstants.ISABNORMALEXIT,"true");
		//保存日志文件 
		saveErrorLog(ex);
		ActivityTaskManager.getInstance().AppExit(AppContext.getContext());
		return true;
	}
	
	/**
	 * 保存异常日志
	 * @param excp
	 */
	public void saveErrorLog(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		//在控制台打印
		System.err.println(sb.toString());
		
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//判断是否挂载了SD卡
			if(FileUtils.checkSDCardExists()){
				String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstants.WY_LOG_PATH;
				if(!FileUtils.checkFileExists(savePath)){
					FileUtils.createDirectorys(savePath);
				}
				String logFilePath = savePath + ERROR_LOG_FILE_NAME;
				File logFile = new File(logFilePath);
				if (logFile.exists()) {
					//日志文件存在 则检测其大小
					long fileSize = FileUtils.getFileSize(logFilePath);
					//如果日志文件超过1M 则删除再创建
					if(fileSize >= ERROR_LOG_FILE_MAX_SIZE){
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
				pw.write(sb.toString());
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
	
	/**
	 * 检查是否异常退出 如果异常退出 则提示用户是否需要发送错误报告
	 * 
	 * @param isConfirm 是否弹出确认对话框
	 */
	public void checkError(boolean isConfirm,final Context context){
		if(isConfirm){
			if("true".equals(AppContext.getInstance().getProperty(AppConstants.ISABNORMALEXIT))){
				new AlertDialog.Builder(context)
				.setTitle("发送错误日志")
				.setIcon(R.drawable.notification_error)
				.setMessage("系统检测到程序异常退出,是否发送错误日志？")
				.setPositiveButton("发送", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						MailUtil.sendErrorInfoMail(context);
					}
				})
				.setNegativeButton("不发送", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						AppContext.getInstance().removeProperty(AppConstants.ISABNORMALEXIT);
					}
				})
				.show();
			}
		}else{
			if("true".equals(AppContext.getInstance().getProperty(AppConstants.ISABNORMALEXIT))){
				MailUtil.sendErrorInfoMail(context);
			}
		}
		
	}
	
	
	
}
