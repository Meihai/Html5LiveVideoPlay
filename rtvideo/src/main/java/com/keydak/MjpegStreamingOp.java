package com.keydak;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
//import com.xuggle.mediatool.IMediaReader;
//import com.xuggle.mediatool.ToolFactory;
//import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.ApplicationAdapter;
import com.sun.net.httpserver.HttpServer;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2016/10/13.
 */
@Path("/streaming")
public class MjpegStreamingOp extends Application {
    //图片缓存存放
    private static Cache<String, BufferedImage> images = null;
    //图片id存放队列
    public static String imagesId=null;
   // private Logger logger = LoggerFactory.getLogger(getClass());
    private HttpServer server;
    private int port = 8558;
    private int frameRate = 25;  // this parameter decide the sleep time
    public static boolean running=true;

    public static void main(String[] args){
        MjpegStreamingOp insMjpegStreaming=new MjpegStreamingOp().port(8558).framerate(25);
        try{
            insMjpegStreaming.prepare();
            System.out.println("启动成功");
            String streamLocation="rtsp://admin:admin@192.168.0.189:554/h264/ch1/main/av_stream/";
            RtVideoRecv insRtVideoRecv=new RtVideoRecv(streamLocation);
            insRtVideoRecv.decodeAndCaptureFrames();
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }catch(IOException e1){
            e1.printStackTrace();
        }
       finally{
            running=true;
        }


    }

    public static Cache<String,BufferedImage> getImages(){
        if(images==null){
            images=CacheBuilder.newBuilder()
                    .expireAfterWrite(20, TimeUnit.SECONDS) //获取的图片存放20秒钟后过期
                    .build();
        }
        return images;
    }



    public MjpegStreamingOp port(int nr) {
        this.port = nr;
        return this;
    }

    public MjpegStreamingOp framerate(int nr) {
        this.frameRate = nr;
        return this;
    }

    public void prepare() throws IllegalArgumentException, IOException {
        //此处需要修改为从rtsp读进来的images存放的类
        images = MjpegStreamingOp.getImages();
        ApplicationAdapter connector = new ApplicationAdapter(
                new MjpegStreamingOp());
        server = HttpServerFactory.create("http://localhost:" + port + "/",
                connector);
        server.start();
    }
    /**
     * Sets the classes to be used as resources for this application
     */
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();
        s.add(MjpegStreamingOp.class);
        return s;
    }

    public void deactivate() {
        server.stop(0);
        images.invalidateAll();
        images.cleanUp();
    }

    @GET
    @Path("/streams")
   // @Produces("text/plain")
    @Produces(MediaType.APPLICATION_JSON)
    public String getStreamIds() throws IOException {
        String result = new String();
        //result+="picture listing as follows:";

        for (String id : images.asMap().keySet()) {
            result += "/streaming/picture/" + id + ".jpeg\r\n";
        }
        System.out.println("\r\n");
        for (String id : images.asMap().keySet()) {
            result += "/streaming/mjpeg/" + id + ".mjpeg\r\n";
        }
        return result;
    }

    @GET
    @Path("/picture/{streamid}.jpeg")
    @Produces("image/jpg")
    public Response jpeg(@PathParam("streamid") final String streamId) {
        BufferedImage image = null;
        if ((image = images.getIfPresent(streamId)) != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "jpg", baos);
                byte[] imageData = baos.toByteArray();
                return Response.ok(imageData).build(); // non streaming
                // return Response.ok(new
                // ByteArrayInputStream(imageDAta)).build(); // streaming
            } catch (IOException ioe) {
               // logger.warn("Unable to write image to output", ioe);
                ioe.printStackTrace();
                return Response.serverError().build();
            }
        } else {
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/playmultiple")
    @Produces("text/html")
    public String showPlayers(@DefaultValue("1") @QueryParam("cols") int cols,
                              @DefaultValue("0") @QueryParam("offset") int offset,
                              @DefaultValue("1") @QueryParam("number") int number)
            throws IOException {
        // number = Math.min(6, number);
        String result = "<html><head><title>Mjpeg stream players" +
                " </title></head><body bgcolor=\"#3C3C3C\">";
        result += "<font style=\"color:#CCC;\">Streams: " + images.size()
                + " (showing " + offset + " - "
                + Math.min(images.size(), offset + number) + ")</font><br/>";
        result += "<table style=\"border-spacing:0; "+
                  " border-collapse: collapse;\"><tr>";
        int videoNr = 0;
        for (String id : images.asMap().keySet()) {
            if (videoNr < offset) {
                videoNr++;
                continue;
            }
            if (videoNr - offset > 0 && (videoNr - offset) % cols == 0) {
                result += "</tr><tr>";
            }
            result += "<td><video poster=\"mjpeg/"
                    + id
                    + ".mjpeg\">"
                    + "Your browser does not support the video tag.</video></td>";
//            result +=
//             "<td><img src=\"http://"+InetAddress.getLocalHost().getHostAddress()
//            +":"+port+"/streaming/mjpeg/"+id+".mjpeg\"></td>";
            if (videoNr > offset + number)
                break;
            videoNr++;
        }
        result += "</tr></table></body></html>";
        return result;
    }

    @GET
    @Path("/play")
    @Produces("text/html")
    public String showPlayers(@QueryParam("streamid") String streamId)
            throws IOException {
        String result = "<html><head><title>Mjpeg stream: " + streamId
                + "</title></head><body bgcolor=\"#3C3C3C\">";
        result += "<font style=\"color:#CCC;\"><a href=\"tiles\">Back</a></font><br/>";
        result += "<table style=\"border-spacing:0; border-collapse: collapse;\"><tr>";
        result += "<video poster=\"mjpeg/" + streamId + ".mjpeg\">"
                + "Your browser does not support the video tag.</video>";
        return result;
    }

    @GET
    @Path("/tiles")
    @Produces("text/html")
    public String showTiles(@DefaultValue("3") @QueryParam("cols") int cols,
                            @DefaultValue("-1") @QueryParam("width") float width)
            throws IOException {
      //  String result="<%@ page language=\"java\" import=\"com.keydak.MjpegStreamingOp\" pageEncoding=\"UTF-8\"%>";
      String   result = "<html><head><title>Mjpeg stream players</title>";

        result += "</head><body bgcolor=\"#3C3C3C\">";
   //    result +="<script language=\"JavaScript\"> function myrefresh() { window.location.reload();}setInterval('myrefresh()',30); </script>";
   //     result +="<script language=\"JavaScript\"> function myrefresh() { <td><a href=\"play?streamid="+imagesId+"\"><img src=\"picture/"+imagesId+".jpeg"+"/></a>}</script>";
      //  result +="<script language=\"JavaScript\"> function myrefresh() { <td><a href=\"play?streamid=${MjpegStreamingOp.imagesId}\"><img src=\"picture/${MjpegStreamingOp.imagesId}.jpeg\"/></a>} setInterval('myrefresh()',30); </script>";
        result += "<table style=\"border-spacing:0; border-collapse: collapse;\"><tr>";
 //       int videoNr = 0;
//        for (String id : images.asMap().keySet()) {
//            if (videoNr > 0 && videoNr % cols == 0) {
//                result += "</tr><tr>";
//            }
//            result += "<td><a href=\"play?streamid=" + id
//                    + "\"><img src=\"picture/" + id + ".jpeg\" "
//                    + (width > 0 ? "width=\"" + width + "\"" : "") + "/></a>";
//            videoNr++;
//            break;
//        }
//       result+="<script language=\"JavaScript\"> setInterval('myrefresh()',30); </script>";
        if(imagesId!=null){
            result += "<td><a href=\"play?streamid=" + imagesId
                   + "\"><img src=\"picture/" + imagesId + ".jpeg\" "
                   + (width > 0 ? "width=\"" + width + "\"" : "") + "/></a>";
        }
        result += "</tr></table></body></html>";
        return result;
    }

    @GET
    @Path("/mjpeg/{streamid}.mjpeg")
    @Produces("multipart/x-mixed-replace; boundary=--BoundaryString\r\n")
    public Response mjpeg(@PathParam("streamid") final String streamId) {
         StreamingOutput output = new StreamingOutput() {
             private BufferedImage prevImage = null;
             private int sleep = 1000 / frameRate;
             @Override
             public void write(OutputStream outputStream) throws IOException,
                    WebApplicationException {
                  BufferedImage image = null;
                  try {
                       while ((image = images.getIfPresent(streamId)) != null)
                            if (prevImage == null || !image.equals(prevImage)) {
                                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                 ImageIO.write(image, "jpg", baos);
                                 byte[] imageData = baos.toByteArray();
                                 outputStream.write(("--BoundaryString\r\n"
                                            + "Content-type: image/jpeg\r\n"
                                            + "Content-Length: "
                                            + imageData.length + "\r\n\r\n")
                                            .getBytes());
                                 outputStream.write(imageData);
                                 outputStream.write("\r\n\r\n".getBytes());
                                 outputStream.flush();
                            }
                       Thread.sleep(sleep);
                       outputStream.flush();
                      outputStream.close();
                   } catch (IOException ioe) {
                       //  logger.info("Stream for [" + streamId + "] closed by client!");
                      ioe.printStackTrace();
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
             }
         };
         return Response.ok(output).header("Connection", "close")
        .header("Max-Age", "0").header("Expires", "0")
        .header("Cache-Control", "no-cache, private")
        .header("Pragma", "no-cache").build();
    }
}