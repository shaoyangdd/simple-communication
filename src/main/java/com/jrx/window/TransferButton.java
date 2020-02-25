package com.jrx.window;

import com.jrx.common.constant.Constant;
import com.jrx.data.GlobalStateData;
import com.jrx.transfer.FileTransferClient;
import com.jrx.transfer.NioClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;

public class TransferButton {

    private JButton jButton = new JButton();

    private static final Logger logger = Logger.getLogger("TransferButton");

    public JButton getjButton() {
        jButton.setName("发送");
        jButton.setText("发送");
        jButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                logger.info("确认按钮被点击");
                synchronized (this) {
                    if (GlobalStateData.transferFlag) {
                        logger.info("正在传输中，不能再操作");
                        return;
                    }
                    GlobalStateData.transferFlag = true;
                }

                //获取IP地址
                final Component component2 = jButton.getParent().getComponent(1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //启动client
                        try {
//                            new NioClient().init(((JTextField)component2).getText(), Constant.DEFAULT_PORT).listen();
                            new FileTransferClient(((JTextField)component2).getText()).sendFile(GlobalStateData.readFileName);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        return jButton;
    }

    public void setjButton(JButton jButton) {
        this.jButton = jButton;
    }
}
