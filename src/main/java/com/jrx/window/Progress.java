package com.jrx.window;

import com.jrx.data.GlobalStateData;

import javax.swing.*;

public class Progress extends Thread {
    JProgressBar progressBar;
    JButton button;
    //进度条上的数字
    int[] progressValues = {6, 18, 27, 39, 51, 66, 81, 100};

    public Progress(JProgressBar progressBar, JButton button) {
        this.progressBar = progressBar;
        this.button = button;
    }

    public void run() {
//        for (int i = 0; i < progressValues.length; i++) {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            //设置进度条的值
//            progressBar.setValue(progressValues[i]);
//        }

        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("当前进度:" + GlobalStateData.percet);
            progressBar.setValue(GlobalStateData.percet);
            if (GlobalStateData.percet >= 100) {
                break;
            }
        }
        progressBar.setIndeterminate(false);
        progressBar.setString("传输完成！");
        button.setEnabled(true);
    }
}