package com.wy.common.client;

import com.wy.bean.Update;


/** 
 * �������ͻ�������ӿ�
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-1-30
 */
public interface IJsonAppClient {
	
	
	public void setHost();
	
	/**
	 * �汾����
	 * 
	 * @return
	 * @throws Exception
	 */
	public Update checkVersion() throws Exception;
}
