package com.wy.bean.json;

import org.json.JSONException;
import org.json.JSONObject;

/** 
 * 描述：通过JSONObject构建实体对象
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-5-30
 */
public abstract class JSONBuilder<T> {
	
	/**
	 * 子类实现build方法创建实体
	 * 
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	public abstract T build(JSONObject jsonObject) throws JSONException;
}
