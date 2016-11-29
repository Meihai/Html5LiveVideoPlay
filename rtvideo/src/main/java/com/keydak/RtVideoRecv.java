package com.keydak;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.awt.image.BufferedImage;

/**
 * Created by admin on 2016/10/17.
 */
public class RtVideoRecv extends MediaListenerAdapter {
    private String streamLocation=null;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private int frameNr=0; //帧数统计
    public RtVideoRecv(String streamLocation){
        this.streamLocation=streamLocation;
    }
    public String getStreamLocation(){
        return this.streamLocation;
    }

    public void setStreamLocation(String streamLocation){
        this.streamLocation=streamLocation;
    }

    public void decodeAndCaptureFrames(){
        IMediaReader mediaReader=ToolFactory.makeReader(streamLocation);
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
       // onVideoPicture(IVideoPictureEvent event);
        mediaReader.addListener(this);
        while(mediaReader.readPacket()==null && MjpegStreamingOp.running);
        mediaReader.close();
    }
    @Override
    public void onVideoPicture(IVideoPictureEvent event){
          BufferedImage frame=event.getImage();
          String streamId=String.valueOf(frame.hashCode());
          MjpegStreamingOp.getImages().put(streamId,frame);
          MjpegStreamingOp.imagesId=streamId;
          frameNr++;
         // System.out.println("frameNr="+frameNr);
          logger.info("frameNr="+frameNr);

    }


}
