package com.personal.handson.springioc.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//代表的是Spring里面的@Autowrid
@Target(ElementType.FIELD) //描述方法的
@Retention(RetentionPolicy.RUNTIME) // 仅运行时保留
public @interface CustomAutowried {
}
