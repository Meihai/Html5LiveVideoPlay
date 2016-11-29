package com.keydak.test;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by admin on 2016/10/17.
 */
@XmlRootElement
public class Reader implements Serializable{

    String name;
    String password;

    public Reader(){

    };
    public Reader(String name,String password){
        this.name=name;
        this.password=password;
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
        return this.password;
    }

    public String toString(){
        return "Name:"+name+",Password:"+password;
    }


}
