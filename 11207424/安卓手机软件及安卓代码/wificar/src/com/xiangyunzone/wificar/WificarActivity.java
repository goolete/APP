package com.xiangyunzone.wificar;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class WificarActivity extends Activity 
     implements OnGestureListener
{
	private Button loginButton;
	// 定义手势检测器实例
	GestureDetector detector;
	//定义手势动作两点之间的最小距离
	final int FLIP_DISTANCE = 80;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wificar);
		detector = new GestureDetector(this);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏代码				
	
	    loginButton = (Button)findViewById(R.id.login);
	    loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
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
			
			Intent intent=new Intent();	     		 
	         intent.setClass(WificarActivity.this, controlActivity.class);
		     startActivity(intent);
		     overridePendingTransition(R.anim.left_in, R.anim.left_out); 
			return true;
		}
		/*
		 * 如果第二个触点事件的X座标大于第一个触点事件的X座标超过FLIP_DISTANCE 
		 * 也就是手势从右向左滑。
		 */
		else if (event2.getX() - event1.getX() > FLIP_DISTANCE)
		{
			Intent intent=new Intent();	     		 
	         intent.setClass(WificarActivity.this, AboutusActivity.class);
		     startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.right_out); 
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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_wificar, menu);
		return true;
		
	}
	public static void dialog_Exit(Context context) {
		   AlertDialog.Builder builder = new Builder(context);
		   builder.setMessage("确定要退出吗?");
		   builder.setTitle("提示");
		   builder.setIcon(android.R.drawable.ic_dialog_alert);
		   builder.setPositiveButton("确认",
		           new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		                   dialog.dismiss();
		                   android.os.Process.killProcess(android.os.Process
		                           .myPid());
		               }
		           });
		  
		   builder.setNegativeButton("取消",
		           new android.content.DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int which) {
		                   dialog.dismiss();
		               }
		           });
		  
		   builder.create().show();
		   
		  }     
	 @Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
	        // TODO Auto-generated method stub  
	        switch(item.getItemId()){  
	      
	        case R.id.menu_out:  
	        	 dialog_Exit(WificarActivity.this);
	        	 break;  
	        case R.id.menu_settings:
	        	 Intent intent=new Intent();	     		 
	 	         intent.setClass(WificarActivity.this,SettingActivity.class);
	 		     startActivity(intent);
	 		     overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
	        	// break; 
	        }  
	        return super.onOptionsItemSelected(item);  
	    }  
}
