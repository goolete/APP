package com.xiangyunzone.wificar;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


public class MySurfaceView extends SurfaceView 
   implements Callback, Runnable
     {
	    private SurfaceHolder sfh;  
	    private Canvas canvas;  
	    URL videoUrl;
	    private String url;
	    HttpURLConnection conn;
	    Bitmap bmp;
	    private Paint p;      
	    InputStream inputstream=null;
	    private Bitmap mBitmap;    
	    private static int mScreenWidth; 
	    private static int mScreenHeight;
        float myEyesDistance;
        int numberOfFaceDetected;
        Paint myPaint = new Paint();
        
	    public MySurfaceView(Context context, AttributeSet attrs) {

	    	  super(context, attrs);
	    	  initialize();
	    	  p = new Paint();
	    	  p.setAntiAlias(true);
	    	  myPaint.setColor(Color.GREEN);
	    	  myPaint.setStyle(Paint.Style.STROKE);
	    	  myPaint.setStrokeWidth(3);
	             
	    	  sfh = this.getHolder();
	    	  sfh.addCallback(this);
	    	  this.setKeepScreenOn(true);
	    	  setFocusable(true);
	    	  this.getWidth();
	    	  this.getHeight();  
	    	   }

	    	 private void initialize() { 
	    		   DisplayMetrics dm = new  DisplayMetrics();     
	    		   dm = getResources().getDisplayMetrics();
	    	           mScreenWidth = dm.widthPixels; 
	    	         mScreenHeight = dm.heightPixels;                  
	    	       } 
	    	 class DrawVideo extends Thread
	    	  {
	    	  
	    	    public DrawVideo() { 
	    	     
	    	  }     
	    	   public void run() {
	    		     while (true)
	    		     {
	    		     try {  
	    		    	 	videoUrl=new URL(url);
	    		    	 	conn = (HttpURLConnection)videoUrl.openConnection();
	    		    	 	conn.connect();
	    		    	 	inputstream = conn.getInputStream(); //��ȡ��
	    		    	 	BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
	    		            BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
	    		            bmp = BitmapFactory.decodeStream(inputstream,null,BitmapFactoryOptionsbfo);//�ӻ�ȡ�����й�����BMPͼ��
	    		            mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
	    		            canvas = sfh.lockCanvas();
	    		            canvas.drawColor(Color.WHITE);
	    		            canvas.drawBitmap(mBitmap, 0,0, null);//��BMPͼ���ڻ�����
	    		            sfh.unlockCanvasAndPost(canvas);//����һ��ͼ�񣬽������� 
	    		            
	    		         } catch (Exception ex) { 
	    		          ex.printStackTrace();
	    		         } finally {     
	    		         }   
	    		     }
	    		     }
	 
	    }    
	        public void surfaceChanged(SurfaceHolder holder, int format, int width,    
	            int height) 
	          {    
	           }    
	        public void surfaceDestroyed(SurfaceHolder holder) 
	       {
	    	   
	        } 
	       
	        public void GetCameraIP(String p){url=p;}

	        public void run() {
		     while (true)
		     {
		     try {  
		         	videoUrl=new URL(url);
		         	conn = (HttpURLConnection)videoUrl.openConnection();
		         	conn.connect();
		         	inputstream = conn.getInputStream(); //��ȡ��
		         	bmp = BitmapFactory.decodeStream(inputstream);//�ӻ�ȡ�����й�����BMPͼ��
		         	mBitmap = Bitmap.createScaledBitmap(bmp, mScreenWidth, mScreenHeight, true);
		            canvas = sfh.lockCanvas();
		            canvas.drawColor(Color.WHITE);
		            canvas.drawBitmap(mBitmap, 0,0, null);//��BMPͼ���ڻ�����
		            sfh.unlockCanvasAndPost(canvas);//����һ��ͼ�񣬽������� 
		         } 
		     catch (Exception ex) 
		         { 
		          ex.printStackTrace();
		         } 
		     finally 
		         {  
		    	 
		         }   
		     }
		     }

	        public void surfaceCreated(SurfaceHolder holder) 
		{
			new DrawVideo().start();	
		}
	       
            public void drowblack()
		{
		}
	}    