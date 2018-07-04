package com.personal.handson.springioc;

import com.personal.handson.springioc.annotation.CustomAutowried;
import com.personal.handson.springioc.annotation.CustomService;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author feihu5
 * @date 2018/7/4 15:16
 */
public class Object_Reflec {

    //模拟SpringBean容器
    List<Object> objectList;

    public Object_Reflec() {
        objectList = new ArrayList<Object>();
    }

    public void get_ref(Object object) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = object.getClass();
        //获取其变量
        Field[] fields = clazz.getDeclaredFields();
        //先找出带有注解的类,后判断是否带有CustomService注解
        if (fields.length != 0) {
            if (clazz.isAnnotationPresent(CustomService.class)) {
                for (Field field : fields) {
                    //判断是否需要注入
                    if (field.isAnnotationPresent(CustomAutowried.class)) {
                        //这里先将Cat类加载
                        Class<?> catClass = Class.forName(field.getType().getName(), false, Thread.currentThread().getContextClassLoader());
                        //赋给master
                        field.set(object, catClass.newInstance());
                        //最后将已将赋值后的Master保存进容器
                        objectList.add(object);
                    }
                }
            }
        }
    }

    public List<Object> returnList() {
        //返回容器方便以后使用
        return objectList;
    }
}
