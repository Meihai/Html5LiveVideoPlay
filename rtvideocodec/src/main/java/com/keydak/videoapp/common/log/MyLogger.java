package com.keydak.videoapp.common.log;

//import org.slf4j.Logger;
import org.apache.log4j.Logger;


public abstract class MyLogger {
	protected static Logger logger;
	
	public static void error(String msg){
		logger.error(msg);
	}
	public static void error(String msg,Throwable t){
		logger.error(msg, t);
	}
	
	public static void warn(String msg){
		logger.warn(msg);
	}
	public static void warn(String msg,Throwable t){
		logger.warn(msg, t);
	}
	
	public static void info(String msg){
		logger.info(msg);
	}
	public static void info(String msg,Throwable t){
		logger.info(msg, t);
	}
	
}
