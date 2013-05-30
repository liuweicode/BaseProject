package com.wy.net;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import com.wy.AppContext;
import com.wy.net.AppClient;

/**
 * 描述：客户端请求封装
 * 
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-30
 */
public abstract class XmlAppClient extends AppClient{

	/**
	 * get请求URL
	 * 
	 * @param url
	 * @throws Exception
	 */
	protected InputStream _GET(AppContext appContext,String url) throws HttpException, IOException {
		System.out.println("get_url==> " + url);
		
		String cookie = getCookie();
		String userAgent = getUserAgent();
		
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpException("请求[" + url + "]失败,网络错误，响应码 statusCode = " + statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				System.out.println("XMLDATA=====>" + responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				throw new HttpException("请求[" + url + "]失败,错误信息："
						+ e.getMessage());
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				throw new IOException("请求[" + url + "]失败,错误信息："
						+ e.getMessage());
			} finally {
				// 释放连接
				if (httpGet != null) {
					httpGet.releaseConnection();
					httpClient = null;
				}
			}
		} while (time < RETRY_TIME);
		// responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return new ByteArrayInputStream(responseBody.getBytes());
	}

	/**
	 * 公用post方法
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	protected InputStream _POST(AppContext appContext,String url, Map<String, Object> params,
			Map<String, File> files) throws HttpException, IOException {
		System.out.println("_POST 请求参数信息开始");
		System.out.println(url);
		System.out.println(params);
		System.out.println(files);
		System.out.println("_POST 请求参数信息结束");
		
		String cookie = getCookie();
		String userAgent = getUserAgent();

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		// post表单参数处理
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params
						.get(name)), UTF_8);
				// System.out.println("post_key==> " + name + "    value==>"+
				// String.valueOf(params.get(name)));
			}
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				// System.out.println("post_key_file==> "+file);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpException("请求URL:" + url + " 参数：" + params + "失败,网络错误，响应码 statusCode = " + statusCode);
				} else if (statusCode == HttpStatus.SC_OK) {
					Cookie[] cookies = httpClient.getState().getCookies();
					String tmpcookies = "";
					for (Cookie ck : cookies) {
						tmpcookies += ck.toString() + ";";
					}
					// 保存cookie
					if (appContext != null && tmpcookies != "") {
						appContext.setProperty(COOKIE, tmpcookies);
						appCookie = tmpcookies;
					}
				}
				responseBody = httpPost.getResponseBodyAsString();
				System.out.println("XMLDATA=====>" + responseBody);
				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				throw new HttpException("请求URL:" + url + " 参数：" + params
						+ "失败,错误信息：" + e.getMessage());
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				throw new IOException("请求URL:" + url + " 参数：" + params
						+ "失败,错误信息：" + e.getMessage());
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		// responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return new ByteArrayInputStream(responseBody.getBytes());
	}

}
