package com.personal.handson.springioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//CustomService注解代表的是Spring的@Service
@Target(ElementType.TYPE) // 类
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomService {

}
