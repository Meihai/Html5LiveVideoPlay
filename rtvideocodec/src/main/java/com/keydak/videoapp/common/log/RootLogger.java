package com.keydak.videoapp.common.log;

import org.apache.log4j.Logger;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * 记录root类别的log日志
 * @author yangcao
 *
 */
public class RootLogger extends MyLogger{

	static{
		//logger = LoggerFactory.getLogger(RootLogger.class); 
		logger = Logger.getLogger("ROOT");
	}
	
	public static void writeErrorLog(String msg , Throwable t){
		StringBuffer logBuffer = new StringBuffer();
		if(msg!=null)
		    logBuffer.append(msg);
		//do something  
		if(t!=null){
			logBuffer.append(t.getStackTrace());
		}		
		//TODO
		error(logBuffer.toString(),t);
	}
	
	public static void writeWarnLog(String msg ){
		StringBuffer logBuffer = new StringBuffer();
		//do something  
		logBuffer.append(msg);
		//TODO
		warn(logBuffer.toString());
	}
	
	public static void writeInfoLog(String msg ){
		StringBuffer logBuffer = new StringBuffer();
		//do something  
		logBuffer.append(msg);
		//TODO
		info(logBuffer.toString());
	}
	
	
}
