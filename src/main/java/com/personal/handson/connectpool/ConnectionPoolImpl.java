package com.personal.handson.connectpool;

import redis.clients.jedis.Jedis;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author feihu5
 * @date 2018/7/4 13:45
 */
public class ConnectionPoolImpl implements ConnectionPool {

    /**
     * 是否关闭
     */
    AtomicBoolean isClosed = new AtomicBoolean(false);

    /**
     * 队列实现连接 对象存储 空闲队列
     */
    LinkedBlockingQueue<Jedis> idle;

    /**
     * 繁忙队列
     */
    LinkedBlockingQueue<Jedis> busy;

    /**
     * 大小控制连接数量
     */
    AtomicInteger activeSize = new AtomicInteger(0);

    /**
     * 记录连接被创建的次数
     */
    AtomicInteger createCounter = new AtomicInteger(0);

    int maxActive;
    long maxWait;


    @Override
    public void init(int maxActive, long maxWait) {
        this.maxActive = maxActive;
        this.maxWait = maxWait;
        idle = new LinkedBlockingQueue<Jedis>();
        busy = new LinkedBlockingQueue<Jedis>();
    }

    @Override
    public Jedis borrowResource() throws Exception {
        Jedis jedis = null;
        //获取连接的开始时间
        long now = System.currentTimeMillis();

        while (null == jedis) {

            //从空闲队列中获取一个
            jedis = idle.poll();

            if (null != jedis) {
                //如果空闲队列里有连接,直接是被复用，再将此连接移动到busy （繁忙）队列中
                busy.offer(jedis);
                System.out.println("从空闲队列里拿到连接");
                return jedis;
            }

            //如果空闲队列里没有连接,就判断是否超出连接池大小，不超出就创建一个
            //多线程并发
            if (activeSize.get() < maxActive) {
                //先加再判断
                if (activeSize.incrementAndGet() <= maxActive) {
                    //创建jedis连接
                    jedis = new Jedis("127.0.0.1", 6379);
                    jedis.auth("123456");
                    System.out.println("连接被创建的次数：" + createCounter.incrementAndGet());
                    //存入busy队列
                    busy.offer(jedis);
                    return jedis;
                } else {
                    //加完后超出大小再减回来
                    activeSize.decrementAndGet();
                }

            }

            //如果前面2个都不能拿到连接，那就在我们设置的最大等待超时时间内，等待别人释放连接
            try {
                //等待别人释放得到连接，同时也有最长的等待时间限制
                jedis = idle.poll(maxWait - (System.currentTimeMillis() - now), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                throw new Exception("等待异常 ... ");
            }

            if (null == jedis) {
                //判断是否超时
                if ((System.currentTimeMillis() - now) >= maxWait) {
                    throw new Exception("timeout ... ");
                } else {
                    continue;
                }
            } else {
                //存入busy队列
                busy.offer(jedis);
            }

        }
        return jedis;
    }

    @Override
    public void release(Jedis jedis) throws Exception {
        if (null == jedis) {
            System.out.println("释放 的jedis为空");
            return;
        }
        if (busy.remove(jedis)) {
            idle.offer(jedis);
        } else {
            //如果释放不成功,则减去一个连接，在创建的时候可以自动补充
            activeSize.decrementAndGet();
            throw new Exception("释放jedis异常");
        }
    }

    @Override
    public void close() {

        if (isClosed.compareAndSet(false, true)) {
            LinkedBlockingQueue<Jedis> pool = idle;
            while (pool.isEmpty()) {
                Jedis jedis = pool.poll();
                jedis.close();
                if (pool == idle && pool.isEmpty()) {
                    pool = busy;
                }

            }
        }
    }
}