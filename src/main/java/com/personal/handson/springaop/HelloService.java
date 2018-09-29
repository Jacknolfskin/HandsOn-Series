package com.personal.handson.springaop;

/**
 * Spring使用了CGLIG和JDK动态代理。
 * 在JDK动态代理中，要求必须提供接口，而CGLIB是不需要的，我们这里只谈论JDK动态代理，
 * 在大部分的情况下，建议使用JDK动态代理，因为JDK动态代理的速度要比CGLIB要快，
 * 在Spring中一个有切面的Bean如果有接口声明，Spring就会用JDK动态代理代理它，否者启用CGLIB。
 */
public interface HelloService {

    public void sayHello(String name);
}
