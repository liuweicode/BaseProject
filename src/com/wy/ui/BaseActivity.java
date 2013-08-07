package com.wy.ui;

import android.app.Activity;
import android.os.Bundle;

import com.wy.utils.ui.ActivityTaskManager;

/** 
 * 描述：Activity基类
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-16
 */
public abstract class BaseActivity extends Activity{

	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//添加Activity到堆栈
		ActivityTaskManager.getInstance().putActivity(LOG_TAG, this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//结束Activity&从堆栈中移除
		ActivityTaskManager.getInstance().removeActivity(LOG_TAG);
	}
	
}
