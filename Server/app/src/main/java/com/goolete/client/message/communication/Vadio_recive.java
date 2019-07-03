package com.goolete.client.message.communication;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Vadio_recive {

    byte[] buffer;
    byte[] data = new byte[1024];
    private int port;

    public Vadio_recive(int port){
        this.port = port;
    }
    public byte[] recive(){
        try{
            while (true) {
                try {
                    DatagramSocket datagramSocket  = new DatagramSocket(port);
                    DatagramPacket recevPacket;
                    recevPacket = new DatagramPacket(buffer, 0, buffer.length);
                    datagramSocket.receive(recevPacket);
                    String ip = recevPacket.getAddress().toString();
                    data = recevPacket.getData();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
}

