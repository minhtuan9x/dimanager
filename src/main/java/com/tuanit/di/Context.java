package com.tuanit.di;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class Context {
    public static Map<String, Object> map = new HashMap<>();

    public static <T> void scanPackage(Class<T> aClassIn) throws Exception {
        Package aPackage = aClassIn.getPackage();
        Set<Class<?>> classes = findAllClassesUsingGoogleGuice(aPackage.getName());
        System.out.println("---SINGLETON---");
        for (Class<?> aClass : classes) {
            if (aClass.isAnnotationPresent(Configuration.class)) {
                Object oMe = aClass.getDeclaredConstructor().newInstance();

                List<Method> methods = Arrays.stream(aClass.getDeclaredMethods())
                        .filter(item -> item.isAnnotationPresent(Bean.class))
                        .collect(Collectors.toList());
                for (Method item : methods) {
                    item.setAccessible(true);
                    Object o = item.invoke(oMe);
                    map.put(item.getReturnType().getName(), o);
                    System.out.println("New instance from the configuration: " + item.getReturnType().getName().split("\\.")[aClass.getName().split("\\.").length - 1]);
                    System.out.println("-------------");
                }
            }
        }


        for (Class<?> aClass : classes) {
            if (aClass.isAnnotationPresent(Component.class)) {
                Object o = map.get(aClass.getName());
                if (o == null) {
                    o = aClass.getDeclaredConstructor().newInstance();
                    System.out.println
                            ("New instance: " + aClass.getName().split("\\.")[aClass.getName().split("\\.").length - 1]);
                    map.put(aClass.getName(), o);
                }
                System.out.println("-------------");
            }
        }

        for (Class<?> aClass : classes) {
            if (aClass.isAnnotationPresent(Component.class)) {
                Object o = map.get(aClass.getName());
                for (Field field : aClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Autowired.class)) {
                        if (field.get(o) == null) {
                            field.setAccessible(true);
                            Object fieldObj = map.get(field.getType().getName());
                            field.set(o, fieldObj);
                            System.out.println("--> " + field.getType().getName().split("\\.")[field.getType().getName().split("\\.").length - 1]);
                        }
                    }
                }
                map.put(aClass.getName(), o);
                System.out.println("-------------");
            }
        }
    }

    public static <T> T getInstance(Class<T> aClass) {
        return (T) map.get(aClass.getName());
    }


    public static Set<Class<?>> findAllClassesUsingGoogleGuice(String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName()
                        .startsWith(packageName))
                .map(ClassPath.ClassInfo::load)
                .collect(Collectors.toSet());
    }

}
