package com.wy.bean;

/** 
 * �������汾����
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-1-29
 */
public class Update{

	protected int versionCode;//�汾��
	
	protected String versionName;//�汾����
	
	protected String downloadURL;//���ص�ַ
	
	protected String publishTime;//����ʱ��
	
	protected String versionDesc;//�汾����

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadURL() {
		return downloadURL;
	}

	public void setDownloadURL(String downloadURL) {
		this.downloadURL = downloadURL;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}
	
}
