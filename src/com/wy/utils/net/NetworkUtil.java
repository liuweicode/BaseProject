/*
 * @(#)NetUtil.java		       Project:ProgramList
 * Date:2012-8-10
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
package com.wy.utils.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wy.utils.lang.StringUtils;

/** 
 * 描述：网络类
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-16
 */
public class NetworkUtil {

	public static final int NETTYPE_WIFI = 0x01;
	
	public static final int NETTYPE_CMWAP = 0x02;
	
	public static final int NETTYPE_CMNET = 0x03;
	
	/**
	 * 检测是否已经连接网络。
	 * 
	 * @param context
	 * @return 当且仅当连上网络时返回true,否则返回false。
	 */
	public static boolean isConnectingToInternet(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return (info != null) && info.isAvailable();
	}
	
    /**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	@SuppressLint("DefaultLocale")
	public int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
}
