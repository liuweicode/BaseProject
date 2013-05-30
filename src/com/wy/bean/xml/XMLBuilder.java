package com.wy.bean.xml;

import java.io.InputStream;

import com.thoughtworks.xstream.mapper.CannotResolveClassException;

/** 
 * 描述：构建实体对象
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-5-30
 */
public abstract class XMLBuilder<T> {

	public abstract T build(InputStream input)throws CannotResolveClassException;
	
	public abstract T build(String xml)throws CannotResolveClassException;
}
