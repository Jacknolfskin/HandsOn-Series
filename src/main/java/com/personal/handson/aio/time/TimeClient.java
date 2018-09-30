package com.personal.handson.aio.time;

/**
 * @Auther: ifly
 * @Date: 2018/9/30 10:34
 * @Description:
 */
public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        while (true){
            new Thread(new AsyncTimeClientHandler("127.0.0.1",port),"AIO-AayncTimeClientHandler-001").start();
            Thread.sleep(1000);
        }

    }

}

