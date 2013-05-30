package com.wy.common;

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
public interface IAppClient {
	
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
