package com.wy.common.client;

import com.wy.bean.Update;


/** 
 * 描述：客户端请求接口
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-30
 */
public interface IJsonAppClient {
	
	
	public void setHost();
	
	/**
	 * 版本更新
	 * 
	 * @return
	 * @throws Exception
	 */
	public Update checkVersion() throws Exception;
}
