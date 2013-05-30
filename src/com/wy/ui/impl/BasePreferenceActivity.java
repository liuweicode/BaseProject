package com.wy.ui.impl;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.wy.ui.IBasePreferenceActivity;
import com.wy.utils.ui.ActivityTaskManager;

/** 
 * 描述：Activity基类
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-28
 */
public abstract class BasePreferenceActivity extends PreferenceActivity implements IBasePreferenceActivity{

	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//添加Activity到堆栈
		ActivityTaskManager.getInstance().putActivity(LOG_TAG, this);
		//加载界面配置
		setContentView();
		//初始化界面控件
		initView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//结束Activity&从堆栈中移除
		ActivityTaskManager.getInstance().removeActivity(LOG_TAG);
	}
	
}
