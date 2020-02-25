package com.jrx.window;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {


    public MainWindow() throws HeadlessException {
        setTitle("文件传输同步工具");    //设置显示窗口标题
        setSize(700, 500);    //设置窗口显示尺寸
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //置窗口是否可以关闭
//        JLabel jl = new JLabel("标签1");    //创建一个标签
//        Container c = getContentPane();    //获取当前窗口的内容窗格
//        c.add(jl);    //将标签组件添加到内容窗格上
        setVisible(true);    //设置窗口是否可见
    }


}
