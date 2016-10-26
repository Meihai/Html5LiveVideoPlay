package com.keydak.videoapp.hessian.client;
import com.caucho.hessian.client.HessianProxyFactory;
import com.keydak.videoapp.hessian.codecinterface.VideoCodecI;

import java.net.MalformedURLException;

/**
 * Created by admin on 2016/10/25.
 */
public class VideoCodecClient {
    //项目名+服务名
    public static  String url = "http://192.168.0.132:8085/rtvideocodec/hessianService";
    public static  HessianProxyFactory  factory=new HessianProxyFactory();
    public static VideoCodecI videoCodecIins;
    public static void main(String[] args){
        try{
            videoCodecIins  = (VideoCodecI) factory.create(VideoCodecI.class, url);
            WebSocketStart wsins=new WebSocketStart();
            new Thread(wsins).start();
            FFMpegStart ffins=new  FFMpegStart();
            new Thread(ffins).start();
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
    }

     static class  WebSocketStart implements  Runnable{
        public void run(){
            try{
                videoCodecIins.startAnotherBat("E:/MyGitProject/jsmpeg/startWebSocket.bat");
                System.out.println("启动WebSocket成功");
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
    static class FFMpegStart implements Runnable{
        public void run(){
            try{
                videoCodecIins.startAnotherBat("startFFmpeg.bat");
                System.out.println("成功运行FFmpeg程序对RTSP实时视频流进行解码");
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }



}
