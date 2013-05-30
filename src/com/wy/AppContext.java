package com.wy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.wy.exception.AppError;
import com.wy.utils.lang.StringUtils;
import com.wy.widget.RemoteImageView.ImageCache;

/** 
 * 描述：应用程序上下文 保存全局属性
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-16
 */
public class AppContext extends Application{
	
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	private static AppContext instance;
	protected static Context appContext;
	
	@Override
	public void onCreate(){
		instance = this;
		super.onCreate();
		appContext = getApplicationContext();
		
		mImageCache = new ImageCache();
		
		//当程序发生Uncaught异常时捕获
		AppError appCrashHandler = new AppError();
		appCrashHandler.initUncaught();
	}
	
	private ImageCache mImageCache;
	
	public ImageCache getImageCache() {
		return mImageCache;
	}
	
	public static AppContext getInstance(){
		return instance;
	}
	
	public static Context getContext(){
		return appContext;
	}
	
	/**
	 * 获取App唯一标识
	 * @return
	 */
	public String getAppId() {
		String uniqueID = getProperty(AppConstants.CONF_APP_UNIQUEID);
		if(StringUtils.isEmpty(uniqueID)){
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConstants.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}
    
    
	/**
	 * 判断缓存数据是否可读
	 * @param cachefile
	 * @return
	 */
	public boolean isReadDataCache(String cachefile)
	{
		return readObject(cachefile) != null;
	}
	
	/**
	 * 保存对象
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try{
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			try {
				oos.close();
			} catch (Exception e) {}
			try {
				fos.close();
			} catch (Exception e) {}
		}
	}
	
	/**
	 * 读取对象
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file){
		if(!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try{
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable)ois.readObject();
		}catch(FileNotFoundException e){
		}catch(Exception e){
			e.printStackTrace();
			//反序列化失败 - 删除缓存文件
			if(e instanceof InvalidClassException){
				File data = getFileStreamPath(file);
				data.delete();
			}
		}finally{
			try {
				ois.close();
			} catch (Exception e) {}
			try {
				fis.close();
			} catch (Exception e) {}
		}
		return null;
	}
	
	/**
	 * 判断缓存是否存在
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile)
	{
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if(data.exists())
			exist = true;
		return exist;
	}
	
	public boolean containsProperty(String key){
		Properties props = getProperties();
		 return props.containsKey(key);
	}
	
	public void setProperties(Properties ps){
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties(){
		return AppConfig.getAppConfig(this).get();
	}
	
	public void setProperty(String key,String value){
		AppConfig.getAppConfig(this).set(key, value);
	}
	
	public String getProperty(String key){
		return AppConfig.getAppConfig(this).get(key);
	}
	
	public int getIntProperty(String key){
		String value = getProperty(key);
		try{
			return Integer.parseInt(value);
		}catch (Exception e) {
			return 0;
		}
	}
	
	public void removeProperty(String...key){
		AppConfig.getAppConfig(this).remove(key);
	}
	
	/**
	 * 获取App安装包信息
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try { 
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {    
			e.printStackTrace(System.err);
		} 
		if(info == null) info = new PackageInfo();
		return info;
	}
	
}
