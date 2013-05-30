package com.wy.exception;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.widget.Toast;

import com.wy.AppConstants;
import com.wy.AppContext;
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

	protected boolean isSendEmail = false;
	
	//系统默认的UncaughtException处理类 
	protected Thread.UncaughtExceptionHandler mDefaultHandler;
		
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
			//退出程序
			ActivityTaskManager.getInstance().AppExit(AppContext.getContext());
		}
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
//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
		Toast.makeText(AppContext.getContext(), "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
//				Looper.loop();
//			}
//		}.start();
		if(isSendEmail){
			sendErrorInfoMail(ex);
		}
		//保存日志文件 
		saveErrorLog(ex);
		return true;
	}
	
	public void sendErrorInfoMail(Throwable ex){
		StringBuffer sb = new StringBuffer();
		sb.append("--------------------"+(new Date().toLocaleString())+"---------------------\n");
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
		
//		  Mail m = new Mail("say.i.want.to.say@gmail.com", ""); 
//	      m.setHost("smtp.gmail.com");
//	      m.setPort("465");
//	      m.setSport("465");
//	      
//	      String[] toArr = {"i@liuwei.co"};
//	      m.setTo(toArr); 
//	      m.setFrom("say.i.want.to.say@gmail.com"); 
//	      m.setSubject("错误信息"); 
//	      m.setBody(sb.toString()); 
//	      try { 
//		        //m.addAttachment("/sdcard/EcookPad/Log/exception_log20130226.txt"); 
//		        m.send();
//	      } catch(Exception e) {
//		        Log.e("AppError", "Could not send email", e); 
//	      } 
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
		
		String errorlog = "throwable_log"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//判断是否挂载了SD卡
			String storageState = Environment.getExternalStorageState();		
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstants.WY_LOG_PATH;
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			//没有挂载SD卡，无法写文件
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
			pw.write(sb.toString());
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
