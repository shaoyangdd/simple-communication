package com.jrx.transfer;

import com.jrx.data.GlobalStateData;
import com.jrx.window.ComponentFactory;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Date;

import static com.jrx.common.constant.Constant.RECIEVE_LOG;
import static com.jrx.common.constant.Constant.SERVER_PORT;

/**
 * 文件传输Client端<br>
 * 功能说明：
 *
 * @author 大智若愚的小懂
 * @Date 2016年09月01日
 * @version 1.0
 */
public class FileTransferClient extends Socket {
 

 
    private Socket client;
 
    private FileInputStream fis;
 
    private DataOutputStream dos;

    private JTextArea jTextField;

    /**
     * 构造函数<br/>
     * 与服务器建立连接
     * @throws Exception
     */
    public FileTransferClient(String ip) throws Exception {
        super(ip, SERVER_PORT);
        this.client = this;
        jTextField = (JTextArea) ComponentFactory.componentMap.get(RECIEVE_LOG);
        jTextField.setText("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");
        System.out.println("Cliect[port:" + client.getLocalPort() + "] 成功连接服务端");
    }
 
    /**
     * 向服务端传输文件
     * @throws Exception
     */
    public void sendFile(String fileName) throws Exception {
        try {

            File file = new File(fileName);
            if(file.exists()) {
                fis = new FileInputStream(file);
                dos = new DataOutputStream(client.getOutputStream());
 
                // 文件名和长度
                dos.writeUTF(file.getName());
                dos.flush();
                dos.writeLong(file.length());
                dos.flush();
 
                // 开始传输文件
                jTextField.setText(jTextField.getText() + System.lineSeparator() + "======== 开始传输文件 ========" + System.lineSeparator() );
                System.out.println(new Date() + ": ======== 开始传输文件 ========");
                long start = System.currentTimeMillis();
                byte[] bytes = new byte[1024 * 1024 * 10];
                int length = 0;
                long progress = 0;
                long lastProgressPercent = 0;
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dos.write(bytes, 0, length);
                    dos.flush();
                    progress += length;
                    System.out.print("| " + (100*progress/file.length()) + "% |");
                    long precent = (100*progress/file.length());
                    if (precent - lastProgressPercent >= 10) {
                        //增长大于10%就显示一次进度
                        lastProgressPercent = precent;
                        jTextField.setText(jTextField.getText() + "| " + (100*progress/file.length()) + "% |");
                    }
                }
                System.out.println();
                jTextField.setText(jTextField.getText() + System.lineSeparator() +"======== 文件传输成功 ========,耗时:" + (System.currentTimeMillis()-start)/1000 + "秒");
                System.out.println("======== 文件传输成功 ========,耗时:" + (System.currentTimeMillis()-start)/1000 + "秒");
                GlobalStateData.transferFlag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null)
                fis.close();
            if(dos != null)
                dos.close();
            client.close();
        }
    }
 
    /**
     * 入口
     * @param args
     */
//    public static void main(String[] args) {
//        try {
//            FileTransferClient client = new FileTransferClient(); // 启动客户端连接
//            client.sendFile("/Users/kangshaofei/Documents/project/simple-communication/bak/流程图.png"); // 传输文件
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
 
}

