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
 * �������Զ���Toast ���Բ������� �����Զ�����ʾλ��
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-1-21
 */
public class AppToast extends Toast{
	
	/*��ʾ����*/
	public static final int SOUND_NO = -1;
	public static final int SOUND_1 = R.raw.sound1;
	public static final int SOUND_2 = R.raw.sound2;
	public static final int SOUND_ERROR = R.raw.error;
	
	/*��ʾλ��*/
	public static final int POSITION_TOP = Gravity.TOP;
	public static final int POSITION_CENTER = Gravity.CENTER;
	public static final int POSITION_BOTTOM = Gravity.BOTTOM;
	
	protected MediaPlayer mPlayer;

	protected boolean isSound;
	
	/*Ĭ�ϲ���ʾ����*/
	public AppToast(Context context) {
		this(context,SOUND_NO);
	}
	
	/*���soundSource����SOUND_NO �򲻲�������*/
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
	 * ��ȡ�Զ���Toastʵ�� Ĭ����ʾ���Ϸ� ƫ����Ϊ0
	 * 
	 * @param context 
	 * @param text ��ʾ��Ϣ
	 * @param soundSource ������������Դ Ŀǰ֧�� SOUND_1  SOUND_2  �������Ҫ����������ΪSOUND_NO
	 * @param time ��ʾ��Ϣ��ʾ��ʱ��
	 * @param position ��ʾ��Ϣ��ʾ��λ��
	 * @param xOffset ��ʾ��Ϣ��ʾ��λ��X��ƫ����
	 * @param yOffset ��ʾ��Ϣ��ʾ��λ��Y��ƫ����
	 * @return
	 */
	public static AppToast makeText(Context context, CharSequence text, int soundSource) {
        return makeText(context, text, soundSource, 600, POSITION_TOP,0,0);
    }
	/**
	 * ��ȡ�Զ���Toastʵ�� Ĭ��ƫ����Ϊ0
	 * 
	 * @param context 
	 * @param text ��ʾ��Ϣ
	 * @param soundSource ������������Դ Ŀǰ֧�� SOUND_1  SOUND_2  �������Ҫ����������ΪSOUND_NO
	 * @param time ��ʾ��Ϣ��ʾ��ʱ��
	 * @param position ��ʾ��Ϣ��ʾ��λ��
	 * @return
	 */
	public static AppToast makeText(Context context, CharSequence text, int soundSource,int time) {
		return makeText(context, text, soundSource, time, POSITION_TOP,0,0);
	}
	
	/**
	 * ��ȡ�Զ���Toastʵ��
	 * 
	 * @param context 
	 * @param text ��ʾ��Ϣ
	 * @param soundSource ������������Դ Ŀǰ֧�� SOUND_1  SOUND_2  �������Ҫ����������ΪSOUND_NO
	 * @param time ��ʾ��Ϣ��ʾ��ʱ��
	 * @param position ��ʾ��Ϣ��ʾ��λ��
	 * @param xOffset ��ʾ��Ϣ��ʾ��λ��X��ƫ����
	 * @param yOffset ��ʾ��Ϣ��ʾ��λ��Y��ƫ����
	 * @return
	 */
	public static AppToast makeText(Context context, CharSequence text, int soundSource,int time,int position,int xOffset, int yOffset) {
		AppToast result = new AppToast(context, soundSource);
		LayoutInflater inflate = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		
		View v = inflate.inflate(R.layout.app_toast, null);
		v.setMinimumWidth(dm.widthPixels);//���ÿؼ���С���Ϊ�ֻ���Ļ���
		TextView tv = (TextView)v.findViewById(R.id.app_toast_message);
		tv.setText(text);
		
		result.setView(v);
		result.setDuration(time);
		result.setGravity(position, xOffset, yOffset);
		
		return result;
	}
	
}