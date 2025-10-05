package main.java.myhashmap;

public interface MyMap<K, V> {
    V get(K key);
    void put(K key, V value);
    V remove(K key);
    boolean containsKey(K key);
}
