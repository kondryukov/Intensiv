package test.java;

import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import main.java.myhashmap.*;
import static org.junit.jupiter.api.Assertions.*;

class MyHashMapTest {
    private MyHashMap<String, Integer> map;

    @Test
    @DisplayName("put/get: базовая вставка и чтение")
    void putGet() {
        map = new MyHashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        assertTrue(map.containsKey("a"));
        assertTrue(map.containsKey("b"));
        assertEquals(1, map.get("a"));
        assertEquals(2, map.get("b"));
    }

    @Test
    @DisplayName("get: отсутствующий ключ → null, containsKey → false")
    void getAbsent() {
        map = new MyHashMap<>();
        assertNull(map.get("missing"));
        assertFalse(map.containsKey("missing"));
    }

    @Test
    @DisplayName("put: перезапись значения по существующему ключу")
    void putOverwrite() {
        map = new MyHashMap<>();
        map.put("k", 1);
        map.put("k", 2);
        assertEquals(2, map.get("k"));
    }

    @Test
    @DisplayName("remove: удаляет единственный элемент в бакете")
    void removeSingleBucket() {
        map = new MyHashMap<>();
        map.put("x", 42);
        assertTrue(map.containsKey("x"));
        map.remove("x");
        assertFalse(map.containsKey("x"));
        assertNull(map.get("x"));
    }

    @Test
    @DisplayName("equals: равные, но разные по ссылке ключи работают")
    void equalButDistinctKeys() {
        map = new MyHashMap<>();
        String k1 = "key";
        String k2 = "key";
        map.put(k1, 7);
        assertTrue(map.containsKey(k2));
        assertEquals(7, map.get(k2));
    }

    @Test
    @DisplayName("resize: добавляем много элементов")
    void resizePut() {
        map = new MyHashMap<>();
        Map<String, Integer> trueMap = new HashMap<>();
        for (int i = 0; i < 100_000; i++) {
            String s = String.valueOf(i);
            map.put(s, i);
            trueMap.put(s, i);
            assertEquals(trueMap.get(s), map.get(s));
        }

        for (int i = 0; i < 100_000; i++) {
            String s = String.valueOf(i);
            assertEquals(trueMap.get(s), map.get(s));
        }
    }

    @Test
    @DisplayName("resize: добавляем много элементов и все их удаляем, проверяя remove")
    void resizePutRemove() {
        map = new MyHashMap<>();
        Map<String, Integer> trueMap = new HashMap<>();
        for (int i = 0; i < 100_000; i++) {
            String s = String.valueOf(i);
            map.put(s, i);
            trueMap.put(s, i);
            assertEquals(trueMap.get(s), map.get(s));
        }

        for (int i = 0; i < 100_000; i++) {
            String s = String.valueOf(i);
            Integer myValue = map.remove(s);
            Integer trueMapValue = trueMap.remove(s);
            assertEquals(myValue, trueMapValue);
        }
    }

    @Test
    @DisplayName("resize: добавляем много элементов и все их удаляем")
    void resizeDeleteReturn() {
        map = new MyHashMap<>();
        Map<String, Integer> trueMap = new HashMap<>();
        for (int i = 0; i < 100_000; i++) {
            String s = String.valueOf(i);
            map.put(s, i);
            trueMap.put(s, i);
            assertEquals(trueMap.get(s), map.get(s));
        }
        for (int i = 0; i < 100_000; i++) {
            String s = String.valueOf(i);
            assertEquals(trueMap.get(s), map.get(s));
        }
    }

    @Test
    @DisplayName("Коллизии: добавляем много элементов(с предсказуемым hashcode) и потом их перезаписываем")
    void collisionsAndResize() {
        class Key {
            final String string;
            Key(String string) {
                this.string = string;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Key key = (Key) o;
                return Objects.equals(string, key.string);
            }

            @Override
            public int hashCode() {
                return string.length();
            }
        }
        MyMap<Key, Integer> map = new MyHashMap<>();
        Map<Key, Integer> trueMap = new HashMap<>();
        Key key;
        for (int i = 0; i < 10000; i++) {
            key = new Key(String.valueOf(i));
            map.put(key, i);
            trueMap.put(key, i);
            assertEquals(trueMap.get(key), map.get(key));
        }
        for (int i = 0; i < 10000; i++) {
            key = new Key(String.valueOf(i) + i);
            map.put(key, i);
            trueMap.put(key, i);
            assertEquals(trueMap.get(key), map.get(key));
        }
    }


    @Test
    @DisplayName("put/get: базовая вставка и чтение null элементов")
    void putGetNull() {
        map = new MyHashMap<>();
        map.put(null, 1);
        assertEquals(1, map.get(null));
        assertTrue(map.containsKey(null));
    }

    @Test
    @DisplayName("resize: добавляем много null элементов")
    void resizePutNull() {
        map = new MyHashMap<>();
        Map<String, Integer> trueMap = new HashMap<>();
        String s;
        for (int i = 0; i < 100_000; i++) {
             s = (i % 2 == 0) ? null : String.valueOf(i);
            map.put(s, i);
            trueMap.put(s, i);
            assertEquals(trueMap.get(s), map.get(s));
        }

        for (int i = 0; i < 100_000; i++) {
            s = (i % 2 == 0) ? null : String.valueOf(i);
            assertEquals(trueMap.get(s), map.get(s));
        }
    }

    @Test
    @DisplayName("resize: добавляем много null элементов и все их удаляем, проверяя remove")
    void resizePutRemoveNull() {
        map = new MyHashMap<>();
        Map<String, Integer> trueMap = new HashMap<>();
        String s;
        for (int i = 0; i < 100_000; i++) {
            s = (i % 2 == 0) ? null : String.valueOf(i);
            map.put(s, i);
            trueMap.put(s, i);
            assertEquals(trueMap.get(s), map.get(s));
        }

        for (int i = 0; i < 100_000; i++) {
            s = (i % 2 == 0) ? null : String.valueOf(i);
            Integer myValue = map.remove(s);
            Integer trueMapValue = trueMap.remove(s);
            assertEquals(myValue, trueMapValue);
        }
    }
}
