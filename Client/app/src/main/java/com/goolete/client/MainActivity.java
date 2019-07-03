package com.goolete.client;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.goolete.client.GPS.GPS_sned;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public String ip = "192.168.2.154";
    public static final int port = 1255;
    public static final int RECIVE_DATA = 1;

    public boolean flag_gps_find = false;
    public boolean flag_gps_send = false;

    private TextView txt;
    private View include;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Button btn_gps;
    private Button btn_gps_find;
    private EditText server_ip;
    private EditText name;
    private EditText number;
    private ScrollView mScrollView;
    Handler handler = new Handler();

    private String gps_data;
    private String gps_send_data;

    private double latitude;
    private double longitude;

    GPS_sned sendThread = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        include = findViewById(R.id.include_main);
        mScrollView = include.findViewById(R.id.scrollView);
        btn_gps_find = findViewById(R.id.gps_find);
        btn_gps = include.findViewById(R.id.gps_send);
        txt = include.findViewById(R.id.text1);
        txt.setMovementMethod(ScrollingMovementMethod.getInstance());
        /*
        输入框初始化
         */
        server_ip = include.findViewById(R.id.server_ip);
        name = include.findViewById(R.id.name);
        number = include.findViewById(R.id.number);
        ip = server_ip.getText().toString();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        init();
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        btn_gps.setEnabled(false);
        //按钮点击事件的方法
        btn_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
                    //权限已授权，功能操作
                    ip = server_ip.getText().toString();
                    sendThread = new GPS_sned(mHandler,ip,port,gps_send_data);
                    btn_gps.setEnabled(false);
                    btn_gps_find.setEnabled(false);
                    sendThread.start();
                    txt.append("发送成功\r\n");
                    scrollToBottom(mScrollView, txt);
                } else {
                    //未授权，提起权限申请
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.INTERNET)) {
                        Toast.makeText(MainActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
                        ip = server_ip.getText().toString();
                        sendThread = new GPS_sned(mHandler,ip,port,gps_send_data);
                        btn_gps.setEnabled(false);
                        btn_gps_find.setEnabled(false);
                        sendThread.start();
                        txt.append("发送成功\r\n");
                        scrollToBottom(mScrollView, txt);
                    } else {
                        //申请权限
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET, Manifest.permission.INTERNET}, 522);
                        ip = server_ip.getText().toString();
                        sendThread = new GPS_sned(mHandler,ip,port,gps_send_data);
                        btn_gps.setEnabled(false);
                        btn_gps_find.setEnabled(false);
                        sendThread.start();
                        txt.append("发送成功\r\n");
                        scrollToBottom(mScrollView, txt);
                    }
                }
            }
        });

        btn_gps_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps();
                flag_gps_find = true;
                if(flag_gps_find){
                    btn_gps.setEnabled(true);
                }
            }
        });

    }
    public void init(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.INTERNET)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET}, 200);
            }
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
            }
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.RECORD_AUDIO}, 200);
            }
        }
    }


    private Handler mHandler = new Handler(){//使用handle更新UI线程（在子线程中不能更新主线程UI，所以需要使用handler）
        public void handleMessage(Message msg){
            switch (msg.what){
                case RECIVE_DATA:
                    String data = (String)msg.obj;
                    if(data.equals("#0255")){
                        txt.append("签到成功！\n");
                        scrollToBottom(mScrollView, txt);
                    }else if(data.equals("#0266")){
                        txt.append("签到失败！\n");
                        scrollToBottom(mScrollView, txt);
                    }
                    break;
            }
        }
    };

    //实现GPS的方法
    public void gps() {
        //定义LocationManager对象
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //定义Criteria对象
        Criteria criteria = new Criteria();
        // 定位的精准度
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        // 海拔信息是否关注
        criteria.setAltitudeRequired(false);
        // 对周围的事情是否进行关心
        criteria.setBearingRequired(false);
        // 是否支持收费的查询
        criteria.setCostAllowed(true);
        // 是否耗电
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        // 对速度是否关注
        criteria.setSpeedRequired(false);

        //得到最好的定位方式
        String provider = locationManager.getBestProvider(criteria, true);

        //GPS权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //权限已授权，功能操作
            locationManager.requestLocationUpdates(provider, 5000, 0, new MyLocationListener());

        } else {
            //未授权，提起权限申请
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                locationManager.requestLocationUpdates(provider, 5000, 0, new MyLocationListener());

            } else {
                //申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 522);
                //locationManager.requestLocationUpdates(provider, 600, 0, new MyLocationListener());
            }
        }

    }

    //实现监听接口
    private final class MyLocationListener implements LocationListener {
        @Override// 位置的改变
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            latitude = location.getLatitude();// 维度
            longitude = location.getLongitude();// 经度
            //显示当前坐标
            gps_send_data = "{\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\",\"name\":\"" + name.getText() + "\",\"number\":\"" + number.getText() + "\"}" ;
            gps_data = "查询GPS成功！   location:(纬度  "+latitude+",经度  "+longitude+")\n";
            handler.post(runnableUi);
        }

        @Override// gps卫星有一个没有找到
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override// 某个设置被打开
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override// 某个设置被关闭
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable   runnableUi=new  Runnable(){
        @Override
        public void run() {
            txt.append(gps_data);
            scrollToBottom(mScrollView, txt);
        }

    };
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
        getMenuInflater().inflate(R.menu.main, menu);
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
            /*Intent intent1 = new Intent();
            intent1.setClass(MainActivity.this,MainActivity.class);
            startActivity(intent1);*/
        } else if (id == R.id.nav_talk) {
            Intent intent2 = new Intent();
            intent2.setClass(MainActivity.this,Message_call_Activity.class);
            startActivity(intent2);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 根据scrolview 和子view去测量滑动的位置
     *
     * @param scrollView
     * @param view
     */
    private void scrollToBottom(final ScrollView scrollView, final View view) {

        handler.post(new Runnable() {

            @Override
            public void run() {
                if (scrollView == null || view == null) {
                    return;
                }
                // offset偏移量。是指当textview中内容超出 scrollview的高度，那么超出部分就是偏移量
                int offset = view.getMeasuredHeight()
                        - scrollView.getMeasuredHeight();
                if (offset < 0) {
                    offset = 0;
                }
                //scrollview开始滚动
                scrollView.scrollTo(0, offset);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler = null;
        }

    }

}
