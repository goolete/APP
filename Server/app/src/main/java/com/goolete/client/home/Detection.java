package com.goolete.client.home;

import com.goolete.client.home.Data.Student;

import java.util.ArrayList;

public class Detection {
    private ArrayList list_gps;
    private ArrayList list_data = new ArrayList();
    private ArrayList list_data_falure = new ArrayList();
    private double latitude;
    private double longitude;

    public Detection(ArrayList list,double server_latitude,double server_longitude){
        this.list_gps = list;
        this.latitude = server_latitude;
        this.longitude = server_longitude;
    }

    public ArrayList detection(){
        /*
        删除重复的学生信息
         */
        for(int i = 0;i < list_gps.size();i++){
            for(int j = i+1;j < list_gps.size();j++){
                Student stu1 = (Student) list_gps.get(i);
                Student stu2 = (Student) list_gps.get(j);
                if(stu1.getIp().equals(stu2.getIp())){
                    list_gps.remove(j);
                }
            }
        }
        return list_gps;
    }

    public ArrayList compare(){
        /*
        对学生信息进行判断,保存在范围之内的gps信息
         */
        for(int i = 0;i < list_gps.size();i++){
            Student stu = (Student)list_gps.get(i);
            if((Math.abs(stu.getLatitude() - latitude) <= 0.0004 )&& (Math.abs(stu.getLongitude() - longitude) <= 0.0004 )){
                list_data.add(stu);
            }
        }
        return list_data;
    }
    public ArrayList compare_falure(){
        /*
        对学生信息进行判断,保存在范围之外的gps信息
         */
        for(int i = 0;i < list_gps.size();i++){
            Student stu = (Student)list_gps.get(i);
            if((Math.abs(stu.getLatitude() - latitude) > 0.0004 ) || (Math.abs(stu.getLongitude() - longitude) > 0.0004 )){
                list_data_falure.add(stu);
            }
        }
        return list_data_falure;
    }

    public boolean judget(int flag_connter_recive){
        if(flag_connter_recive == (list_data.size() - 1)){
            Student stu = (Student)list_gps.get(flag_connter_recive);
            if((Math.abs(stu.getLatitude() - latitude) <= 0.0004 )&& (Math.abs(stu.getLongitude() - longitude) <= 0.0004 )){
                return true;
            }
        }

        return false;
    }
}
