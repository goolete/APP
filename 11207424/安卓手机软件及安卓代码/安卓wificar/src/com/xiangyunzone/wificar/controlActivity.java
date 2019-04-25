package com.xiangyunzone.wificar;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import android.R.bool;
import android.R.integer;
import android.app.Activity;
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
	
	// �������Ƽ����ʵ��
	GestureDetector detector;
	//�������ƶ�������֮�����С����
	final int FLIP_DISTANCE = 50;
	private Thread mThreadClient = null;
	private Socket mSocketClient = null;
	//��Ƶ�߳�
	private Thread mThreadvideo = null;
	private  String recvMessageClient = "";
	MySurfaceView r;
    private  boolean isConnect=false; 
	//ָ���  ���ݻ���
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
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//��������
		 //��ȡ���������
        SharedPreferences  share = controlActivity.this.getSharedPreferences("perference", MODE_PRIVATE);
        
        //����keyѰ��ֵ ����1 key ����2 ���û��value��ʾ������
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
	   Btn_djcenter= (Button)findViewById(R.id.Btn_DJcenter);
	   Btn_djright= (Button)findViewById(R.id.Btn_DJright);
	   
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
	    		 Toast.makeText(controlActivity.this,"ǰ��",500).show();
				break;
			case MotionEvent.ACTION_UP:
				 mPrintWriterClient.print(getstop);
                 mPrintWriterClient.flush();
	    		// Toast.makeText(controlActivity.this,"Btn_goforword",500).show();
				break;
			default:
				break;
			} 
			
			}
			else {
				Toast.makeText(controlActivity.this,"��������wificar��",200).show();
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
		    		 Toast.makeText(controlActivity.this,"����",500).show();
					break;
				case MotionEvent.ACTION_UP:
					 mPrintWriterClient.print(getstop);
	                 mPrintWriterClient.flush();
		    		// Toast.makeText(controlActivity.this,"Btn_goforword",500).show();
					break;
				default:
					break;
				} 
				
				}
				else {
					Toast.makeText(controlActivity.this,"��������wificar��",200).show();
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
		    		 Toast.makeText(controlActivity.this,"��ת",500).show();
					break;
				case MotionEvent.ACTION_UP:
					 mPrintWriterClient.print(getstop);
	                 mPrintWriterClient.flush();
		    		// Toast.makeText(controlActivity.this,"Btn_goforword",500).show();
					break;
				default:
					break;
				} 
				
				}
				else {
					Toast.makeText(controlActivity.this,"��������wificar��",200).show();
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
		    		 Toast.makeText(controlActivity.this,"��ת",500).show();
					break;
				case MotionEvent.ACTION_UP:
					 mPrintWriterClient.print(getstop);
	                 mPrintWriterClient.flush();
		    		// Toast.makeText(controlActivity.this,"Btn_goforword",500).show();
					break;
				default:
					break;
				} 
				
				}
				else {
					Toast.makeText(controlActivity.this,"��������wificar��",200).show();
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
			    		 Toast.makeText(controlActivity.this,"�����ת",500).show();
	    		  }
	    		  else {
	    			  Toast.makeText(controlActivity.this,"��������wificar��",200).show();
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
			    		 Toast.makeText(controlActivity.this,"�������",500).show();
	    		  }
	    		  else {
	    			  Toast.makeText(controlActivity.this,"��������wificar��",200).show();
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
			    		 Toast.makeText(controlActivity.this,"�����ת",500).show();
	    		  }
	    		  else {
	    			  Toast.makeText(controlActivity.this,"��������wificar��",200).show();
				}
	    	  }
	    	  });
	   Btn_openwifi.setOnClickListener(new  Button.OnClickListener()
	    {
	    	  public void onClick(View v) {
	    		  if(!isConnect)
	    		  {
	    		//����mThreadClient�߳�
	    	        mThreadClient = new Thread(mRunnable);
	    		    mThreadClient.start();
	    		  Btn_openwifi.setBackgroundResource(R.drawable.connect);
	    		  Toast.makeText(controlActivity.this,"������������",500).show();
	    		  }
	    		  else {
	    			  onDestroy();
	    			  isConnect=false;
	    			  Btn_openwifi.setBackgroundResource(R.drawable.disconnect);
				}
	    	  }
	    	  });
	   //����mThreadvideo�߳�
	    mThreadvideo = new Thread(mRunvideo);
	    mThreadvideo.start();//������Ƶ����
	}	
	@Override
	public boolean onFling(MotionEvent event1, MotionEvent event2,
		float velocityX, float velocityY)
	{
		/*
		 * �����һ�������¼���X������ڵڶ��������¼���X���곬��FLIP_DISTANCE
		 * Ҳ�������ƴ������󻬡�
		 */
		if (event1.getX() - event2.getX() > FLIP_DISTANCE)
		{
			return true;
		}
		/*
		 * ����ڶ��������¼���X������ڵ�һ�������¼���X���곬��FLIP_DISTANCE 
		 * Ҳ�������ƴ������󻬡�
		 */
		else if (event2.getX() - event1.getX() > FLIP_DISTANCE)
		{
			this.finish();
			overridePendingTransition(R.anim.right_in, R.anim.right_out); 
			return true;
		}
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//����Activity�ϵĴ����¼�����GestureDetector����
		 //mPrintWriterClient.print(getstop);
         //mPrintWriterClient.flush();
		 Toast.makeText(controlActivity.this,"touchme",500).show();
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
	 //�߳�mRunnable����
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
					//���ӷ�����
					mSocketClient = new Socket(sIP, port);	//portnum
					//ȡ�����롢�����
					mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
					
					mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
					
					recvMessageClient = "�ɹ�����wificar��";//��Ϣ����
					//Toast.makeText(controlActivity.this,recvMessageClient,300).show();
				    isConnect = true;
					Message msg = new Message();
	                msg.what = 1;
					mHandler.sendMessage(msg);		
					//break;
				}
				catch (Exception e) 
				{
					Btn_openwifi.setBackgroundResource(R.drawable.disconnect);
					recvMessageClient = "���Ӵ������������Ƿ����ӣ���";//��Ϣ����
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
							recvMessageClient = getInfoBuff(buffer, count);//��Ϣ����
							Thread.sleep(500);
							Message msg = new Message();
			                msg.what = 0;
							mHandler.sendMessage(msg);
						}
					}
					catch (Exception e)
					{
						recvMessageClient = "�����쳣:" + e.getMessage() + "\n";//��Ϣ����
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
		 //���մ���
	private String getInfoBuff(char[] buff, int count)
				{
					char[] temp = new char[count];
					for(int i=0; i<count; i++)
					{
						temp[i] = buff[i];
					}
					return new String(temp);
				}
	public void onDestroy() {
		super.onDestroy();
		if (isConnect) 
		{				
			isConnect = false;
			try {
				if(mSocketClient!=null)
				{
					mSocketClient.close();
					mSocketClient = null;
					//mThreadvideo.destroy();
					mPrintWriterClient.close();
					mPrintWriterClient = null;
					recvMessageClient = "����˿ڳɹ�";//��Ϣ����
					Message msg = new Message();
	                msg.what = 0;
					mHandler.sendMessage(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			mThreadClient.interrupt();
		}

	}
	  private Runnable	mRunvideo	= new Runnable() 
	    {
	    	public void run()
			{
				try 
				{				
					 //��Ƶ����
			        r=(MySurfaceView)findViewById(R.id.mySurfaceView1);
			        r.GetCameraIP(getvideoip);
			        recvMessageClient=getvideoip;
					Message msg = new Message();
	                msg.what = 1;
					mHandler.sendMessage(msg);		

				}
				catch (Exception e) 
				{
					recvMessageClient = "��Ƶ���Ӵ���";//��Ϣ����
					Message msg = new Message();
	                msg.what = 1;
					mHandler.sendMessage(msg);
					return;
				}		
	       }
	    };
}
