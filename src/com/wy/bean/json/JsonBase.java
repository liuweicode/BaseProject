package com.wy.bean.json;

import com.wy.bean.Base;

/** 
 * 描述：json实体基类
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * public abstract class JsonBase<T> extends Base{

	private static final long serialVersionUID = -7772034754491117557L;

	public abstract T build(JSONObject jsonObject) throws JSONException;
   }
 * 
 * 创建时间: 2013-2-1
 */
public abstract class JsonBase extends Base{

	private static final long serialVersionUID = -7772034754491117557L;

}
