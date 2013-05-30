package com.wy.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wy.AppContext;
import com.wy.utils.apk.ApkInfo;
import com.wy.utils.apk.ApplicationUtil;

/**
 * 描述：客户端GET POST请求封装
 * 
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-30
 */
public abstract class AppClient{

	/*连接超时时间*/
	public final static int TIMEOUT_CONNECTION = 20000;
	/*请求超时*/
	public final static int TIMEOUT_SOCKET = 20000;
	/*重试次数*/
	public final static int RETRY_TIME = 3;
	/*字符编码*/
	protected final static String UTF_8 = "UTF-8";
	
	public final static String CONF_COOKIE = "cookie";
	
	/**
	 * 获取请求的主机
	 * 
	 * @return
	 */
	public abstract String getHost();
	
	protected AppClient() {
	}
	
	protected static String appCookie;
	
	protected static String appUserAgent;

	public static void cleanCookie() {
		appCookie = "";
	}
	
	protected String getCookie() {
		if(appCookie == null || appCookie == "") {
			appCookie = AppContext.getInstance().getProperty(CONF_COOKIE);
		}
		return appCookie;
	}
	
	protected String getUserAgent() {
		if(appUserAgent == null || appUserAgent == "") {
			ApkInfo apkInfo = ApplicationUtil.getApkInfo();
			StringBuilder ua = new StringBuilder("WEI_WIN");
			ua.append('/'+apkInfo.versionName+'_'+apkInfo.versionCode);//App版本
			ua.append("/Android");//手机系统平台
			ua.append("/"+android.os.Build.VERSION.RELEASE);//手机系统版本
			ua.append("/"+android.os.Build.MODEL); //手机型号
			ua.append("/"+AppContext.getInstance().getAppId());//客户端唯一标识
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}

	protected HttpClient getHttpClient() {        
        HttpClient httpClient = new HttpClient();
		// 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // 设置 默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 设置 连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		// 设置 读数据超时时间 
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
		// 设置 字符集
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}	
	
	protected GetMethod getHttpGet(String url, String cookie, String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", getHost());
		httpGet.setRequestHeader("Connection","Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}
	
	protected PostMethod getHttpPost(String url, String cookie, String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// 设置 请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", getHost());
		httpPost.setRequestHeader("Connection","Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}
	
	protected String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//不做URLEncoder处理
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}

		return url.toString().replace("?&", "?");
	}

	/**
	 * 获取网络图片
	 * 
	 * @param url
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Bitmap getNetBitmap(String url)throws HttpException, IOException{
		System.out.println("image_url==> "+url);
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url,null,null);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpException("获取图片["+url+"]失败,网络错误，响应码 statusCode = " + statusCode);
				}
				InputStream inStream = httpGet.getResponseBodyAsStream();
				bitmap = BitmapFactory.decodeStream(inStream);
				inStream.close();
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
				throw new HttpException("获取图片["+url+"]失败,错误信息："+e.getMessage());
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
				throw new IOException("获取图片["+url+"]失败,错误信息："+e.getMessage());
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}
	
}
