package com.personal.handson.springioc;

import com.personal.handson.springioc.annotation.CustomAutowried;
import com.personal.handson.springioc.annotation.CustomService;

/**
 * @author feihu5
 * @date 2018/7/4 15:13
 */
@CustomService
public class Master {

    @CustomAutowried
    Cat cat;

    public void call() {
        cat.eat();
    }
}
