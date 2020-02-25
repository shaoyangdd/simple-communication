package com.jrx.window;

import com.jrx.data.GlobalStateData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class TransFileChooser {

    private static final Logger logger = Logger.getLogger("TransFileChooser");

    private JLabel label = new JLabel("所选文件路径：");
    private JTextField jtf = new JTextField(25);
    private JButton button = new JButton("浏览");

    public TransFileChooser() {
//        JFrame jf = new JFrame("文件选择器");
//        JPanel panel = new JPanel();
//        panel.add(label);
//        panel.add(jtf);
//        panel.add(button);
//        jf.add(panel);
//        jf.pack();    //自动调整大小
//        jf.setVisible(true);
//        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button.addActionListener(new MyActionListener());    //监听按钮事件
    }

    //Action事件处理
    class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            JFileChooser fc = new JFileChooser("F:\\");
            int val = fc.showOpenDialog(null);    //文件打开对话框
            if (val == fc.APPROVE_OPTION) {
                //正常选择文件
                jtf.setText(fc.getSelectedFile().toString());
                logger.info("选择文件".concat(fc.getSelectedFile().toString()));
                GlobalStateData.readFileName = fc.getSelectedFile().toString();
            } else {
                //未正常选择文件，如选择取消按钮
                jtf.setText("未选择文件");
                logger.info("未选择文件");
            }
        }
    }

//    public static void main(String[] args) {
//        new TransFileChooser();
//    }


    public JLabel getLabel() {
        return label;
    }


    public JTextField getJtf() {
        return jtf;
    }


    public JButton getButton() {
        return button;
    }

}