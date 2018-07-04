package com.personal.handson.threadpool;

/**
 * @author feihu5
 * @date 2018/7/4 10:21
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {

        Runnable run = new Runnable() {

            @Override
            public void run() {

                System.out.println("当前执行任务的线程名称"+ Thread.currentThread() + "---------");

            }
        };


        ThreadPoolInterface ti = new ThreadFixPoolImpl();
        for (int i = 0; i < 1000; i++) {
            ti.addWork(run);
        }
    }

}
