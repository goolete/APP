package com.goolete.client.home;

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
import android.widget.ExpandableListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.goolete.client.home.Data.Student;
import com.goolete.client.home.GPS.GPS_recive;
import com.goolete.client.home.listtool.Translate;
import com.goolete.client.message.Message_call_Activity;
import com.goolete.client.R;
import com.goolete.client.home.listtool.ContentInfo;
import com.goolete.client.home.listtool.EListViewUtils;
import com.goolete.client.home.listtool.MyAdapter;
import com.goolete.client.home.listtool.TitleInfo;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,MyAdapter.OnGroupExpanded{

    public static final int RECIVE_NAME = 0;
    public static final int RECIVE_IP = 1;

    public static final String ip = "192.168.1.111";
    public static final int port = 1255;
    public int flag_connter_recive = 0;
    public boolean flag_sucess = true;

    private ExpandableListView mElistview;
    public String[] titleStrings = {"签到成功", "签到失败"};

    public String[][] nameStrings = {
            {""},
            {""}
    };
    private MyAdapter mAdapter;
    private List<TitleInfo> mList = new ArrayList<>();

    private TextView txt;
    private View include;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Button btn_gps;
    private Button btn_save;
    private Button btn_message;
    private ScrollView mScrollView;
    Handler handler = new Handler();

    private double latitude;
    private double longitude;

    public ArrayList client_data = new ArrayList();
    public ArrayList client_data_sucess = new ArrayList();
    public ArrayList client_data_falure= new ArrayList();

    GPS_recive reciveThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        include = findViewById(R.id.include_main);

        mElistview = include.findViewById(R.id.mElistview);
        txt = include.findViewById(R.id.text1);
        mScrollView = include.findViewById(R.id.scrollView);
        txt.setMovementMethod(ScrollingMovementMethod.getInstance());
        btn_gps = include.findViewById(R.id.find);
        btn_save = include.findViewById(R.id.save);
        btn_message = include.findViewById(R.id.message);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        txt.append("请点击查询GPS信息，然后点击校正按钮！\n" + "查询到GPS信息后，再点击校正按钮！\n");
        btn_save.setEnabled(false);
        btn_gps.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    gps();
                    //SystemClock.sleep(4000);
                    System.out.println(latitude + longitude);
                    if(latitude != 0 && longitude != 0){
                        handler.post(runnable_gps);
                        btn_save.setEnabled(true);
                    }else{
                        txt.append("请点击查询GPS信息按钮\n");
                        scrollToBottom(mScrollView, txt);
                    }
            }
        });
        btn_save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(latitude != 0 && longitude != 0){
                    handler.post(runnableUi);
                }
                reciveThread = new GPS_recive(mHandler,port,latitude,longitude);
                reciveThread.start();
            }
        });
        btn_message.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initList();
                initAdapter();
                initListenter();
                btn_message.setEnabled(false);
            }
        });

    }

    // 构建Runnable对象，在runnable中更新界面
    Runnable runnable_gps=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            //显示当前坐标
            txt.append("查询GPS成功！\n   location:(纬度  "+latitude+",经度  "+longitude+")\n");
            txt.append("请点击校正按钮\n");
            scrollToBottom(mScrollView, txt);
        }
    };
    // 构建Runnable对象，在runnable中更新界面
    Runnable runnableUi=new  Runnable(){
        @Override
        public void run() {
            //更新界面
            txt.append("校正成功！ 正在等待接收...\n");
            scrollToBottom(mScrollView, txt);
        }
    };


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
                case RECIVE_NAME:
                        Student stud1 = (Student) client_data.get(flag_connter_recive);
                        String data = (String) msg.obj;
                        if(flag_connter_recive == (client_data.size()-1)){
                            if(flag_sucess){
                                txt.append("        " + data + "   签到成功!\n");
                            }else{
                                txt.append("        " + data + "   签到失败!\n");
                            }
                            txt.append("    共有" + client_data.size() + "人签到\n" + "   " + client_data_sucess.size() + "人签到成功，" + (client_data.size() - client_data_sucess.size()) + "人未能签到成功。\r\n");
                            scrollToBottom(mScrollView, txt);
                            flag_connter_recive++;
                        }

                    break;
                case RECIVE_IP:
                    if(msg.obj != null){
                        Student stu = new Student();
                        stu = (Student) msg.obj;
                        client_data.add(stu);
                        Detection det = new Detection(client_data,latitude,longitude);
                        client_data = det.detection();
                        client_data_sucess = det.compare();
                        client_data_falure = det.compare_falure();
                        flag_sucess = det.judget(flag_connter_recive);
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
            locationManager.requestLocationUpdates(provider, 100, 0, new MyLocationListener());

        } else {
            //未授权，提起权限申请
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                locationManager.requestLocationUpdates(provider, 100, 0, new MyLocationListener());

            } else {
                //申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 522);
                locationManager.requestLocationUpdates(provider, 100, 0, new MyLocationListener());
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
            intent2.setClass(MainActivity.this, Message_call_Activity.class);
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

    /**
     * ExpandableListView条目点击事件
     */
    private void initListenter() {
        //子对象点击监听事件
        mElistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String data = nameStrings[groupPosition][childPosition];
                String ip  = "";
                try{
                    JSONObject jsonObject = new JSONObject(data);
                    ip = jsonObject.getString("ip");
                }catch (Exception e){
                    e.printStackTrace();
                }

                Intent intent = new Intent();
                //第一参数取的是这个应用程序的Context，生命周期是整个应用
                //第二个参数是要跳转的页面的全路径
                intent.setClassName( getApplicationContext(), "com.goolete.client.message.Message_call_Activity" );
                //Bundle类用作携带数据，它类似于Map，用于存放key-value名值对形式的值
                Bundle b = new Bundle();
                b.putString("IP", ip);
                //此处使用putExtras，接受方就响应的使用getExtra
                intent.putExtras(b);
                startActivity(intent);
                // 关闭当前页面
                System.exit(0);
                //Toast.makeText(MainActivity.this,nameStrings[groupPosition][childPosition]+"",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //组对象点击监听事件
        mElistview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;//请务必返回false，否则分组不会展开
            }
        });
        //组对象判断分组监听事件
        mAdapter.setOnGroupExPanded(this);
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        mAdapter = new MyAdapter(mList, this);
        mElistview.setAdapter(mAdapter);
    }

    /**
     * 初始化数据源
     */
    private void initList() {
        Translate tran = new Translate(client_data_sucess,client_data_falure);
        nameStrings = tran.trans();

        for (int i = 0; i < titleStrings.length; i++) {
            //创建组对象
            TitleInfo info= new TitleInfo();
            //循环添加组的标题名
            info.setTitle(titleStrings[i]);
            //创建子对象数据源
            List<ContentInfo> list = new ArrayList<>();
            for (int j = 0; j < nameStrings[0].length; j++) {
                //创建子对象
                ContentInfo info2 = new ContentInfo();
                //添加用户名或者头像
                info2.setName(nameStrings[i][j]);
                //将子对象添加到数据源
                list.add(info2);
            }
            //将子对象数据源复制给组对象
            info.setInfo(list);
            //将组对象添加到总数据源中
            mList.add(info);

        }

    }


    /**
     * 监听是否关闭其他的组对象
     * @param groupPostion
     */
    public void onGroupExpanded(int groupPostion) {
        EListViewUtils utils=new EListViewUtils();
        utils.expandOnlyOne(groupPostion,mElistview);
    }
}
