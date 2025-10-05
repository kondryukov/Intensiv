package myOwnHashMap;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;
        private Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
    private Node<K, V>[] buckets;
    private int size;
    private int capacity;

    public MyHashMap() {
        this.capacity = 16;
        buckets = newBucket(capacity);
        this.size = 0;
    }
    @SuppressWarnings("unchecked")
    private static <K, V> Node<K, V>[] newBucket (int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }
    @SuppressWarnings("unchecked")
    public void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newBuckets = (Node<K, V>[]) new Node[newCapacity];
        for (int i = 0; i < capacity; i++) {
            Node<K,V> current = buckets[i];
            while (current != null) {
                int newIndex = (current.key.hashCode() & 0x7fffffff) % newCapacity;
                Node<K,V> next = current.next;
                current.next = newBuckets[newIndex];
                newBuckets[newIndex] = current;
                current = next;
            }
            buckets[i] = null;
        }
        buckets = newBuckets;
        capacity = newCapacity;
    }

    @Override
    public V get(K key) {
        int index = (key.hashCode() & 0x7fffffff) % buckets.length;
        if (buckets[index] != null) {
            Node<K, V> tmp = buckets[index];
            while (tmp.next != null) {
                if (tmp.key.equals(key)) {
                    return tmp.value;
                }
                tmp = tmp.next;
            }
            if (tmp.key.equals(key)) {
                return tmp.value;
            }
            return (tmp.key.equals(key) ? tmp.value : null);
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        int index = (key != null ? (key.hashCode() & 0x7fffffff) % buckets.length : 0);
        if (buckets[index] == null) {
            buckets[index] = new Node<>(key, value, null);
            size++;
        } else {
            Node<K, V> current = buckets[index];
            while(current.next != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key.equals(key)) {
                current.value = value;
            } else {
                current.next = new Node<>(key, value, null);
                size++;
            }
        }
        if ((float) (size / capacity) > 0.75f) {
            resize();
        }
    }
    @Override
    public V remove(K key) {
        int index = (key.hashCode() & 0x7fffffff) % buckets.length;
        if (buckets[index] != null) {
            Node<K, V> current = buckets[index];
            Node<K, V> prev = null;
            while(current != null) {
                if (current.key.equals(key)) {
                    if (prev == null) {
                        buckets[index] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    return current.value;
                }
                prev = current;
                current = current.next;
            }
        }
        return null;
    }
    @Override
    public boolean containsKey(K key) {
        int index = (key.hashCode() & 0x7fffffff) % buckets.length;
        if (buckets[index] != null) {
            Node<K, V> current = buckets[index];
            while (current != null) {
                if (current.key.equals(key)) {
                    return true;
                }
                current = current.next;
            }
            return false;
        }
        return false;
    }
}
