package com.wy.common.client.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.wy.AppContext;
import com.wy.common.client.AppClient;
import com.wy.common.client.IJsonAppClient;
import com.wy.exception.AppException;

/** 
 * ����������Json����
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-4-22
 */
public abstract class JsonAppClient extends AppClient implements IJsonAppClient{

	protected static final String TAG = "JsonAppClient";
	
	protected JsonAppClient() {
		
	}

	protected static String appCookie;
	
	protected static String appUserAgent;

	public static void cleanCookie() {
		appCookie = "";
	}
	
	protected static String getCookie(AppContext appContext) {
		if(appCookie == null || appCookie == "") {
			appCookie = appContext.getProperty("cookie");
		}
		return appCookie;
	}
	
	protected static String getUserAgent(AppContext appContext) {
		if(appUserAgent == null || appUserAgent == "") {
			StringBuilder ua = new StringBuilder("we-win.com.cn");
			ua.append('/'+appContext.getPackageInfo().versionName+'_'+appContext.getPackageInfo().versionCode);//App�汾
			ua.append("/Android");//�ֻ�ϵͳƽ̨
			ua.append("/"+android.os.Build.VERSION.RELEASE);//�ֻ�ϵͳ�汾
			ua.append("/"+android.os.Build.MODEL); //�ֻ��ͺ�
			//ua.append("/"+appContext.getAppId());//�ͻ���Ψһ��ʶ
			appUserAgent = ua.toString();
		}
		return appUserAgent;
	}
	
	protected static HttpClient getHttpClient() {        
        HttpClient httpClient = new HttpClient();
		// ���� HttpClient ���� Cookie,���������һ���Ĳ���
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        // ���� Ĭ�ϵĳ�ʱ���Դ������
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// ���� ���ӳ�ʱʱ��
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		// ���� �����ݳ�ʱʱ�� 
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIMEOUT_SOCKET);
		// ���� �ַ���
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}	
	
	protected static GetMethod getHttpGet(String url, String cookie, String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// ���� ����ʱʱ��
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", AppClient.appHost);
		httpGet.setRequestHeader("Connection","Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}
	
	protected static PostMethod getHttpPost(String url, String cookie, String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// ���� ����ʱʱ��
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", AppClient.appHost);
		httpPost.setRequestHeader("Connection","Keep-Alive");
		httpPost.setRequestHeader("Cookie", cookie);
		httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}
	
	protected static String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if(url.indexOf("?")<0)
			url.append('?');

		for(String name : params.keySet()){
			url.append('&');
			url.append(name);
			url.append('=');
			url.append(String.valueOf(params.get(name)));
			//����URLEncoder����
			//url.append(URLEncoder.encode(String.valueOf(params.get(name)), UTF_8));
		}

		return url.toString().replace("?&", "?");
	}
	
	/**
	 * get����URL
	 * @param url
	 * @throws IOException 
	 * @throws JSONException 
	 * @throws AppException 
	 */
	protected static JSONObject _GET(AppContext appContext, String url) throws IOException, JSONException {	
		
		System.out.println("_GET ���������Ϣ��ʼ");
		System.out.println(url);
		System.out.println("_GET ���������Ϣ����");
		
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		
		HttpClient httpClient = null;
		GetMethod httpGet = null;

		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, cookie, userAgent);			
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpException("����["+url+"]ʧ��,���������Ӧ�� statusCode = " + statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				System.out.println("return data =====>"+responseBody);
				break;				
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// �����������쳣��������Э�鲻�Ի��߷��ص�����������
				throw new HttpException("����["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// ���������쳣
				throw new IOException("����["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} finally {
				// �ͷ�����
				httpGet.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		
		//responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
//		if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
//			try {
//				Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
//				if(res.getErrorCode() == 0){
//					appContext.Logout();
//					appContext.getUnLoginHandler().sendEmptyMessage(1);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}			
//		}
		return new JSONObject(responseBody);
	}
	
	/**
	 * ����post����
	 * 
	 * @param appContext
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	protected static JSONObject _POST(AppContext appContext, String url, Map<String, Object> params, Map<String,File> files) throws IOException, JSONException {
		System.out.println("_POST ���������Ϣ��ʼ");
		System.out.println(url);
		System.out.println(params);
		System.out.println(files);
		System.out.println("_POST ���������Ϣ����");
		
		String cookie = getCookie(appContext);
		String userAgent = getUserAgent(appContext);
		
		HttpClient httpClient = null;
		PostMethod httpPost = null;
		
		//post����������
		int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
        if(params != null)
        for(String name : params.keySet()){
        	parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
        	//System.out.println("post_key==> "+name+"    value==>"+String.valueOf(params.get(name)));
        }
        if(files != null)
        for(String file : files.keySet()){
        	try {
				parts[i++] = new FilePart(file, files.get(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
        	//System.out.println("post_key_file==> "+file);
        }
		
		String responseBody = "";
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpPost = getHttpPost(url, cookie, userAgent);	        
		        httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));	
		        int statusCode = httpClient.executeMethod(httpPost);
		        if(statusCode != HttpStatus.SC_OK) 
		        {
		        	throw new HttpException("����URL:"+url+" ������"+params+"ʧ��,���������Ӧ�� statusCode = " + statusCode);
		        }
		        else if(statusCode == HttpStatus.SC_OK) 
		        {
		            Cookie[] cookies = httpClient.getState().getCookies();
		            String tmpcookies = "";
		            for (Cookie ck : cookies) {
		                tmpcookies += ck.toString()+";";
		            }
		            //����cookie   
	        		if(appContext != null && tmpcookies != ""){
	        			appContext.setProperty("cookie", tmpcookies);
	        			appCookie = tmpcookies;
	        		}
		        }
		     	responseBody = httpPost.getResponseBodyAsString();
		     	httpPost.getResponseBody();
		     	httpPost.getResponseContentLength();
		     	System.out.println("return data =====>"+responseBody);
		     	break;	     	
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// �����������쳣��������Э�鲻�Ի��߷��ص�����������
				throw new HttpException("����["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// ���������쳣
				throw new IOException("����["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} finally {
				// �ͷ�����
				httpPost.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
        
//        responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
//		if(responseBody.contains("result") && responseBody.contains("errorCode") && appContext.containsProperty("user.uid")){
//			try {
//				Result res = Result.parse(new ByteArrayInputStream(responseBody.getBytes()));	
//				if(res.getErrorCode() == 0){
//					appContext.Logout();
//					appContext.getUnLoginHandler().sendEmptyMessage(1);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}			
//		}
        return new JSONObject(responseBody);
	}
	
	/**
	 * ��ȡ����ͼƬ
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	public static Bitmap getNetBitmap(String url) throws IOException {
		//System.out.println("image_url==> "+url);
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		Bitmap bitmap = null;
		int time = 0;
		do{
			try 
			{
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, null, null);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpException("����ͼƬURL:"+url+"ʧ��,���������Ӧ�� statusCode = " + statusCode);
				}
		        InputStream inStream = httpGet.getResponseBodyAsStream();
		        bitmap = BitmapFactory.decodeStream(inStream);
		        inStream.close();
		        break;
			} catch (HttpException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// �����������쳣��������Э�鲻�Ի��߷��ص�����������
				throw new HttpException("����["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} catch (IOException e) {
				time++;
				if(time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {} 
					continue;
				}
				// ���������쳣
				throw new IOException("����ͼƬURL:"+url+"ʧ��,���������쳣,������Ϣ��"+e.getMessage());
			} finally {
				// �ͷ�����
				httpGet.releaseConnection();
				httpClient = null;
			}
		}while(time < RETRY_TIME);
		return bitmap;
	}

}
