package com.wy.bean.json;

import org.json.JSONException;
import org.json.JSONObject;

/** 
 * ������ͨ��JSONObject����ʵ�����
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-5-30
 */
public abstract class JSONBuilder<T> {
	
	/**
	 * ����ʵ��build��������ʵ��
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public abstract T build(JSONObject jsonObject) throws JSONException;
}
