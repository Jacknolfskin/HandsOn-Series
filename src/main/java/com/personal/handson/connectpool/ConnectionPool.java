package com.personal.handson.connectpool;

import redis.clients.jedis.Jedis;

/**
 * @author feihu5
 * @date 2018/7/4 13:44
 */
public interface ConnectionPool {

    /**
     * 初始化线程池
     * max 最大连接数
     * maxWait 最大等待时间
     * */
    void init(int maxActive ,long maxWait);

    /**
     * 获取连接
     * */
    Jedis borrowResource()throws Exception;

    /****
     * 释放连接
     * **/
    void release(Jedis jedis)throws Exception;


    /****
     * 关闭
     * **/
    void close();
}

