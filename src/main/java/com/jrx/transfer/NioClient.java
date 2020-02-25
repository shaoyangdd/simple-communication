package com.jrx.transfer;

import com.jrx.data.GlobalStateData;
import com.jrx.window.Progress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioClient {

    //管道管理器
    private Selector selector;

    public NioClient init(String serverIp, int port) throws IOException {
        //获取socket通道
        SocketChannel channel = SocketChannel.open();

        channel.configureBlocking(false);
        //获得通道管理器
        selector = Selector.open();

        //客户端连接服务器，需要调用channel.finishConnect();才能实际完成连接。
        channel.connect(new InetSocketAddress(serverIp, port));
        //为该通道注册SelectionKey.OP_CONNECT事件
        channel.register(selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen() throws IOException {
        System.out.println("[client] 客户端启动");
        //轮询访问selector
        while (true) {

            //判断是否下载成功，如果下载成功就结束轮循
            if (GlobalStateData.percet >= 100) {
                System.out.println("[client]下载结束，结束轮循");
                break;
            }

            //选择注册过的io操作的事件(第一次为SelectionKey.OP_CONNECT)
            selector.select();
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                //删除已选的key，防止重复处理
                ite.remove();
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();

                    //如果正在连接，则完成连接
//                    if (channel.isConnectionPending()) {
//                        channel.finishConnect();
//                    }

                    while (!channel.finishConnect()) {

                    }
                    System.out.println("[client] 客户端连接成功");
                    channel.configureBlocking(false);
                    //连接成功后，注册接收服务器消息的事件
                    channel.register(selector, SelectionKey.OP_READ);
                    sendFile(channel);

//                    GlobalStateData.socketChannel = channel;

                } else if (key.isReadable()) { //有可读数据事件。
                    SocketChannel channel = (SocketChannel) key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    channel.read(buffer);
                    byte[] data = buffer.array();
                    String message = new String(data);

                    System.out.println("[client] recevie message from server:, size:" + buffer.position() + " msg: " + message);
//                    ByteBuffer outbuffer = ByteBuffer.wrap(("client.".concat(msg)).getBytes());
//                    channel.write(outbuffer);
                }
            }
        }
    }



    public static void sendFile(SocketChannel socketChannel) {
        //开启一个线程处理进度
        new Progress(GlobalStateData.jProgressBar, GlobalStateData.jButton).start();

        FileInputStream fin = null;
        FileChannel fileInputChannel = null;
        try {
            System.out.println("发文件"+ GlobalStateData.readFileName);
            fin = new FileInputStream(new File(GlobalStateData.readFileName));
            fileInputChannel = fin.getChannel();

            // 先把文件名发给server
//            String[] arr = GlobalStateData.readFileName.split(File.separator);
//            String fileName = arr[arr.length-1];
//            if (fileName.length() < 100) {
//                for (int i=0;i<100-fileName.length(); i++) {
//                    fileName.concat(" ");
//                }
//            }
//            System.out.println("[client] fileName.length: " + fileName.length());
//            socketChannel.write(ByteBuffer.wrap(fileName.getBytes()));

            // 再发文件内容给server
            long fileSize = fileInputChannel.size();
            int capacity = 10;// 字节
            ByteBuffer bf = ByteBuffer.allocate(capacity);
            System.out.println("[client] 限制是：" + bf.limit() + ",容量是：" + bf.capacity() + " ,位置是：" + bf.position());
            int length = -1;
            long count = 0;
            long processedSize = 0;
            while ((length = fileInputChannel.read(bf)) != -1) {


                byte[] bytes = bf.array();


//                            byte[] bytes = bf.array();
                String gbkStr = new String(bytes, "GBK");
                String utf8Str = new String(bytes, "UTF-8");

                System.out.println("gbkStr: " + gbkStr);
                System.out.println("utf8Str: " + utf8Str);





                count ++;
                processedSize += length;
                bf.flip();
                //向服务器发送消息
                socketChannel.write(bf);


//                            System.out.println("向服务器发送消息, " + gbkStr + " 限制是：" + bf.limit() + "容量是：" + bf.capacity() + "位置是：" + bf.position());
                /*
                 * 注意，读取后，将位置置为0，将limit置为容量, 以备下次读入到字节缓冲中，从0开始存储
                 */

                bf.clear();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GlobalStateData.percet = new BigDecimal(processedSize).divide(new BigDecimal(fileSize),2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).intValue();
            }


            System.out.println("[client] count:" + count);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                socketChannel.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            if( null != fileInputChannel) {
                try {
                    fileInputChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }




//    public static void main(String[] args) throws IOException {
//        new NioClient().init("127.0.0.1", 9981).listen();
//    }
}
