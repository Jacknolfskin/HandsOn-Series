package com.personal.handson.hashmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author feihu5
 * @date 2018/7/6 18:38
 */
public class MyHashMap<K, V> implements MyMap<K, V> {

    //数组默认初始化长度
    private static final int DEFAULF_INITIAL_CAPACITY = 1 << 4;

    //负载因子
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int defaultInitSize;

    private float defaultLoadFactor;

    //MAP当中entry的数量
    private int entryUserSize;

    //数组
    private com.personal.handson.hashmap.Entry<K, V>[] table = null;

    public MyHashMap() {
        this(DEFAULF_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int defaulfInitialCapacity, float defaultLoadFactor) {
        if (defaulfInitialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacoty:" + defaulfInitialCapacity);

        }
        if (defaultLoadFactor <= 0 || Float.isNaN(defaultLoadFactor)) {
            throw new IllegalArgumentException("Illegal load factor:" + defaultLoadFactor);
        }

        this.defaultInitSize = defaulfInitialCapacity;
        this.defaultLoadFactor = defaultLoadFactor;

        table = new com.personal.handson.hashmap.Entry[this.defaultInitSize];
    }

    @Override
    public V put(K k, V v) {
        V oldValue = null;
        //判断是否需要扩容，扩容完毕，肯定需要重新散列
        if (entryUserSize >= defaultInitSize * defaultLoadFactor) {
            resize(2 * defaultInitSize);
        }
        int index = hash(k) & (defaultInitSize - 1);
        if (table[index] == null) {
            table[index] = new com.personal.handson.hashmap.Entry<K, V>(k, v, null);
            ++entryUserSize;
        } else {
            com.personal.handson.hashmap.Entry<K, V> entry = table[index];
            com.personal.handson.hashmap.Entry<K, V> e = entry;
            while (Objects.nonNull(e)) {
                if (k == e.getKey() || k.equals(e.getKey())) {
                    oldValue = e.value;
                    e.value = v;
                    return oldValue;
                }
                e = e.next;
            }
            table[index] = new com.personal.handson.hashmap.Entry<K, V>(k, v, entry);
            ++entryUserSize;
        }
        return oldValue;
    }

    @Override
    public V get(K k) {
        int index = hash(k) & (defaultInitSize - 1);
        if (table[index] == null) {
            return null;
        } else {
            com.personal.handson.hashmap.Entry<K, V> entry = table[index];
            do {
                if (k == entry.getKey() || k.equals(entry.getKey())) {
                    return entry.value;
                }
                entry = entry.next;
            } while (entry != null);
        }
        return null;
    }

    private int hash(K k) {
        int hashCode = k.hashCode();
        //要想散列均匀，就得进行二进制的位运算！
        hashCode ^= (hashCode >>> 20) ^ (hashCode >>> 12);
        return hashCode ^ (hashCode >>> 7) ^ (hashCode >>> 4);
    }

    private void resize(int i) {
        com.personal.handson.hashmap.Entry[] newTable = new com.personal.handson.hashmap.Entry[i];
        //改变数组大小
        defaultInitSize = i;
        entryUserSize = 0;
        rehash(newTable);
    }

    private void rehash(com.personal.handson.hashmap.Entry<K, V>[] newTable) {
        List<com.personal.handson.hashmap.Entry<K, V>> entryList = new ArrayList<com.personal.handson.hashmap.Entry<K, V>>();
        for (com.personal.handson.hashmap.Entry<K, V> entry : table) {
            if (Objects.nonNull(entry)) {
                do {
                    entryList.add(entry);
                    entry = entry.next;
                } while (entry != null);
            }
        }

        //覆盖旧的应用
        if (newTable.length > 0) {
            table = newTable;
        }

        //重新Hash,就是重新Put Entry到HashMap
        for (com.personal.handson.hashmap.Entry<K, V> entry : entryList) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
