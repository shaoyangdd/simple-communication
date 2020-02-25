package com.jrx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class Client {
	public static void main(String[] args) {
//		for (int i = 0; i < 1; i++) {
//			// 模拟三个发端
//			new Thread() {
//				public void run() {
					try {
						SocketChannel socketChannel = SocketChannel.open();
						socketChannel.configureBlocking(true);
						socketChannel.socket().connect(new InetSocketAddress("192.168.1.6", 8888));
						File file = new File("/Users/kangshaofei/Documents/project/simple-communication/bak/流程图.png");
						FileChannel fileChannel = new FileInputStream(file).getChannel();
						ByteBuffer buffer = ByteBuffer.allocate(10000);

//						socketChannel.read(buffer);
//						buffer.flip();
//						System.out.println(new String(buffer.array(), 0, buffer.limit(), Charset.forName("utf-8")));
//						buffer.clear();
						int num = 0;
						System.out.println("000");
						while ((num=fileChannel.read(buffer)) > 0) {
							buffer.flip();						
							socketChannel.write(buffer);
							buffer.clear();
						}
						System.out.println("111");
						if (num == -1) {
							fileChannel.close();
							socketChannel.shutdownOutput();
						}
						System.out.println("222");
//						try {
//							Thread.sleep(10000);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
						// 接受服务器
						ByteBuffer readBuffer = ByteBuffer.allocate(100);
						socketChannel.read(readBuffer);
						readBuffer.flip();
						System.out.println("333");
						System.out.println(new String(readBuffer.array(), 0, buffer.limit(), Charset.forName("utf-8")));
						buffer.clear();
						socketChannel.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					
//				};
//			}.start();
//
//		}
//		Thread.yield();
	}
}