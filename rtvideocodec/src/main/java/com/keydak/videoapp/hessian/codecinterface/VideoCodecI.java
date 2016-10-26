package com.keydak.videoapp.hessian.codecinterface;

/**
 * Created by admin on 2016/10/25.
 */
public interface VideoCodecI {
    //启动websocket，提供密码,mpeg 视频流监听端口,websocket连接端口 或者FFmpeg
    public boolean startAnotherBat(String batPath);

}
