package com.jrx.transfer;


import com.jrx.data.GlobalStateData;
import com.jrx.window.ComponentFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

import static com.jrx.common.constant.Constant.RECIEVE_LOG;


public class NioServer {

    //通道管理器
    private Selector selector;

    ServerSocketChannel serverChannel = null;


    String showText = "";

    //获取一个ServerSocket通道，并初始化通道
    public NioServer init(int port) throws IOException {
        //获取一个ServerSocket通道
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(9999));
        //获取通道管理器
        selector = Selector.open();
        //将通道管理器与通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，
        //只有当该事件到达时，Selector.select()会返回，否则一直阻塞。
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        return this;
    }

    public void listen() throws IOException {

        JTextArea jTextField = (JTextArea) ComponentFactory.componentMap.get(RECIEVE_LOG);

        System.out.println("[server] 服务器端启动成功");

        //使用轮询访问selector
        while (true) {

            System.out.println("[server] selector.select()");
            jTextField.setText(showText = showText + "[server] selector.select()" + System.lineSeparator());
            //当有注册的事件到达时，方法返回，否则阻塞。
            selector.select();
            System.out.println("[server] selector.selected");
            jTextField.setText(showText = showText + "[server] selector.selected" + System.lineSeparator());
            //获取selector中的迭代器，选中项为注册的事件
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();

            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                System.out.println("[server] ite.next()");
                jTextField.setText(showText = showText + "[server] ite.next()" + System.lineSeparator());
                //删除已选key，防止重复处理
                ite.remove();
                //客户端请求连接事件
                if (key.isAcceptable()) {
                    //获得客户端连接通道
                    SocketChannel channel = serverChannel.accept();
                    channel.configureBlocking(false);
                    //向客户端发消息
                    channel.write(ByteBuffer.wrap("[server] 已经收到你的请求连接".getBytes()));
                    //在与客户端连接成功后，为客户端通道注册SelectionKey.OP_READ事件。
                    channel.register(selector, SelectionKey.OP_READ);
                    System.out.println("[server]客户端请求连接事件");
                    jTextField.setText(showText = showText + "[server]客户端请求连接事件" + System.lineSeparator());
                } else if (key.isReadable()) {//有可读数据事件
                    System.out.println("[server]收到可读消息...");
                    jTextField.setText(showText = showText + "[server]收到可读消息..." + System.lineSeparator());
                    //获取客户端传输数据可读取消息通道。
                    SocketChannel channel = (SocketChannel) key.channel();
                    FileOutputStream fos = null;
                    FileChannel fosChannel = null;
                    String fileName = "";
                    try {
                        //先读文件名
//                        ByteBuffer fileNameBuffer = ByteBuffer.allocate(100);
//                        if (!fileNameBuffer.hasRemaining()) {
//                            break;
//                        }
//                        channel.read(fileNameBuffer);
//                        String fileName = new String(fileNameBuffer.array(), "GBK").trim();
//                        System.out.println("[server] fileName: " + fileName);
                        File file = new File(GlobalStateData.writeFileNamePath);
                        fileName = file.getName();
                        if(!file.exists()) {
                            System.out.println("[server] 文件:" + GlobalStateData.writeFileNamePath + "不存在，创建空文件");
                            jTextField.setText(showText = showText + "[server] 文件:" + GlobalStateData.writeFileNamePath + "不存在，创建空文件" + System.lineSeparator());
                            file.createNewFile();
                        }
                        fos = new FileOutputStream(file);
                        fosChannel = fos.getChannel();

                        //创建读取数据缓冲器
                        ByteBuffer buffer = ByteBuffer.allocate(10);
                        int read;
                        long count = 0;
                        while ((read = channel.read(buffer)) != -1 && read != 0) {

                            buffer.flip();
//                            byte[] data = new byte[buffer.remaining()];
//                            buffer.get(data);
//
//                            String message = new String(data, "GBK");
//                            System.out.println("receive message from client, size:" + buffer.position() + " msg: " + message);
                            fosChannel.write(buffer);
                            buffer.clear();
                            count ++;
                        }
                        if (read < 0) {
                            System.out.println("[server] 非法key，关闭channel");
                            jTextField.setText(showText = showText + "[server] 非法key，关闭channel" + System.lineSeparator());
                            channel.close();
                        }
                        System.out.println("[server] count:" + count);
                        jTextField.setText(showText = showText + "[server] count:" + count + System.lineSeparator());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println("[server] 关闭流fos");
                        jTextField.setText(showText = showText + "[server] 关闭流fos" + System.lineSeparator());
                        channel.close();
                        if (fosChannel != null) {
                            fosChannel.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                        System.out.println("显示日志");
                        jTextField.setText("接收文件"+fileName+"成功");
                    }
                }
            }
        }
    }
}
