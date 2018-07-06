package com.personal.handson.hashmap;

/**
 * 定义接口
 * @param <K>
 * @param <V>
 */
public interface MyMap<K, V> {
    public V put(K k, V v);

    public V get(K k);

    //接口内部定义了一个内部接口Entry
    interface Entry<K, V> {
        public K getKey();

        public V getValue();
    }
}
