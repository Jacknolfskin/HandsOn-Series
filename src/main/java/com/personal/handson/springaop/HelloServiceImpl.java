package com.personal.handson.springaop;

/**
 * @author feihu5
 * @date 2018/7/16 14:34
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String name) {
        System.err.println("Hello-" + name);
    }
}
