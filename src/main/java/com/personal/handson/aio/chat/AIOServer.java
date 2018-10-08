package com.personal.handson.aio.chat;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: feihu5
 * @Date: 2018/9/29 15:29
 * @Description:
 */
public class AIOServer {
    static final int PORT = 30000;
    final static String UTF_8 = "utf-8";
    static List<AsynchronousSocketChannel> channelList
            = new ArrayList<>();

    public static void main(String[] args)
            throws Exception {
        AIOServer server = new AIOServer();
        server.startListen();
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startListen() throws InterruptedException,
            Exception {
        // 创建一个线程池
        ExecutorService executor = Executors.newFixedThreadPool(20);
        // 以指定线程池来创建一个AsynchronousChannelGroup
        AsynchronousChannelGroup channelGroup = AsynchronousChannelGroup
                .withThreadPool(executor);
        // 以指定线程池来创建一个AsynchronousServerSocketChannel
        AsynchronousServerSocketChannel serverChannel
                = AsynchronousServerSocketChannel.open(channelGroup)
                // 指定监听本机的PORT端口
                .bind(new InetSocketAddress(PORT));
        // 使用CompletionHandler接受来自客户端的连接请求
        serverChannel.accept(null, new AcceptHandler(serverChannel));  //①
    }
}


