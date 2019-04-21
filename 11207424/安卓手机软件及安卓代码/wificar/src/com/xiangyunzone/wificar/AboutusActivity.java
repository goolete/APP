package com.xiangyunzone.wificar;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.Animation;

public class AboutusActivity extends Activity implements OnGestureListener{
	  // 定义手势检测器实例
	GestureDetector detector;
	//定义一个动画数组，用于为ViewFlipper指定切换动画效果
	Animation[] animations = new Animation[4];
	//定义手势动作两点之间的最小距离
	final int FLIP_DISTANCE = 50;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		detector = new GestureDetector(this);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏代码
	}
	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
		float velocityX, float velocityY)
	{
		/*
		 * 如果第一个触点事件的X座标大于第二个触点事件的X座标超过FLIP_DISTANCE
		 * 也就是手势从右向左滑。
		 */
		if (event1.getX() - event2.getX() > FLIP_DISTANCE)
		{
			
			this.finish();
			overridePendingTransition(R.anim.left_in, R.anim.left_out); 
			//Toast.makeText(this, "right", 500).show();
			return true;
		}
		/*
		 * 如果第二个触点事件的X座标大于第一个触点事件的X座标超过FLIP_DISTANCE 
		 * 也就是手势从右向左滑。
		 */
		else if (event2.getX() - event1.getX() > FLIP_DISTANCE)
		{
			return true;
		}
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//将该Activity上的触碰事件交给GestureDetector处理
		return detector.onTouchEvent(event);
	}		
	@Override
	public boolean onDown(MotionEvent arg0)
	{
		return false;
	}
	@Override
	public void onLongPress(MotionEvent event)
	{
	}
	@Override
	public boolean onScroll(MotionEvent event1, MotionEvent event2,
		float arg2, float arg3)
	{
		return false;
	}
	@Override
	public void onShowPress(MotionEvent event)
	{
	}
	@Override
	public boolean onSingleTapUp(MotionEvent event)
	{
		return false;
	}	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub
    if (((keyCode == KeyEvent.KEYCODE_BACK) ||
(keyCode == KeyEvent.KEYCODE_HOME))
&& event.getRepeatCount() == 0) {
        	this.finish();
 			overridePendingTransition(R.anim.left_in, R.anim.left_out);
         }
         return false;
        
         //end onKeyDown
  }

}
