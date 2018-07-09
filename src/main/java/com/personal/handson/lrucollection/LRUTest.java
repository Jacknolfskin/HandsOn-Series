package com.personal.handson.lrucollection;

import java.util.Map;

/**
 * @author feihu5
 * @date 2018/7/9 20:32
 * 动手写Java LRU算法
 */
public class LRUTest {

    public static void main(String[] args) {
        LRULinkedMap<String, Integer> map = new LRULinkedMap(4);
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        System.out.println("#############使用前##############");
        for (Map.Entry<String, Integer> e : map.getAll()) {
            System.out.print(e.getKey() + " : " + e.getValue() + "\n");
        }
        map.get("1");
        map.put("5", 5);
        System.out.println("#############使用后##############");
        for (Map.Entry<String, Integer> e : map.getAll()) {
            System.out.print(e.getKey() + " : " + e.getValue() + "\n");
        }
    }
}
