package com.jrx.util;

/**
 *
 */
public class Logger {

    public static void info (String info) {
        //TODO 判断日志级别，文件还是控制台或mq
        System.out.println(info);
    }
}
