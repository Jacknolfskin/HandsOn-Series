package com.personal.handson.connectpool;

import redis.clients.jedis.Jedis;

import java.util.concurrent.CountDownLatch;

/**
 * @author feihu5
 * @date 2018/7/4 13:49
 * 动手写Java连接池
 */
public class ConnectionPoolTest {
    private static int threadCount = 30;

    /**
     * 为保证30个线程同时并发运行
     */
    private final static CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(threadCount);

    public static void main(String[] args) {
        final ConnectionPool pool = new ConnectionPoolImpl();
        //连接池最大连接数和获取连接的超时时间
        pool.init(10, 2000L);

        //循环开30个线程
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    int i = 0;
                    //每个线程里循环十次获取连接
                    while (i < 10) {
                        i++;
                        Jedis jedis = null;
                        try {
                            //每次减一
                            COUNT_DOWN_LATCH.countDown();
                            //此处等待状态，阻塞线程运行，为0时让30个线程同时进行
                            COUNT_DOWN_LATCH.await();
                            jedis = pool.borrowResource();
                            jedis.get("a");
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                //释放连接
                                pool.release(jedis);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }).start();
        }
    }

}
