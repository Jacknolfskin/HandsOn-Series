package com.personal.handson.hashmap;

/**
 * @author feihu5
 * @date 2018/7/6 19:32
 * 动手写Java HashMap
 */
public class Test {

    public static void main(String[] args) {
        MyMap<String, String> myMap = new MyHashMap<>();
        for (int i = 0; i < 500; i++) {
            myMap.put("key" + i, "value" + i);
        }

        for (int i = 0; i < 500; i++) {
            System.out.println("key" + i + ", value is :" + myMap.get("key" + i));
        }
    }
}
