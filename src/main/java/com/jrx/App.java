package com.jrx;

import com.jrx.data.GlobalStateData;
import com.jrx.transfer.FileTransferServer;
import com.jrx.transfer.NioClient;
import com.jrx.transfer.NioServer;
import com.jrx.util.Logger;
import com.jrx.window.ComponentFactory;
import com.jrx.window.MainWindow;
import com.jrx.window.TransFileChooser;
import com.jrx.window.TransferButton;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

import static com.jrx.common.constant.Constant.DEFAULT_PORT;

public class App {

    public static void main(String[] args) throws IOException {
        // 读取配置文件
        Logger.info("读取配置文件开始");
        Properties properties = new Properties();
        String path = System.getProperty("user.dir");
        Logger.info("当前路径:" + path);
        BufferedReader bufferedReader0 = new BufferedReader(new FileReader(path + File.separator + "config.properties"));
        properties.load(bufferedReader0);
        GlobalStateData.properties = properties;
        Enumeration propertyNames =  properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String)propertyNames.nextElement();
            Logger.info(key + ":" + properties.get(key));
            if ("default-recieve-path".equals(key)) {
                GlobalStateData.writeFileNamePath = (String) properties.get(key);
            }
        }
        Logger.info("读取配置文件结束");
        JFrame jFrame = new MainWindow();

        // 0. 设置菜单
        jFrame.setJMenuBar(ComponentFactory.getJmenu());

        // 1. 选择文件的Panel
        JPanel jPanelSelectFile = new JPanel();    //创建一个JPanel对象
        JLabel jl = new JLabel("请选择要传输的文件");    //创建一个标签
        jPanelSelectFile.setBackground(Color.PINK);    //设置背景色
        jPanelSelectFile.add(jl);    //将标签添加到面板
        // 1.1 文件选择框框
        TransFileChooser transFileChooser = new TransFileChooser();
        jPanelSelectFile.add(transFileChooser.getLabel());
        jPanelSelectFile.add(transFileChooser.getJtf());
        jPanelSelectFile.add(transFileChooser.getButton());

        // 2. 选择传输目标的panel
        JPanel jPanelTransfer = new JPanel();
        jPanelTransfer.setBackground(Color.orange);
        JLabel label = new JLabel("请输入文件发送目标IP: ");
        JTextField jtf = new JTextField(25);
        jtf.setToolTipText("请输入指定的IP");
        jPanelTransfer.add(label);
//        jPanelTransfer.add(ComponentFactory.getSelectSerarchTypeComboBox());
        jPanelTransfer.add(jtf);
        //文件传输按钮
        TransferButton transferButton = new TransferButton();
        GlobalStateData.jButton= transferButton.getjButton();
        jPanelTransfer.add(GlobalStateData.jButton);

//        jFrame.add(jPanel, BorderLayout.NORTH);    //将面板添加到窗口
//        jFrame.add(jPanel1, BorderLayout.WEST);
//        jFrame.add(jPanel2, BorderLayout.EAST);




        //设置进度条panel
//        GlobalStateData.jProgressBar = ComponentFactory.getJprogressBar(transferButton.getjButton());
//        JPanel progressBarPanel = new JPanel();
//        progressBarPanel.setBackground(Color.pink);
//        progressBarPanel.add(ComponentFactory.getTransferProcessLabel());
//        progressBarPanel.add(GlobalStateData.jProgressBar);
//        progressBarPanel.add(ComponentFactory.getPauseButton());
//        progressBarPanel.add(ComponentFactory.getRemainButton());
//        progressBarPanel.add(ComponentFactory.getCancelButton());

        // 3.接收文件panel
        JPanel recieveFilePanel = new JPanel();
        recieveFilePanel.setSize(800, 800);
        recieveFilePanel.add(ComponentFactory.getRecieveFileLog());

        jFrame.setVisible(true);    //设置窗口可见
        jFrame.setLayout(new FlowLayout());    //为Frame窗口设置布局为BorderLayout
        jFrame.add(jPanelSelectFile);
        jFrame.add(jPanelTransfer);
//        jFrame.add(progressBarPanel);
        jFrame.add(recieveFilePanel);


        new Thread(new Runnable() {
            @Override
            public void run() {
                //启动server
                try {
//                    new NioServer().init(DEFAULT_PORT).listen();
                    FileTransferServer server = new FileTransferServer(); // 启动服务端
                    server.load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
