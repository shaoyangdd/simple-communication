package com.jrx.data;

import javax.swing.*;
import java.nio.channels.SocketChannel;
import java.util.Properties;

/**
 * 全局状态
 */
public class GlobalStateData {

    /**
     * 传输按钮状态 false 未被点击；true 已被点击
     */
    public static volatile boolean transferButtonFlag = false;

    /**
     * 传输状态 0 未开始；1 传输中；2 已暂停；3 已取消
     */
    public static volatile int transferState = 0;

    public static Properties properties = null;

    public static volatile String readFileName = "";

    public static volatile String writeFileNamePath = "D:\\";

    public static volatile SocketChannel socketChannel = null;

    public static volatile String ip = null;

    public static volatile JProgressBar jProgressBar = null;

    public static volatile JButton jButton = null;


    public static volatile int percet = 0;

    /**
     * 传输中标志 false 未传输  true 传输中
     */
    public static volatile boolean transferFlag = false;


    // properties key---
    public static final String DEFUALT_RECV_PATH = "default-recieve-path";

}
