package com.personal.handson.springaop;

/**
 * @author feihu5
 * @date 2018/7/16 14:51
 */
public class Test {
    public static void main(String[] args){
        HelloProxy helloProxy = new HelloProxy();
        //因为使用了接口HelloService绑定了代理对象，所以可以用HelloService作为代理对象的声明.
        HelloService proxy = (HelloService) helloProxy.bind(new HelloServiceImpl());
        //此时使用代理对象运行方法进入HelloProxy的invoke方法里
        proxy.sayHello("HuFei");
    }
}
