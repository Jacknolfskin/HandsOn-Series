package com.personal.handson.aio.chat;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * @Auther: ifly
 * @Date: 2018/9/29 15:50
 * @Description: 实现自己的CompletionHandler类
 */
public class AcceptHandler implements
        CompletionHandler<AsynchronousSocketChannel, Object> {
    // 定义一个ByteBuffer准备读取数据
    ByteBuffer buff = ByteBuffer.allocate(1024);
    private AsynchronousServerSocketChannel serverChannel;

    public AcceptHandler(AsynchronousServerSocketChannel sc) {
        this.serverChannel = sc;
    }

    // 当有客户端连接上的时候触发该方法，将客户端的AsynchronousSocketChannel传入，以便发送数据
    @Override
    public void completed(final AsynchronousSocketChannel sc
            , Object attachment) {
        // 记录新连接的进来的Channel
        AIOServer.channelList.add(sc);
        // 准备接受客户端的下一次连接
        serverChannel.accept(null, this);

        sc.read(buff, null
                , new CompletionHandler<Integer, Object>()  //② 读取客户端的数据，数据在buff里
                {
                    @Override
                    public void completed(Integer result   //这里表示当客户端AsynchronousSocketChannel完成一次IO,调用此方法
                            , Object attachment) {
                        buff.flip();
                        // 将buff中内容转换为字符串
                        String content = StandardCharsets.UTF_8
                                .decode(buff).toString();
                        // 遍历每个Channel，将收到的信息写入各Channel中
                        for (AsynchronousSocketChannel c : AIOServer.channelList) {
                            try {
                                c.write(ByteBuffer.wrap(content.getBytes(
                                        AIOServer.UTF_8))).get();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        buff.clear();
                        // 读取下一次数据
                        sc.read(buff, null, this);
                    }

                    @Override
                    public void failed(Throwable ex, Object attachment) {
                        System.out.println("读取数据失败: " + ex);
                        // 从该Channel读取数据失败，就将该Channel删除
                        AIOServer.channelList.remove(sc);
                    }
                });
    }

    @Override
    public void failed(Throwable ex, Object attachment) {
        System.out.println("连接失败: " + ex);
    }
}
