package com.tuanit;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Context {
    public static Map<String, Object> map = new HashMap<>();

    public static void scanPackage(Class aClassIn) throws Exception {
        Package aPackage = aClassIn.getPackage();
        Set<Class> classes = findAllClassesUsingGoogleGuice(aPackage.getName());
        //Helper.log("---SINGLETON---");
        for (Class aClass : classes) {
            if (aClass.isAnnotationPresent(Component.class)) {
                Object o = map.get(aClass.getName());
                if (o == null) {
                    o = aClass.getDeclaredConstructor().newInstance();
                    //Helper.log("New instance: " + aClass.getName().split("\\.")[aClass.getName().split("\\.").length - 1]);
                    map.put(aClass.getName(), o);
                }
                for (Field field : aClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Autowired.class)) {
                        if (field.get(o) == null) {
                            field.setAccessible(true);
                            Object fieldObj = map.get(field.getType().getName());
                            if (fieldObj == null) {

                                Constructor constructor = field.getType().getConstructor();
                                constructor.setAccessible(true);
                                fieldObj = constructor.newInstance();
                                assert fieldObj != null;
                                map.put(field.getClass().getName(), fieldObj);
                            }

                            field.set(o, fieldObj);
                            //Helper.log("--> " + field.getType().getName().split("\\.")[field.getType().getName().split("\\.").length - 1]);

                        }
                    }
                }
                //Helper.log("-------------");
            }

        }
    }

    public static <T> T getInstance(Class<T> aClass) {
        return (T) map.get(aClass.getName());
    }


    public static Set<Class> findAllClassesUsingGoogleGuice(String packageName) throws IOException {
        return ClassPath.from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(clazz -> clazz.getPackageName()
                        .startsWith(packageName))
                .map(clazz -> clazz.load())
                .collect(Collectors.toSet());
    }

}
