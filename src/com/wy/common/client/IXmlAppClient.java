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
public interface IXmlAppClient {
	
	/**
	 * ��ȡ���������
	 * 
	 * @return
	 */
	public String getHost();
	
	/**
	 * �汾����
	 * 
	 * @return
	 * @throws Exception
	 */
	public Update checkVersion() throws Exception;
}
