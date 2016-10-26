package com.keydak.videoapp.hessian.service;
import com.caucho.hessian.server.HessianServlet;
import com.keydak.videoapp.common.log.RootLogger;
import com.keydak.videoapp.hessian.codecinterface.VideoCodecI;
import java.io.File;
/**
 * Created by admin on 2016/10/25.
 */
public class VideoCodecService extends HessianServlet implements VideoCodecI{
    private static final long serialVersionUID = 8896421877705572111L;
    public boolean startAnotherBat(String batPath){
        File batFile=new File(batPath);
        if(!batFile.exists()){
            return false;
        }
        Runtime r=Runtime.getRuntime();
        try{
            r.exec(batPath).waitFor();
        }catch(Exception e){
            e.printStackTrace();
            RootLogger.writeErrorLog("运行脚本:"+batPath+"失败", e);
            return false;
        }
        return true;
    }
}
