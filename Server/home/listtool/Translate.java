package com.goolete.client.home.listtool;

import com.goolete.client.home.Data.Student;

import java.util.ArrayList;

public class Translate {
    private ArrayList list_sucess = new ArrayList();
    private ArrayList list_falure = new ArrayList();
    private String [][] cons = {{""}, {""}};
    public Translate(ArrayList list_success,ArrayList list_falure){
        this.list_falure = list_falure;
        this.list_sucess = list_success;
    }

    public String [][] trans(){
        int a = list_falure.size();
        int b = list_sucess.size();

        for(int i = 0;i < Math.max(a,b);i++){
            if(list_sucess.size() != 0){
                Student stu = (Student) list_sucess.get(i);
                String name = stu.getName();
                int unmber = stu.getNumber();
                String ip = stu.getIp();
                String data = "{\"name\":\"" + name + "\",\"unmber\":\"" + unmber + "\",\"ip\":\"" + ip + "\"}";
                cons[0][i] = data;
            }else{
                cons[0][i] = "";
            }
        }

        for(int i = 0;i < Math.max(a,b);i++){
            if(list_falure.size() != 0 ){
                Student stu = (Student) list_falure.get(i);
                String name = stu.getName();
                int unmber = stu.getNumber();
                String ip = stu.getIp();
                String data = "{\"name\":\"" + name + "\",\"unmber\":\"" + unmber + "\",\"ip\":\"" + ip + "\"}";
                cons[1][i] = data;
            }else{
                cons[1][i] = "";
            }
        }
        return cons;
    }
}
