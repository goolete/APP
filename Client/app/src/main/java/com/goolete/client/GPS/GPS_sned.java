package com.goolete.client.GPS;

import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Handler;

public class GPS_sned extends Thread{

    public static final int RECIVE_DATA = 1;

    private Handler send_handler;
    private String ip;
    private int port;
    private String data;
    Socket send_socket;
    DataOutputStream outputStream;
    InputStream inputStream;
    public GPS_sned(Handler handler,String ip,int port,String data){
            this.send_handler = handler;
            this.ip = ip;
            this.port = port;
            this.data = data;
    }
    @Override
    public void run() {
        try{
            send_socket = new Socket(ip,port);//创建socket，传入上位机的IP地址和端口
            send_socket.setSoTimeout(2000);//设置超时时间
            outputStream = new DataOutputStream(send_socket.getOutputStream());
            sendTextMsg(outputStream,data);
            //outputStream.close();
            while(true){
                if(send_socket.getInputStream() != null) {
                    inputStream = send_socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String data_recive = br.readLine();
                    send_socket.close();
                    outputStream.close();
                    inputStream.close();
                    Message message_recive = Message.obtain();
                    message_recive.what = RECIVE_DATA;
                    message_recive.obj = data_recive;
                    send_handler.sendMessage(message_recive);
                }
                break;
            }

        }catch (IOException e){
            Log.e("","IOException" + e);
        }
    }

    public void sendTextMsg(DataOutputStream out, String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        long len = bytes.length;
        //先发送长度，在发送内容
        out.writeLong(len);
        out.write(bytes);
    }
}



