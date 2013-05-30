package com.wy.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wy.R;

/** 
 * ������ʹ��PopupWindow�Զ���Toast ��ʾ����Ч��
 *
 * ����: Liu wei
 * 
 * ���䣺i@liuwei.co
 * 
 * ����ʱ��: 2013-1-24
 */
@Deprecated
public class PopupWindowToast {

	private static MediaPlayer mPlayer;
	private static boolean isSound = false;
	
	private static PopupWindow mPopupWindow;
	
	private PopupWindowToast(){
	}
	
	private static void initMediaPlayer(Context context){
		mPlayer = MediaPlayer.create(context, R.raw.sound1);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener(){
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}        	
        });	
	}
	
	private static long endTime = System.currentTimeMillis();
	
	private static final int SHOWTIME = 2000;
	
	public static void makeToast(Activity activity, String text, boolean isSound){
		
		if(mPopupWindow == null){
			View layout = LayoutInflater.from(activity).inflate(R.layout.popup_window_toast, null);
			TextView tv = (TextView)layout.findViewById(R.id.textView1);
			tv.setText(text);
			//���õ������ֺ������С
	    	mPopupWindow = new PopupWindow(layout,LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			//���ö�������Ч��
			mPopupWindow.setAnimationStyle(R.style.dialogWindowAnim);
			// ���ð�͸����ɫ
			ColorDrawable dw = new ColorDrawable(0x7DC0C0C0);
			mPopupWindow.setBackgroundDrawable(dw);
			mPopupWindow.setClippingEnabled(true);
		}else{
			View view = mPopupWindow.getContentView();
			TextView tv = (TextView)view.findViewById(R.id.textView1);
			tv.setText(text);
			endTime = System.currentTimeMillis() + SHOWTIME;
		}
		
		if(isSound){
			initMediaPlayer(activity);
			if(!mPlayer.isPlaying()){
				mPlayer.start();
			}
		}
		mPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.RIGHT | Gravity.TOP,0, 35);
		
		final Handler hander = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what==1){
					 Log.d("PopupWindowToast", "�ر�PopupWindowToast");
				      if(mPopupWindow!=null && mPopupWindow.isShowing()){
			    			mPopupWindow.dismiss();
			    	  }
				}
			}
		};
		
		new Thread(){
			@Override
			public void run() {
				endTime = System.currentTimeMillis() + SHOWTIME;
			      while (System.currentTimeMillis() < endTime) {
			          synchronized (this) {
			              try {
			                  wait(endTime - System.currentTimeMillis());
			              } catch (Exception e) {
			              }
			          }
			      }
			      hander.sendEmptyMessage(1);
			}
		}.start();
		
	}
	
}
