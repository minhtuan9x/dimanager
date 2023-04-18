package com.tuanit;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class St {
    private static final MapWrapper<String, Object> MAP = new MapWrapper<>();

    public static <T> T getInstant(Class<T> tClass, MapWrapper<Class<?>, Object> map) {
        try {
            T obj = MAP.get(tClass, map);
            if (obj != null) {
                return obj;
            }
            T t;
            if (map == null) {
                Constructor<T> tConstructor = tClass.getDeclaredConstructor();
                tConstructor.setAccessible(true);
                t = tConstructor.newInstance();
            } else {
                Constructor<T> tConstructor = tClass.getDeclaredConstructor(map.keySet().toArray(new Class[0]));
                tConstructor.setAccessible(true);
                t = tConstructor.newInstance(map.values().toArray(new Object[0]));
            }
            MAP.put(t, map);
            return MAP.get(tClass, map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T getInstant(Class<T> tClass) {
        return getInstant(tClass, null);
    }

    public static MapWrapper<Class<?>,Object> builder() {
        return new MapWrapper<>();
    }


    public static class MapWrapper<K, V> extends HashMap<K, V> {

        @Override
        public V put(K key, V value) {
            synchronized (MAP){
                return super.put(key, value);
            }
        }

        public void put(Object o, Map<Class<?>, Object> map) {
            String key = o.getClass().getName() + (map != null ? map.values() : "");
            this.put((K) key, (V) o);
        }

        public <T> T get(Class<T> tClass, Map<Class<?>, Object> map) {
            String key = tClass.getName() + (map != null ? map.values() : "");
            T t = (T) this.get(key);
            if (t == null) {
                System.out.println(key + " is a new instant");
            } else {
                System.out.println(key + " is exist");
            }

            return t;
        }

        public MapWrapper<K, V> putConstructor(K key, V value) {
            super.put(key, value);
            return this;
        }
    }


}
