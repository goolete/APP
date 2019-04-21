package com.xiangyunzone.wificar;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import android.R.bool;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.Button;
import android.widget.Toast;


public class controlActivity extends Activity 
implements OnGestureListener{
	
	// 定义手势检测器实例
	GestureDetector detector;
	//定义手势动作两点之间的最小距离
	final int FLIP_DISTANCE = 50;
	private Thread mThreadClient = null;
	private Socket mSocketClient = null;
	//视频线程
	private Thread mThreadvideo = null;
	private  String recvMessageClient = "";
	MySurfaceView r;
    private  boolean isConnect=false; 
	//指令发出  数据缓存
	static PrintWriter mPrintWriterClient = null;
	static BufferedReader mBufferedReaderClient	= null;
	private Button Btn_goforword;
	private Button Btn_goback;
	private Button Btn_turnleft;
	private Button Btn_turnright;
	private Button Btn_openwifi;
	private Button Btn_djleft;
	private Button Btn_djcenter;
	private Button Btn_djright;
	private String getstop ;
	private String getcontrolip;
	private String getvideoip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);
		detector = new GestureDetector(this);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏代码
		 //获取保存的数据
        SharedPreferences  share = controlActivity.this.getSharedPreferences("perference", MODE_PRIVATE);
        
        //根据key寻找值 参数1 key 参数2 如果没有value显示的内容
        getvideoip = share.getString("videoip", "http://192.168.1.1:8080?action=snapshot");
        getcontrolip  = share.getString("controlip", "192.168.1.1:2001");
		final String getforword  = share.getString("forword", "4");
		final String getback  = share.getString("back", "2");
		final String getturnleft  = share.getString("turnleft", "3");
		final String getturnright  = share.getString("turnright", "1");
		final String getdjleft  = share.getString("djleft", "5");
		final String getdjcenter  = share.getString("djcenter", "6");
		final String getdjright  = share.getString("djright", "7");
		getstop = share.getString("stop", "0");
		
	   Btn_goforword= (Button)findViewById(R.id.Btn_forword);
	   Btn_goback= (Button)findViewById(R.id.Btn_back);
	   Btn_turnleft= (Button)findViewById(R.id.Btn_left);
	   Btn_turnright= (Button)findViewById(R.id.Btn_right);
	   Btn_openwifi= (Button)findViewById(R.id.Btn_wifi);
	   Btn_djleft= (Button)findViewById(R.id.Btn_DJleft);
	   Btn_djcenter= (Button)findViewById(R.id.Btn_DJC);
	   Btn_djright= (Button)findViewById(R.id.Btn_DJR);
	   
	   Btn_goforword.setOnTouchListener(new View.OnTouchListener() {		
		@Override
	       public boolean onTouch(View v, MotionEvent event) {
			if(isConnect)
			{
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				 mPrintWriterClient.print(getforword);
                 mPrintWriterClient.flush();
				break;
			case MotionEvent.ACTION_UP:
				 mPrintWriterClient.print(getstop);
                 mPrintWriterClient.flush();
				break;
			default:
				break;
			} 
			
			}
			else {
				Toast.makeText(controlActivity.this,"请先连接wificar！",200).show();
			}
			return false;
		   
		}
	}); 
	   Btn_goback.setOnTouchListener(new View.OnTouchListener() {		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(isConnect)
				{
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					 mPrintWriterClient.print(getback);
	                 mPrintWriterClient.flush();
					break;
				case MotionEvent.ACTION_UP:
					 mPrintWriterClient.print(getstop);
	                 mPrintWriterClient.flush();
					break;
				default:
					break;
				} 
				
				}
				else {
					Toast.makeText(controlActivity.this,"请先连接wificar！",200).show();
				}
				return false;
			   
			}
		});
	   Btn_turnleft.setOnTouchListener(new View.OnTouchListener() {		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(isConnect)
				{
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					 mPrintWriterClient.print(getturnleft);
	                 mPrintWriterClient.flush();
					break;
				case MotionEvent.ACTION_UP:
					 mPrintWriterClient.print(getstop);
	                 mPrintWriterClient.flush();
					break;
				default:
					break;
				} 
				
				}
				else {
					Toast.makeText(controlActivity.this,"请先连接wificar！",200).show();
				}
				return false;
			   
			}
		});   
	   Btn_turnright.setOnTouchListener(new View.OnTouchListener() {		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(isConnect)
				{
				int action = event.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					 mPrintWriterClient.print(getturnright);
	                 mPrintWriterClient.flush();
					break;
				case MotionEvent.ACTION_UP:
					 mPrintWriterClient.print(getstop);
	                 mPrintWriterClient.flush();
					break;
				default:
					break;
				} 
				
				}
				else {
					Toast.makeText(controlActivity.this,"请先连接wificar！",200).show();
				}
				return false;
			   
			}
		});
	   Btn_djleft.setOnClickListener(new  Button.OnClickListener()
	    {
	    	  public void onClick(View v) {
	    		  if(isConnect)
	    		  {
	    			     mPrintWriterClient.print(getdjleft);
		                 mPrintWriterClient.flush();
			    		 Toast.makeText(controlActivity.this,"舵机左转",500).show();
	    		  }
	    		  else {
	    			  Toast.makeText(controlActivity.this,"请先连接wificar！",200).show();
				}
	    	  }
	    	  });
	   Btn_djcenter.setOnClickListener(new  Button.OnClickListener()
	    {
	    	  public void onClick(View v) {
	    		  if(isConnect)
	    		  {
	    			     mPrintWriterClient.print(getdjcenter);
		                 mPrintWriterClient.flush();
			    		 Toast.makeText(controlActivity.this,"舵机居中",500).show();
	    		  }
	    		  else {
	    			  Toast.makeText(controlActivity.this,"请先连接wificar！",200).show();
				}
	    	  }
	    	  });
	   Btn_djright.setOnClickListener(new  Button.OnClickListener()
	    {
	    	  public void onClick(View v) {
	    		  if(isConnect)
	    		  {
	    			     mPrintWriterClient.print(getdjright);
		                 mPrintWriterClient.flush();
			    		 Toast.makeText(controlActivity.this,"舵机右转",500).show();
	    		  }
	    		  else {
	    			  Toast.makeText(controlActivity.this,"请先连接wificar！",200).show();
				}
	    	  }
	    	  });
	   Btn_openwifi.setOnClickListener(new  Button.OnClickListener()
	    {
	    	  public void onClick(View v) {
	    		  if(!isConnect)
	    		  {
	    		//开启mThreadClient线程
	    	        mThreadClient = new Thread(mRunnable);
	    		    mThreadClient.start();
	    		  Btn_openwifi.setBackgroundResource(R.drawable.connect);
	    		  Toast.makeText(controlActivity.this,"尝试连接网络",500).show();
	    		  }
	    		  else {
	    			  Toast.makeText(controlActivity.this,"断开连接将返回主界面",500).show();
	    			  closethis();
	    			  Btn_openwifi.setBackgroundResource(R.drawable.disconnect);
				}
	    	  }
	    	  });
	   //开启mThreadvideo线程
	    mThreadvideo = new Thread(mRunvideo);
	    mThreadvideo.start();//开启视频监听
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
			return true;
		}
		/*
		 * 如果第二个触点事件的X座标大于第一个触点事件的X座标超过FLIP_DISTANCE 
		 * 也就是手势从右向左滑。
		 */
		else if (event2.getX() - event1.getX() > FLIP_DISTANCE)
		{
			onDestroy();
			this.finish();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
         // TODO Auto-generated method stub
         if (((keyCode == KeyEvent.KEYCODE_BACK) ||
(keyCode == KeyEvent.KEYCODE_HOME))
&& event.getRepeatCount() == 0) {
        	 this.finish();
 			overridePendingTransition(R.anim.right_in, R.anim.right_out);
 			
         }
         return false;
        
         //end onKeyDown
  }
	 //线程mRunnable启动
	private Runnable	mRunnable	= new Runnable() 
		{
			public void run()
			{
				
				String ControlAddress = getcontrolip;
				int start = ControlAddress.indexOf(":");
				String sIP = ControlAddress.substring(0, start);
				String sPort = ControlAddress.substring(start+1);
				int port = Integer.parseInt(sPort);				
				
				Log.d("gjz", "IP:"+ sIP + ":" + port);		

				try 
				{				
					//连接服务器
					mSocketClient = new Socket(sIP, port);	//portnum
					//取得输入、输出流
					
					mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
					
					mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
					
					recvMessageClient = "成功连接wificar！";//消息换行
				    isConnect = true;
					Message msg = new Message();
	                msg.what = 1;
					mHandler.sendMessage(msg);		
					//break;
				}
				catch (Exception e) 
				{
					Btn_openwifi.setBackgroundResource(R.drawable.disconnect);
					recvMessageClient = "连接错误：请检查网络是否连接！！";//消息换行
					Message msg = new Message();
	                msg.what = 1;
					mHandler.sendMessage(msg);
					return;
				}			

				char[] buffer = new char[256];
				int count = 0;
				while (true)
				{
					try
					{
						//if ( (recvMessageClient = mBufferedReaderClient.readLine()) != null )
						if((count = mBufferedReaderClient.read(buffer))>0)
						{						
							recvMessageClient = getInfoBuff(buffer, count);//消息换行
							Thread.sleep(500);
							Message msg = new Message();
			                msg.what = 0;
							mHandler.sendMessage(msg);
						}
					}
					catch (Exception e)
					{
						recvMessageClient = "接收异常:" + e.getMessage() + "\n";//消息换行
						Message msg = new Message();
		                msg.what = 0;
						mHandler.sendMessage(msg);
					}
				}
			}
		};
	Handler mHandler = new Handler()
		{						
			public void handleMessage(Message msg)										
			  {											
				  super.handleMessage(msg);	
				  if(msg.what == 1)
				  {
				  
				  Toast.makeText(controlActivity.this,recvMessageClient,300).show();
				  }
				  if(msg.what == 0)
				  {
				Toast.makeText(controlActivity.this,recvMessageClient,300).show();
				 
				  }
			  }									
		 };
		 //接收处理
	private String getInfoBuff(char[] buff, int count)
				{
					char[] temp = new char[count];
					for(int i=0; i<count; i++)
					{
						temp[i] = buff[i];
					}
					return new String(temp);
				}
	public void Destroy() {
		if (isConnect) 
		{				
			isConnect = false;
			try {
				if(mSocketClient!=null)
				{
					mSocketClient.close();
					mSocketClient = null;
					mPrintWriterClient.close();
					mPrintWriterClient = null;
					recvMessageClient = "网络断开成功";//消息换行
					Message msg = new Message();
	                msg.what = 1;
					mHandler.sendMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	//		mThreadClient.interrupt();
		}

	}
    private Runnable	mRunvideo	= new Runnable() 
	    {
	    	public void run()
			{
				try 
				{				
					 //视频监听
			        r=(MySurfaceView)findViewById(R.id.mySurfaceView1);
			        r.GetCameraIP(getvideoip);	

				}
				catch (Exception e) 
				{
					recvMessageClient = "视频连接错误！";//消息换行
					Message msg = new Message();
	                msg.what = 1;
					mHandler.sendMessage(msg);
					return;
				}		
	       }
	    };
	  private void closethis()
	  {
	     this.finish();
	     overridePendingTransition(R.anim.left_in, R.anim.left_out);	
	  }
}
