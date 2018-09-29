package com.personal.handson.springaop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author feihu5
 * @date 2018/7/16 14:38
 */
public class HelloProxy implements InvocationHandler {

    private Object target;

    public Object bind(Object target){
        this.target = target;
        //类加载器
        Object proxy = Proxy.newProxyInstance(target.getClass().getClassLoader(),
                //对象的接口，明确代理对象挂在哪些接口下
                target.getClass().getInterfaces(),
                //指明代理类，this代表用当前类对象，那么就要求其实现InvocationHandler接口
                this);
        return proxy;
    }
    /**
     * 当生成代理对象是，第三个指定使用HelloProxy进行代理时，代理对象调用的方法就会进入这个方法
     * @param proxy 代理对象
     * @param method 被调用方法
     * @param args 方法参数
     * @return 代理方法返回
     * @throws Throwable 异常处理
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.err.println("反射真实对象方法前");
        //相当于sayHello方法调用
        Object obj = method.invoke(target,args);
        System.err.println("反射真实对象方法后");
        return obj;
    }
}
