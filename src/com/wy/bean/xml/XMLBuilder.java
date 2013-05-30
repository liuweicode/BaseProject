package com.wy.bean.xml;

import java.io.InputStream;

import com.thoughtworks.xstream.mapper.CannotResolveClassException;

/** 
 * ����������ʵ�����
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-5-30
 */
public abstract class XMLBuilder<T> {

	public abstract T build(InputStream input)throws CannotResolveClassException;
	
	public abstract T build(String xml)throws CannotResolveClassException;
}
