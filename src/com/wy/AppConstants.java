package com.wy;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.wy.utils.lang.StringUtils;
import com.wy.widget.AppToast;

/** 
 * 描述：应用程序常量
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-16
 */
public abstract class AppConstants {

	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";//App唯一标识
	
	public static final int PAGE_SIZE = 20;//默认分页大小
	
	public static String WY_UPDATE_APK_PATH = "/WY/Update/";//软件更新默认保存目录
	
	public static String WY_LOG_PATH = "/WY/Log/";//日志默认保存目录
	
	static{
		try {
			ApplicationInfo appInfo = AppContext.getContext().getPackageManager().getApplicationInfo(AppContext.getContext().getPackageName(),PackageManager.GET_META_DATA);
			String updatePath = appInfo.metaData.getString("wy_update_apk_path");
			String logPath = appInfo.metaData.getString("wy_log_path");
			
			if(StringUtils.isNotBlank(updatePath) && (!"null".equals(updatePath))){
				WY_UPDATE_APK_PATH = updatePath;
			}else{
				AppToast.makeText(AppContext.getContext(), "请在AndroidManifest.xml文件配置软件更新下载目录！", AppToast.SOUND_ERROR).show();
			}
			if(StringUtils.isNotBlank(logPath) && (!"null".equals(logPath))){
				WY_LOG_PATH = logPath;
			}else{
				AppToast.makeText(AppContext.getContext(), "请在AndroidManifest.xml文件配置日志保存目录！", AppToast.SOUND_ERROR).show();
			}
			System.out.println("软件更新下载目录:"+WY_UPDATE_APK_PATH);
			System.out.println("日志保存目录:"+WY_LOG_PATH);
		} catch (NameNotFoundException e) {
			AppToast.makeText(AppContext.getContext(), "请在AndroidManifest.xml文件配置目录信息！", AppToast.SOUND_ERROR).show();
		}
	}
	
}
