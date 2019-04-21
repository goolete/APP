package com.example.yangjianlin.hkmonitor_3813;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends Activity implements View.OnClickListener{

    public static final int SUCCESS = 4;
    public static final int ERRORID = 0;
    public static final int ERRORCOM = 1;
    public static final int ERRORKEY = 2;
    public static final int ERRORSSECRET = 3;
    private EditText id;
    private EditText com;
    private EditText key;
    private EditText secret;

    String stringid = null;
    String stringkey = null;
    String stringsecret = null;
    String stringcom = null;

        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button mfindButton = (Button) findViewById(R.id.find_button);
        mfindButton.setOnClickListener(MainActivity.this);
        requestPermissions();

        id = findViewById(R.id.text_input_host);
        com = findViewById(R.id.text_input_com);
        key = findViewById(R.id.text_input_key);
        secret = findViewById(R.id.text_input_secret);
        Toast.makeText(MainActivity.this,"请先输入配置后保存，再进行连接设备",Toast.LENGTH_LONG).show();

    }

    public void onClick(View view) {
        /*if (view.getId() == R.id.preview_button) {
            startActivity(new Intent(this, PreviewActivity.class));
        } else*/
            if (view.getId() == R.id.find_button) {

            stringid = id.getText().toString().trim();
            stringcom = com.getText().toString().trim();
            stringkey = key.getText().toString().trim();
            stringsecret = secret.getText().toString().trim();

            if(TextUtils.isEmpty(stringid))

                {
                    Toast.makeText(MainActivity.this, "所输id为空，请确认后输入", LENGTH_SHORT).show();
                    return;
                }

            if(TextUtils.isEmpty(stringcom))

                {
                    Toast.makeText(MainActivity.this, "所输端口为空，请确认后输入", LENGTH_SHORT).show();
                return;
                }
            if(TextUtils.isEmpty(stringkey))

                {
                    Toast.makeText(MainActivity.this, "所输账号为空，请确认后输入", LENGTH_SHORT).show();
                    return;
                }
            if(TextUtils.isEmpty(stringsecret))

                {
                    Toast.makeText(MainActivity.this, "所输密码为空，请确认后输入", LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.setClass(MainActivity .this,PreviewActivity .class);
                Bundle bundle = new Bundle();
                bundle.putString("stringid",stringid);
                bundle.putString("Stringcom",stringcom);
                bundle.putString("stringkey",stringkey);
                bundle.putString("stringsecret",stringsecret);
                intent.putExtras(bundle);
                startActivity(intent);
                Toast.makeText(MainActivity .this,"保存成功，请连接设备",LENGTH_SHORT).show();
            Log.d(TAG,stringid +stringcom +stringkey +stringsecret);

        }


    }


    /**
     * 获取手机存储读写权限
     */
    private void requestPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, (new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}), 10);
        }
    }

}