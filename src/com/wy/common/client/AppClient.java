package com.wy.common.client;

/** 
 * ������
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-4-22
 */
public class AppClient {
	
	
	
	//public static final String HOST = "192.168.1.200";
	
	public static String appHost; 
	
	/*����ʧЧʱ��*/
	public static final int CACHE_TIME = 60*60000;
	/*�����ӳ���ȡ���ӵĳ�ʱʱ��*/
	public final static int TIMEOUT = 1000;
	/*���ӳ�ʱʱ��*/
	public final static int TIMEOUT_CONNECTION = 10*1000;//60*1000;
	/*����ʱ*/
	public final static int TIMEOUT_SOCKET = 10*1000;//60*1000;
	/*���Դ���*/
	public final static int RETRY_TIME = 2;
	/*�ַ�����*/
	protected final static String UTF_8 = "UTF-8";
	
}
