package com.keydak;

/**
 * Created by admin on 2016/10/17.
 */
public class VideoDecodeThread implements Runnable{
    public void run(){
        String streamLocation="rtsp://admin:admin@192.168.0.189:554/h264/ch1/main/av_stream/";
        RtVideoRecv insRtVideoRecv=new RtVideoRecv(streamLocation);
        insRtVideoRecv.decodeAndCaptureFrames();
    }

}
