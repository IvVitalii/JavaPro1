package com.ivy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        TextContainer tc = new TextContainer("Task2");
        Class<?> cls = tc.getClass();
        Field fld = cls.getDeclaredField("text");
        fld.setAccessible(true);
        String text = (String) fld.get(tc);
        SaveTo saveTo = cls.getAnnotation(SaveTo.class);
        String path = saveTo.value();
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods
        ) {
            if (method.isAnnotationPresent(Saver.class)) {
                method.invoke(tc, path, text);
            }
        }
    }


}
