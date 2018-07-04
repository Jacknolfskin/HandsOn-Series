package com.personal.handson.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author feihu5
 * @date 2018/7/4 10:19
 */
public class ThreadFixPoolImpl implements ThreadPoolInterface {

    /**
     * 线程的个数 默认开启100个线程执行任务
     */
    private static volatile Integer workNum = 10;

    /**
     * 任务队列
     */
    private static LinkedBlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    static AtomicInteger count = new AtomicInteger(0);

    WorkThread[] workThread;

    public ThreadFixPoolImpl() {
        this(workNum);
    }

    public ThreadFixPoolImpl(int workNum) {
        if (0 != workNum) ThreadFixPoolImpl.workNum = workNum;
        workThread = new WorkThread[workNum];
        //预先开启线程
        for (int i = 0; i < workNum; i++) {
            workThread[i] = new WorkThread();
            Thread t = new Thread(workThread[i], "线程 - " + i + ": name");
            t.start();
            System.out.println("线程: " + i + ":已开启");
        }
    }

    @Override
    public int addWork(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.offer(task);
            taskQueue.notifyAll();
        }

        return 1;
    }

    @Override
    public int stopAll() {
        //new WorkThread().setIsRuning();
        if (null != workThread) {
            for (int i = 0; i < workThread.length; i++) {
                workThread[i].setIsRuning();
            }
            return 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        new ThreadFixPoolImpl();
    }


    static class WorkThread extends Thread {

        volatile boolean isRuning = true;

        @Override
        public void run() {
            //预先开好的线程去消费这个任务队列
            while (true) {
                if (isRuning) {
                    synchronized (taskQueue) {
                        if (isRuning && taskQueue.isEmpty()) {
                            try {
                                taskQueue.wait(20);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("等待添加任务状态");
                        } else {
                            if (!taskQueue.isEmpty()) {
                                //从任务队列中取出执行
                                Runnable r = taskQueue.poll();
                                if (null != r) {
                                    r.run();
                                    System.out.println(" 执行第 [ " + count.incrementAndGet() + " ] 次任务");
                                }
                            }

                        }
                    }
                }
            }

        }

        public void setIsRuning() {
            this.isRuning = false;
        }

    }

}
