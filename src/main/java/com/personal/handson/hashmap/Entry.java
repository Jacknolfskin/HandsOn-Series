package com.personal.handson.hashmap;

/**
 * @author feihu5
 * @date 2018/7/6 18:53
 */
public class Entry<K, V> implements MyMap.Entry<K, V> {

    public K key;
    public V value;
    public Entry<K, V> next;

    public Entry() {
    }

    public Entry(K key, V value, Entry<K, V> next) {
        this.key = key;
        this.value = value;
        this.next = next;
    }

    @Override
    public K getKey() {
        return null;
    }

    @Override
    public V getValue() {
        return null;
    }
}
