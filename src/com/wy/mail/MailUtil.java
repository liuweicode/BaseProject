package com.wy.mail;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.wy.AppConstants;
import com.wy.AppContext;
import com.wy.utils.FileUtils;
import com.wy.utils.apk.ApkInfo;
import com.wy.utils.apk.ApplicationUtil;
import com.wy.utils.net.NetworkUtil;

/** 
 * 描述：发送日志到指定邮件
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-6-3
 */
public class MailUtil {

	/**
	 * 发送错误日志邮件
	 */
	public static void sendErrorInfoMail(final Context context){
		  System.out.println("sendErrorInfoMail");
		  if(!NetworkUtil.isConnectingToInternet(context)){
			  Toast.makeText(context, "发送失败,请检查网络设置！！", Toast.LENGTH_SHORT).show();
			  return;
		  }
		  final Handler handler = new Handler(){
			  @Override
			public void handleMessage(Message msg) {
				 AppContext.getInstance().removeProperty(AppConstants.ISABNORMALEXIT);
				 Toast.makeText(context, "感谢您对本软件的支持！", Toast.LENGTH_SHORT).show();
			}
		  };
		  
	      new Thread(){
	    	  public void run() {
	    		  try { 
	  				
	  				//判断是否挂载了SD卡
	  				if(FileUtils.checkSDCardExists()){
	  					
	  					Mail m = new Mail("william@we-win.com.cn", "wy65047030"); 
	  				    m.setHost("smtp.exmail.qq.com");
	  				    m.setPort("25");
	  				    m.setSport("465");
	  				      
	  				    String[] toArr = {"i@liuwei.co"};
	  				    m.setTo(toArr); 
	  				    m.setFrom("william@we-win.com.cn"); 
	  				    m.setSubject("Android错误日志"); 
	  				    
		  				ApkInfo apkInfo = ApplicationUtil.getApkInfo();
		  				StringBuilder ua = new StringBuilder();
		  				ua.append("APP版本:").append(apkInfo.versionName+'/'+apkInfo.versionCode).append("\n");
		  				ua.append("API版本:").append(android.os.Build.VERSION.SDK_INT).append("\n");
		  				ua.append("系统版本:").append(android.os.Build.VERSION.RELEASE).append("\n");
		  				ua.append("手机型号:").append(android.os.Build.MODEL).append("\n");
	  				    m.setBody(ua.toString()); 
	  				    
	  				   //检查日志文件是否存在
	  				   String logDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + AppConstants.WY_LOG_PATH;
	  				   File logDir = new File(logDirPath);
	  				    if(logDir.exists() && logDir.isDirectory()){
	  						//如果存在  将日志文件作为附件发送邮件
	  				    	String[] logNames = logDir.list();
	  				    	for(String name : logNames){
	  				    		m.addAttachment(logDirPath + name);
	  				    	}
	  					}
	  				    if(m.send()){
	  			        	System.out.println("发送邮件成功");
	  			        	handler.sendEmptyMessage(0);
	  			        	if(FileUtils.deleteDirectory(logDirPath)){
	  			        		System.out.println("删除日志目录成功");
	  			        	}else{
	  			        		System.out.println("删除日志目录失败");
	  			        	}
	  			        }else{
	  			        	System.out.println("发送邮件失败");
	  			        }
	  				}
	  	      } catch(Exception e) {
	  		        Log.e("AppError", "Could not send email", e); 
	  	      } 
	    	  };
	      }.start();
	}
}
