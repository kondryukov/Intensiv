package main.java.myhashmap;

public class Main {
    public static void main(String[] args) {
        MyMap<String, Integer> map = new MyHashMap<>();
        map.put(null, 1);
        System.out.println(map.get(null));
    }
}
