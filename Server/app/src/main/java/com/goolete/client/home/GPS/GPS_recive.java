package com.goolete.client.home.GPS;

import android.os.Message;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import android.os.Handler;

import com.goolete.client.home.Data.Student;

import org.json.JSONObject;

public class GPS_recive extends Thread {
    public static final int RECIVE_NAME = 0;
    public static final int RECIVE_IP = 1;

    private List gps_data;
    private Handler recive_handler;
    private String ip;
    private int port;
    private String data;
    private double latitude;
    private double longitude;

    ServerSocket recive_socket;
    DataInputStream inputStream;

    public GPS_recive(Handler handler, int port,double latitude, double longitude){
        this.recive_handler = handler;
        this.port = port;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void run() {
        try{
            recive_socket = new ServerSocket(port);
            while(true) {
                Socket socket = recive_socket.accept();
                recive_socket.setSoTimeout(100000);
                InetAddress client_ip = socket.getInetAddress();
                inputStream = new DataInputStream(socket.getInputStream());
                String data_ip = client_ip.toString();
                String data_gps = getTextMsg(inputStream);

                Student student = new Student();
                student.setIp(data_ip);
                     /*
                    数据解析 json
                    */
                try{
                    JSONObject json_gps = new JSONObject(data_gps);
                    student.setLatitude(json_gps.getDouble("latitude"));
                    student.setLongitude(json_gps.getDouble("longitude"));
                    student.setName(json_gps.getString("name"));
                    student.setNumber(json_gps.getInt("number"));
                }catch (Exception e){
                    Log.e("Exception","JSON" + e);
                }

                if((Math.abs(student.getLatitude() - latitude) <= 0.0004 )&& (Math.abs(student.getLongitude() - longitude) <= 0.0004 )){
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bw.write("#0255\n");
                    bw.flush();
                }else{
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bw.write("#0266\n");
                    bw.flush();
                }

                Message message_IP = Message.obtain();
                message_IP.what = RECIVE_IP;
                message_IP.obj = student;
                recive_handler.sendMessage(message_IP);

                Message message_GPS = Message.obtain();
                message_GPS.what = RECIVE_NAME;
                message_GPS.obj = student.getName();
                recive_handler.sendMessage(message_GPS);

                socket.close();
                inputStream.close();
                System.out.println("通讯结束");
            }
        }catch (Exception e){
            Log.e("","IOException" + e);
        }
    }

    public static String getTextMsg(DataInputStream input) throws IOException {
        //一样先读长度，再根据长度读消息
        long len = input.readLong();
        System.out.println("len = " + len);
        byte[] bytes = new byte[(int) len];
        input.read(bytes);
        String msg = new String(bytes);
        System.out.println("msd = " + msg);
        return msg;
    }
}
