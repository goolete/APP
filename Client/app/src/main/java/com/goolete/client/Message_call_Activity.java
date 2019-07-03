package com.goolete.client;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.goolete.client.GPS.GPS_sned;
import com.goolete.client.vadio.Vadio_send;
import com.goolete.client.vadio.handleReceiveData;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.SocketException;

public class Message_call_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,SurfaceHolder.Callback, Camera.PreviewCallback, handleReceiveData {

    public static final int port = 3423;
    private ByteArrayOutputStream baos;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private View include;
    private ImageView imageView;
    private float mCameraOrientation;
    private Vadio_send vadio_send;
    private EditText text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_call);

        include = findViewById(R.id.include_message);
        text = include.findViewById(R.id.show_ip);
        surfaceView = include.findViewById(R.id.show);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initCanmera();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vadio_send = new Vadio_send(text.getText().toString(),port);
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.startPreview();

                }catch (Exception e){
                    e.printStackTrace();
                }
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initCanmera() {
        if (ActivityCompat.checkSelfPermission(Message_call_Activity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //权限已授权，功能操作
            int cameras = Camera.getNumberOfCameras();
            Camera.CameraInfo info = new Camera.CameraInfo();
            camera = Camera.open(0);
            //没有前置摄像头
            if (camera == null) camera = Camera.open();
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.setPreviewCallback(this);
            } catch (Exception e) {
                camera.release();//释放资源
                camera = null;
            }
        } else {
            //未授权，提起权限申请
            if (ActivityCompat.shouldShowRequestPermissionRationale(Message_call_Activity.this, Manifest.permission.CAMERA)) {
                Toast.makeText(Message_call_Activity.this, "没有权限", Toast.LENGTH_SHORT).show();
                int cameras = Camera.getNumberOfCameras();
                Camera.CameraInfo info = new Camera.CameraInfo();
                camera = Camera.open(0);
                //没有前置摄像头
                if (camera == null) camera = Camera.open();
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.setPreviewCallback(this);
                } catch (Exception e) {
                    camera.release();//释放资源
                    camera = null;
                }
            } else {
                //申请权限
                ActivityCompat.requestPermissions(Message_call_Activity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA}, 522);
                int cameras = Camera.getNumberOfCameras();
                Camera.CameraInfo info = new Camera.CameraInfo();
                camera = Camera.open(0);
                //没有前置摄像头
                if (camera == null) camera = Camera.open();
                try {
                    camera.setPreviewDisplay(surfaceHolder);
                    camera.setPreviewCallback(this);
                } catch (Exception e) {
                    camera.release();//释放资源
                    camera = null;
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_call_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent1 = new Intent();
            intent1.setClass(Message_call_Activity.this,MainActivity.class);
            startActivity(intent1);
        } else if (id == R.id.nav_talk) {
            /*Intent intent2 = new Intent();
            intent2.setClass(Message_call_Activity.this,Message_call_Activity.class);
            startActivity(intent2);*/
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

        Camera.Size previewSize = camera.getParameters().getPreviewSize();
        int[] rgb = decodeYUV420SP(data, previewSize.width, previewSize.height);
        Bitmap bmp = Bitmap.createBitmap(rgb, previewSize.width, previewSize.height, Bitmap.Config.ARGB_8888);
        int smallWidth, smallHeight;
        int dimension = 200;

        if (previewSize.width > previewSize.height) {
            smallWidth = dimension;
            smallHeight = dimension * previewSize.height / previewSize.width;
        } else {
            smallHeight = dimension;
            smallWidth = dimension * previewSize.width / previewSize.height;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(mCameraOrientation);

        Bitmap bmpSmall = Bitmap.createScaledBitmap(bmp, smallWidth, smallHeight, false);
        Bitmap bmpSmallRotated = Bitmap.createBitmap(bmpSmall, 0, 0, smallWidth, smallHeight, matrix, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmpSmallRotated.compress(Bitmap.CompressFormat.WEBP, 80, baos);

        //vadio_send.send(baos);
        //up.sendMsg(baos.toByteArray());
    }

    public int[] decodeYUV420SP(byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;
        int rgb[] = new int[width * height];
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0) y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                if (r < 0) r = 0;
                else if (r > 262143) r = 262143;
                if (g < 0) g = 0;
                else if (g > 262143) g = 262143;
                if (b < 0) b = 0;
                else if (b > 262143) b = 262143;
                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
                        | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
        return rgb;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initCanmera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        int currentCamera = Camera.CameraInfo.CAMERA_FACING_FRONT;
        Camera.Parameters parameters = camera.getParameters();//得到相机设置参数
        Camera.Size size = camera.getParameters().getPreviewSize(); //获取预览大小

        parameters.setPictureFormat(PixelFormat.JPEG);//设置图片格式
        Camera.CameraInfo info = new Camera.CameraInfo();
        camera.getCameraInfo(currentCamera, info);
        int rotation = this.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int resultA = 0, resultB = 0;
        if (currentCamera == Camera.CameraInfo.CAMERA_FACING_BACK) {
            resultA = (info.orientation - degrees + 360) % 360;
            resultB = (info.orientation - degrees + 360) % 360;
            camera.setDisplayOrientation(resultA);
        } else {
            resultA = (360 + 360 - info.orientation - degrees) % 360;
            resultB = (info.orientation + degrees) % 360;
            camera.setDisplayOrientation(resultA);
        }
        camera.setPreviewCallback(this);
        parameters.setRotation(resultB);
        mCameraOrientation = resultB;
        camera.setParameters(parameters);
        camera.startPreview();//开始预览
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void handleReceive(byte[] data) {

    }
}
