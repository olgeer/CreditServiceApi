package com.sword.common;

import org.apache.logging.log4j.LogManager;

/**
 * Logger object of the common
 * @author max
 */
public class Logger{
    private org.apache.logging.log4j.Logger logger;
    private String sessionId;
    private String className;

    public Logger(Class logClass) {
        this.logger = LogManager.getLogger(logClass);
        className=logClass.getName();
        sessionId=ShortKey.newKey();
    }


    public Logger(Class logClass,String sId) {
        this.logger = LogManager.getLogger(logClass);
        className=logClass.getName();
        sessionId=sId;
    }

    public static Logger newInstance(Class logClass) {
        return new Logger(logClass);
    }

    public static Logger newInstance(Class logClass,String sId) {
        return new Logger(logClass,sId);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void fatal(String s) {
        logger.fatal(s);
    }

    public void error(String s) {
        logger.error(s);
    }

    public void warn(String s) {
        logger.warn(s);
    }

    public void debug(String s) {
        logger.debug(s);
    }

    public void info(String s) {
        logger.info(s);
    }

    public void trace(String s) {
        logger.trace(s);
    }

    public static void console(String s){
        System.out.println(Util.logTime()+" CONSOLE - " + s);
    }
}
