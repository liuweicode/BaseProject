package com.wy.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wy.R;

/** 
 * 描述：自定义Toast 可以播放声音 可以自定义显示位置
 *
 * 作者: Liu wei
 * 
 * 邮箱：i@liuwei.co
 * 
 * 创建时间: 2013-1-21
 */
public class AppToast extends Toast{
	
	/*显示声音*/
	public static final int SOUND_NO = -1;
	public static final int SOUND_1 = R.raw.sound1;
	public static final int SOUND_2 = R.raw.sound2;
	public static final int SOUND_ERROR = R.raw.error;
	
	/*显示位置*/
	public static final int POSITION_TOP = Gravity.TOP;
	public static final int POSITION_CENTER = Gravity.CENTER;
	public static final int POSITION_BOTTOM = Gravity.BOTTOM;
	
	protected MediaPlayer mPlayer;

	protected boolean isSound;
	
	/*默认不显示声音*/
	public AppToast(Context context) {
		this(context,SOUND_NO);
	}
	
	/*如果soundSource等于SOUND_NO 则不播放声音*/
	public AppToast(Context context,int soundSource) {
		super(context);
		if(soundSource == SOUND_NO){
			this.isSound = false;
		}else{
			this.isSound = true;
			mPlayer = MediaPlayer.create(context, soundSource);
	        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
				}        	
	        });
		}
    }

	@Override
	public void show() {
		super.show();
		
		if(isSound){
			mPlayer.start();
		}
	}
	
	/**
	 * 获取自定义Toast实例 默认显示在上方 偏移量为0
	 * 
	 * @param context 
	 * @param text 提示消息
	 * @param soundSource 播放声音的资源 目前支持 SOUND_1  SOUND_2  如果不需要播放声音则为SOUND_NO
	 * @param time 提示消息显示的时间
	 * @param position 提示消息显示的位置
	 * @param xOffset 提示消息显示的位置X轴偏移量
	 * @param yOffset 提示消息显示的位置Y轴偏移量
	 * @return
	 */
	public static AppToast makeText(Context context, CharSequence text, int soundSource) {
        return makeText(context, text, soundSource, 600, POSITION_TOP,0,0);
    }
	/**
	 * 获取自定义Toast实例 默认偏移量为0
	 * 
	 * @param context 
	 * @param text 提示消息
	 * @param soundSource 播放声音的资源 目前支持 SOUND_1  SOUND_2  如果不需要播放声音则为SOUND_NO
	 * @param time 提示消息显示的时间
	 * @param position 提示消息显示的位置
	 * @return
	 */
	public static AppToast makeText(Context context, CharSequence text, int soundSource,int time) {
		return makeText(context, text, soundSource, time, POSITION_TOP,0,0);
	}
	
	/**
	 * 获取自定义Toast实例
	 * 
	 * @param context 
	 * @param text 提示消息
	 * @param soundSource 播放声音的资源 目前支持 SOUND_1  SOUND_2  如果不需要播放声音则为SOUND_NO
	 * @param time 提示消息显示的时间
	 * @param position 提示消息显示的位置
	 * @param xOffset 提示消息显示的位置X轴偏移量
	 * @param yOffset 提示消息显示的位置Y轴偏移量
	 * @return
	 */
	public static AppToast makeText(Context context, CharSequence text, int soundSource,int time,int position,int xOffset, int yOffset) {
		AppToast result = new AppToast(context, soundSource);
		LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		
		View v = inflate.inflate(R.layout.app_toast, null);
		v.setMinimumWidth(dm.widthPixels);//设置控件最小宽度为手机屏幕宽度
		TextView tv = (TextView)v.findViewById(R.id.app_toast_message);
		tv.setText(text);
		
		result.setView(v);
		result.setDuration(time);
		result.setGravity(position, xOffset, yOffset);
		
		return result;
	}
	
}