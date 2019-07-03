package com.goolete.client.vadio;

import android.hardware.Camera;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Vadio_send {

    private String server_ip;
    private int port;
    DatagramSocket socket_send;
    public Vadio_send(String server_ip, int port){
        this.server_ip = server_ip;
        this.port = port;
    }

    public void send(ByteArrayOutputStream baos){
        try{
            socket_send = new DatagramSocket();
            InetAddress address = InetAddress.getByName(server_ip);
            DatagramPacket packet = new DatagramPacket(baos.toByteArray(),0,address,port);
            socket_send.send(packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
