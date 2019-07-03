package com.goolete.client.message;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.goolete.client.R;
import com.goolete.client.home.MainActivity;
import com.goolete.client.home.listtool.ContentInfo;
import com.goolete.client.home.listtool.EListViewUtils;
import com.goolete.client.home.listtool.MyAdapter;
import com.goolete.client.home.listtool.TitleInfo;
import com.goolete.client.message.communication.Vadio_recive;

public class Message_call_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SurfaceHolder.Callback {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_call);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        String rString = getIntent().getExtras().getString("IP");
        Toast.makeText(Message_call_Activity.this, rString, Toast.LENGTH_SHORT).show();
        //initSurfaceView();
    }

    private void initSurfaceView() {
        //保持屏幕常亮
        surfaceView.getHolder().setKeepScreenOn(true);

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initMediaPlayer();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mMediaPlayer.release();
                Log.i(">>>>>>>>>>>>>>>>>>>>>", "销毁了");
            }
        });
    }
    private void initMediaPlayer() {

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.reset();//初始化
            Vadio_recive vadio_recive = new Vadio_recive(3423);
            AssetFileDescriptor afd = getAssets().openFd("advideo.mp4");//获取视频资源
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.setDisplay(surfaceView.getHolder());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mMediaPlayer.start();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
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
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
