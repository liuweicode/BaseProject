/*
 * @(#)ApplicationUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-9-13
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wy.utils.apk;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.wy.AppContext;

/**
 * 应用工具类。
 * 
 * @author Geek_Soledad (66704238@51uc.com)
 */
public class ApplicationUtil {

	/**
	 * 通过包名获取应用程序的名称。
	 * 
	 * @param packageName 包名。
	 * @return 返回包名所对应的应用程序的名称。
	 */
	public static String getProgramNameByPackageName(String packageName) {
		PackageManager pm = AppContext.getContext().getPackageManager();
		String name = null;
		try {
			name = pm.getApplicationLabel(pm.getApplicationInfo(packageName,PackageManager.GET_META_DATA)).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	/**
	 * 获取有关本程序的信息。
	 * 
	 * @return 有关本程序的信息。
	 */
	public static ApkInfo getApkInfo() {
		ApkInfo apkInfo = new ApkInfo();
		ApplicationInfo applicationInfo = AppContext.getContext().getApplicationInfo();
		apkInfo.packageName = applicationInfo.packageName;
		apkInfo.iconId = applicationInfo.icon;
		apkInfo.iconDrawable = AppContext.getContext().getResources().getDrawable(apkInfo.iconId);
		apkInfo.programName = AppContext.getContext().getResources().getText(applicationInfo.labelRes).toString();
		PackageInfo packageInfo = null;
		try {
			packageInfo = AppContext.getContext().getPackageManager().getPackageInfo(apkInfo.packageName, 0);
			apkInfo.versionCode = packageInfo.versionCode;
			apkInfo.versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return apkInfo;
	}
}
