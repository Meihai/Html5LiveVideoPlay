package com.keydak.videoapp.common.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.io.File;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.keydak.videoapp.common.log.RootLogger;


public class AppInitListener implements ServletContextListener {

    Logger log=Logger.getLogger(AppInitListener.class);

    public void contextDestroyed(ServletContextEvent sce) {

    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            //一般项目启动时的初始化操作放在这里
            //例如日志加载，数据库连接初始化
            //数据库初始化
            URL url = AppInitListener.class.getResource("/log4j.properties");
            //%20为html的空格编码
            File file = new File(url.getPath());
            String path =  file.getPath().replaceAll("%20", " ");
            PropertyConfigurator.configure(path);
        } catch (Exception e) {
            e.printStackTrace();
            RootLogger.writeErrorLog("", e);
            System.exit(1);
        }
    }

}
