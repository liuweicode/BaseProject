package com.wy.common.client;

/** 
 * 描述：
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-4-22
 */
public class AppClient {
	
	
	
	//public static final String HOST = "192.168.1.200";
	
	public static String appHost; 
	
	/*缓存失效时间*/
	public static final int CACHE_TIME = 60*60000;
	/*从连接池中取连接的超时时间*/
	public final static int TIMEOUT = 1000;
	/*连接超时时间*/
	public final static int TIMEOUT_CONNECTION = 10*1000;//60*1000;
	/*请求超时*/
	public final static int TIMEOUT_SOCKET = 10*1000;//60*1000;
	/*重试次数*/
	public final static int RETRY_TIME = 2;
	/*字符编码*/
	protected final static String UTF_8 = "UTF-8";
	
}
