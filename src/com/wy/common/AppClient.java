package com.wy.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * �������ͻ��������װ
 * 
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-1-30
 */
public abstract class AppClient implements IAppClient{

	/*����ʧЧʱ��*/
	public static final int CACHE_TIME = 60*60000;
	/*�����ӳ���ȡ���ӵĳ�ʱʱ��*/
	public final static int TIMEOUT = 1000;
	/*���ӳ�ʱʱ��*/
	public final static int TIMEOUT_CONNECTION = 20000;//60*1000;
	/*����ʱ*/
	public final static int TIMEOUT_SOCKET = 20000;//60*1000;
	/*���Դ���*/
	public final static int RETRY_TIME = 3;
	/*�ַ�����*/
	protected final static String UTF_8 = "UTF-8";
	
	protected AppClient() {
	}

	protected HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();
		// ���� HttpClient ���� Cookie,���������һ���Ĳ���
		httpClient.getParams().setCookiePolicy(
				CookiePolicy.BROWSER_COMPATIBILITY);
		// ���� Ĭ�ϵĳ�ʱ���Դ������
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		// ���� ���ӳ�ʱʱ��
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(TIMEOUT_CONNECTION);
		// ���� �����ݳ�ʱʱ��
		httpClient.getHttpConnectionManager().getParams()
				.setSoTimeout(TIMEOUT_SOCKET);
		// ���� �ַ���
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	protected GetMethod getHttpGet(String url) {
		GetMethod httpGet = new GetMethod(url);
		// ���� ����ʱʱ��
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Host", getHost());
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		return httpGet;
	}

	protected PostMethod getHttpPost(String url) {
		PostMethod httpPost = new PostMethod(url);
		// ���� ����ʱʱ��
		httpPost.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpPost.setRequestHeader("Host", getHost());
		httpPost.setRequestHeader("Connection", "Keep-Alive");
		return httpPost;
	}

	protected String _MakeURL(String p_url, Map<String, Object> params) {
		StringBuilder url = new StringBuilder(p_url);
		if (url.indexOf("?") < 0)
			url.append('?');

		if(params!=null){
			for (String name : params.keySet()) {
				url.append('&');
				url.append(name);
				url.append('=');
				url.append(String.valueOf(params.get(name)));
				// ����URLEncoder����
				// url.append(URLEncoder.encode(String.valueOf(params.get(name)),
				// UTF_8));
			}
		}
		return url.toString().replace("?&", "?");
	}

	/**
	 * get����URL
	 * 
	 * @param url
	 * @throws Exception
	 */
	protected InputStream http_get(String url) throws HttpException,
			IOException {
		System.out.println("get_url==> "+url);
		HttpClient httpClient = null;
		GetMethod httpGet = null;
		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpException("����["+url+"]ʧ��,���������Ӧ�� statusCode = " + statusCode);
				}
				responseBody = httpGet.getResponseBodyAsString();
				System.out.println("XMLDATA=====>"+responseBody);
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
				// �����������쳣��������Э�鲻�Ի��߷��ص�����������
				throw new HttpException("����["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ���������쳣
				throw new IOException("����["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} finally {
				// �ͷ�����
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
	 * ����post����
	 * 
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	protected InputStream _post(String url, Map<String, Object> params,
			Map<String, File> files) throws HttpException, IOException {
		System.out.println("post_url==> "+url);
		System.out.println("post_params==> "+params);

		HttpClient httpClient = null;
		PostMethod httpPost = null;

		// post����������
		int length = (params == null ? 0 : params.size())
				+ (files == null ? 0 : files.size());
		Part[] parts = new Part[length];
		int i = 0;
		if (params != null)
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
				//System.out.println("post_key==> " + name + "    value==>"+ String.valueOf(params.get(name)));
			}
		if (files != null)
			for (String file : files.keySet()) {
				try {
					parts[i++] = new FilePart(file, files.get(file));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
//				 System.out.println("post_key_file==> "+file);
			}

		String responseBody = "";
		int time = 0;
		do {
			try {
				httpClient = getHttpClient();
				httpPost = getHttpPost(url);
				httpPost.setRequestEntity(new MultipartRequestEntity(parts,httpPost.getParams()));
				int statusCode = httpClient.executeMethod(httpPost);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpException("����URL:"+url+" ������"+params+"ʧ��,���������Ӧ�� statusCode = " + statusCode);
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
				// �����������쳣��������Э�鲻�Ի��߷��ص�����������
				throw new HttpException("����URL:"+url+" ������"+params+"ʧ��,������Ϣ��"+e.getMessage());
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ���������쳣
				throw new IOException("����URL:"+url+" ������"+params+"ʧ��,������Ϣ��"+e.getMessage());
			} finally {
				// �ͷ�����
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		// responseBody = responseBody.replaceAll("\\p{Cntrl}", "");
		return new ByteArrayInputStream(responseBody.getBytes());
	}

	/**
	 * ��ȡ����ͼƬ
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
				httpGet = getHttpGet(url);
				int statusCode = httpClient.executeMethod(httpGet);
				if (statusCode != HttpStatus.SC_OK) {
					throw new HttpException("��ȡͼƬ["+url+"]ʧ��,���������Ӧ�� statusCode = " + statusCode);
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
				// �����������쳣��������Э�鲻�Ի��߷��ص�����������
				throw new HttpException("��ȡͼƬ["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// ���������쳣
				throw new IOException("��ȡͼƬ["+url+"]ʧ��,������Ϣ��"+e.getMessage());
			} finally {
				// �ͷ�����
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return bitmap;
	}
}
