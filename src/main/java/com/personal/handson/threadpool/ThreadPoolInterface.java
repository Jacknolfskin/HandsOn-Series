package com.personal.handson.threadpool;

/**
 * @author feihu5
 * @date 2018/7/4 10:18
 */
public interface ThreadPoolInterface {

    /**
     * @param task
     * @return
     * 添加任务
     */
    int addWork(Runnable task);


    /**
     * 停止全部线程执行任务
     * @return int
     *
     */
    int stopAll();

}
