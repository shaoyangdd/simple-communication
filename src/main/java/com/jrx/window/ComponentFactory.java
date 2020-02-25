package com.jrx.window;

import com.jrx.util.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static com.jrx.common.constant.Constant.RECIEVE_LOG;

public class ComponentFactory {


    /**
     * 组件容器 (server 和 client处理的时候需要回调容器作一些显示动作，所以设计一个容器)
     */
    public static final Map<String, Component> componentMap = new HashMap<>();

    public static JComboBox getSelectSerarchTypeComboBox() {
        JComboBox cmb = new JComboBox();    //创建JComboBox
        cmb.addItem("指定姓名IP");
        cmb.addItem("按姓名查找");
        cmb.addItem("批量添加");
        return cmb;
    }


    public static JMenuBar getJmenu() {
        JMenuBar jMenuBar = new JMenuBar();
//        jMenuBar.add(createFileMenu());
//        jMenuBar.add(createEditMenu());
        return jMenuBar;
    }

    //定义“文件”菜单
    private static JMenu createFileMenu() {
        JMenu menu = new JMenu("文件传输模式");
        menu.setMnemonic(KeyEvent.VK_F);    //设置快速访问符
        JMenuItem item = new JMenuItem("我做为客户端发文件", KeyEvent.VK_N);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        menu.add(item);
        item = new JMenuItem("我做为服务端收文件", KeyEvent.VK_O);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        menu.add(item);
        item = new JMenuItem("我即是客户端也是服务端", KeyEvent.VK_S);
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        menu.add(item);
        menu.addSeparator();
        JMenuItem jMenuItemQuit = new JMenuItem("退出(E)", KeyEvent.VK_E);
        jMenuItemQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
        jMenuItemQuit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("退出");
                System.exit(0);
            }
        });
        menu.add(jMenuItemQuit);
        return menu;
    }

//    /**
//     * 定义“设置”菜单
//     */
//    private static JMenu createEditMenu() {
//        JMenu menu = new JMenu("设置");
//        menu.setMnemonic(KeyEvent.VK_E);
//        JMenuItem fileRecvPathItem = new JMenuItem("文件接收路径", KeyEvent.VK_U);
//        menu.add(fileRecvPathItem);
//        menu.addSeparator();
//        JMenuItem ipNameMapItem = new JMenuItem("IP姓名映射表", KeyEvent.VK_T);
//        menu.add(ipNameMapItem);
//        JMenuItem timeoutItem = new JMenuItem("文件传输超时时间", KeyEvent.VK_C);
//        menu.add(timeoutItem);
//        menu.addSeparator();
//        JMenuItem jMenuItem = new JMenuItem("BA开发组", KeyEvent.VK_C);
//        menu.add(jMenuItem);
//        JCheckBoxMenuItem cbMenuItem = new JCheckBoxMenuItem("启动同步BA文档");
//        menu.add(cbMenuItem);
//        return menu;
//    }


    public static JProgressBar getJprogressBar(JButton button) {
        //创建一个最小值是0，最大值是100的进度条
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        //如果不需要进度上显示“升级进行中...”，可注释此行
        progressBar.setString("传输中...");

        //单机“完成”按钮结束程序
//        button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
////                dispose();
//                System.exit(0);
//            }
//        });
        return progressBar;
    }


    public static JButton getPauseButton() {
        JButton jButton = new JButton();
        jButton.setText("暂停");
        jButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //TODO 暂停按钮被点击，执行暂停操作

                System.out.println("暂停按钮被点击");

            }
        });
        return jButton;
    }

    public static JButton getRemainButton() {
        JButton jButton = new JButton();
        jButton.setText("继续");
        jButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //TODO 继续按钮被点击，执行暂停操作

                System.out.println("继续按钮被点击");

            }
        });
        return jButton;
    }

    public static JButton getCancelButton() {
        JButton jButton = new JButton();
        jButton.setText("取消");
        jButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //TODO 取消按钮被点击，执行暂停操作

                System.out.println("取消按钮被点击");

            }
        });
        return jButton;
    }

    public static JLabel getTransferProcessLabel() {
        JLabel jLabel = new JLabel();
        jLabel.setText("传输进度:");
        return jLabel;
    }


    public static JLabel getSplitLineLabel() {
        JLabel jLabel = new JLabel();
        jLabel.setText("---------------------------------------------------------");
        return jLabel;
    }

    public static JTextArea getRecieveFileLog() {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setSize(400,800);
        jTextArea.setLineWrap(true);    //设置文本域中的文本为自动换行
        jTextArea.setForeground(Color.BLACK);    //设置组件的背景色
        jTextArea.setFont(new Font("楷体",Font.BOLD,16));    //修改字体样式
        jTextArea.setBackground(Color.YELLOW);    //设置按钮背景色

        componentMap.put(RECIEVE_LOG,jTextArea);
        return jTextArea;
    }
}
