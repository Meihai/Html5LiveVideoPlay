package com.keydak.rtsp2rtmp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IContainer;
import com.xuggle.xuggler.IVideoPicture;
/**
 * Created by admin on 2016/10/18.
 */
public class RTSPReader extends MediaListenerAdapter implements Runnable{
    private Logger logger = LoggerFactory.getLogger(RTSPReader.class);
    private IMediaReader mediaReader;
    private int frameSkip;
    private int groupSize;
    private long frameNr; // number of the frame read so far
    private boolean running = false;
    // used to determine if the EOF was reached if Xuggler does not detect it
    private long lastRead = -1;
    private int sleepTime = 0;
    private String streamName;
    private String streamLocation = null;
    private RTMPWriter rtmpWriter = null;
    Double frameRate = 0.0;

    // frameskip和groupsize决定了需要读取的帧的数目，即采样率，
    // 例如，1,1时代表每一帧都需要读取，10,5时代表只需要[0 1 2 3 4] [10 11 12 13 14] ...
    public RTSPReader(String streamName, String streamLocation,
                      int frameSkip, int groupSize, int sleepTime) {
        this.streamLocation = streamLocation;
        this.frameSkip = Math.max(1, frameSkip);
        this.groupSize = Math.max(1, groupSize);
        this.sleepTime = sleepTime;
        this.streamName = streamName;
        lastRead = System.currentTimeMillis() + 10000;
    }

    /**
     * Start reading the provided URL
     */
    public void run() {
        running = true;
        while (running) {
            try {
                // if a url was provided read it
                if (streamLocation != null) {
                    logger.info("Start reading stream: " + streamLocation);
                    mediaReader = ToolFactory.makeReader(streamLocation);
                    lastRead = System.currentTimeMillis() + 10000;
                    frameNr = 0;
                    rtmpWriter = new RTMPWriter(576,704,30.0,streamName);
                    mediaReader.addListener(this);
                    while (mediaReader.readPacket() == null && running);
                    // reset internal state
                    rtmpWriter.setFinish();
                    mediaReader.close();
                } else {
                    logger.error("No stream provided, nothing to read");
                    break;
                }
            } catch (Exception e) {
                logger.warn("Stream closed unexpectatly: " + e.getMessage(), e);
                // sleep a minute and try to read the stream again
                sleep(60 * 1000);
            }
        }
        running = false;
    }

    public void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets called when FFMPEG transcoded a frame
     */
    public void onVideoPicture(IVideoPictureEvent event) {
        lastRead = System.currentTimeMillis();
        if (frameNr % frameSkip < groupSize) {
            IVideoPicture picture = event.getPicture();
            rtmpWriter.write(frameNr, picture);
            // enforced throttling
            if (sleepTime > 0)
                sleep(sleepTime);
        }
        frameNr++;
    }
    /**
     * Tells the StreamReader to stop reading frames
     */
    public void stop() {
        running = false;
    }

    /**
     * Returns whether the StreamReader is still active or not
     * @return
     */
    public boolean isRunning() {
        // kill this thread if the last frame read is to long ago
        // (means Xuggler missed the EoF) and clear resources
        if (lastRead > 0 && System.currentTimeMillis() - lastRead > 3000) {
            running = false;
            if (mediaReader != null && mediaReader.getContainer() != null)
                mediaReader.getContainer().close();
            return this.running;
        }
        return true;
    }
}




