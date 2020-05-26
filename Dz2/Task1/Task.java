package com.ivy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Task {

    @Target(value = ElementType.METHOD)
    @Retention(value = RetentionPolicy.RUNTIME)
    public @interface Test {
        int a();

        int b();

    }

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Task task = new Task();
        Class<?> cls = task.getClass();
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods
        ) {
            if (method.isAnnotationPresent(Test.class)) {
                Test anno = method.getAnnotation(Test.class);
                method.invoke(task, anno.a(), anno.b());
            }

        }
    }

    @Test(a = 2, b = 5)
    public void test(int a, int b) {
        System.out.println("a = " + a + "\n" + "b = " + b + "\n" + "a + b = " + (a + b));
    }


}
