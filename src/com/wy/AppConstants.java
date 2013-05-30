package com.wy;

import com.wy.utils.StringUtils;
import com.wy.widget.AppToast;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/** 
 * ������Ӧ�ó�����
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-1-16
 */
public abstract class AppConstants {

	public static final int PAGE_SIZE = 20;//Ĭ�Ϸ�ҳ��С
	
	public static String WY_UPDATE_APK_PATH = "/WY/Update/";//�������Ĭ�ϱ���Ŀ¼
	
	public static String WY_LOG_PATH = "/WY/Log/";//��־Ĭ�ϱ���Ŀ¼
	
	static{
		try {
			ApplicationInfo appInfo = AppContext.getContext().getPackageManager().getApplicationInfo(AppContext.getContext().getPackageName(),PackageManager.GET_META_DATA);
			String updatePath = appInfo.metaData.getString("wy_update_apk_path");
			String logPath = appInfo.metaData.getString("wy_log_path");
			
			if(StringUtils.isNotBlank(updatePath) && (!"null".equals(updatePath))){
				WY_UPDATE_APK_PATH = updatePath;
			}else{
				AppToast.makeText(AppContext.getContext(), "����AndroidManifest.xml�ļ����������������Ŀ¼��", AppToast.SOUND_ERROR).show();
			}
			if(StringUtils.isNotBlank(logPath) && (!"null".equals(logPath))){
				WY_LOG_PATH = logPath;
			}else{
				AppToast.makeText(AppContext.getContext(), "����AndroidManifest.xml�ļ�������־����Ŀ¼��", AppToast.SOUND_ERROR).show();
			}
			System.out.println("�����������Ŀ¼:"+WY_UPDATE_APK_PATH);
			System.out.println("��־����Ŀ¼:"+WY_LOG_PATH);
		} catch (NameNotFoundException e) {
			AppToast.makeText(AppContext.getContext(), "����AndroidManifest.xml�ļ�����Ŀ¼��Ϣ��", AppToast.SOUND_ERROR).show();
		}
		
	}
}
