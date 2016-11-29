package com.keydak.test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016/11/29.
 */
public class HashMapTest {
    private static Cache<String, String> images = CacheBuilder.newBuilder()
            .expireAfterWrite(20, TimeUnit.SECONDS) //获取的图片存放20秒钟后过期
            .build();;
    public static void main(String[] args){
        HashMap<Integer,String> map=new HashMap<Integer,String>();
        String a="hello";
        String b="nihao";
        String c="chifabla";

        images.put(String.valueOf(a.hashCode()),a);
        images.put(String.valueOf(b.hashCode()),b);
        images.put(String.valueOf(c.hashCode()),c);
        images.put("luanle","luanle");
        for(String ins:images.asMap().keySet()){
            System.out.println(ins+":"+images.getIfPresent(ins));
        }

    }
}
