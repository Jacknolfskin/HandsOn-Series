package com.personal.handson.springioc;

import java.lang.reflect.InvocationTargetException;

/**
 * @author feihu5
 * @date 2018/7/4 15:22
 * 动手写Spring IOC
 */
public class MasterPrediction {
    public static void main(String args[]) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        MasterPrediction masterPrediction = new MasterPrediction();
        Master master = (Master) masterPrediction.springDo();
        //这里如果是普通调用会报空指针异常，而容器却为其将rain这个变量赋值了，所以可以正常输出
        master.call();
    }

    /**
     * 模拟Spring启动过程，这一步其实可以单独写一个类，这一步是容器该做的，而我们并不需要去管
     *
     *
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public Object springDo() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        //启动的时候就需要加载的
        Object_Reflec object_reflec = new Object_Reflec();
        //扫描类注解后new操作然后进行下一步,将其类里面的变量进行new操作并放入容器
        object_reflec.get_ref(new Master());
        Object object = object_reflec.returnList().get(0);
        return object;
    }
}
