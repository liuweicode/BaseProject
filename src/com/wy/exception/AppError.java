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
import android.util.Log;
import android.widget.Toast;

import com.wy.AppConstants;
import com.wy.AppContext;
import com.wy.AppManager;
import com.wy.mail.Mail;

/** 
 * ������ϵͳ�����쳣��������쳣�˳���
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-1-16
 */
public class AppError implements UncaughtExceptionHandler{

	protected boolean isSendEmail = false;
	
	//ϵͳĬ�ϵ�UncaughtException������ 
	protected Thread.UncaughtExceptionHandler mDefaultHandler;
		
	/*��ʼ��*/
	public void initUncaught() {
		//��ȡϵͳĬ�ϵ�UncaughtException������
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//���ø�CrashHandlerΪ�����Ĭ�ϴ�����
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	
	/*��UncaughtException����ʱ��ת��ú���������*/
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			//����û�û�д�������ϵͳĬ�ϵ��쳣������������
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				//AppException.exc(e).makeToast(AppContext.getContext());
			}
			//�˳�����
			AppManager.getAppManager().AppExit(AppContext.getContext());
		}
	}
	
	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
	 * 
	 * @param ex
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//ʹ��Toast����ʾ�쳣��Ϣ
//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
		Toast.makeText(AppContext.getContext(), "�ܱ�Ǹ,��������쳣,�����˳�.", Toast.LENGTH_LONG).show();
//				Looper.loop();
//			}
//		}.start();
		if(isSendEmail){
			sendErrorInfoMail(ex);
		}
		//������־�ļ� 
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
//	      m.setSubject("������Ϣ"); 
//	      m.setBody(sb.toString()); 
//	      try { 
//		        //m.addAttachment("/sdcard/EcookPad/Log/exception_log20130226.txt"); 
//		        m.send();
//	      } catch(Exception e) {
//		        Log.e("AppError", "Could not send email", e); 
//	      } 
	}
	/**
	 * �����쳣��־
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
		//�ڿ���̨��ӡ
		System.err.println(sb.toString());
		
		String errorlog = "throwable_log"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//�ж��Ƿ������SD��
			String storageState = Environment.getExternalStorageState();		
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstants.WY_LOG_PATH;
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
