package com.ivy;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        TestClass test = new TestClass("Vasul", 25, 12);
        Serializer.serialize(test);
        TestClass tc = Serializer.deserialize("D:\\save.txt", TestClass.class);
        System.out.println(tc.getClass());
        System.out.println(tc.getName());
        System.out.println(tc.getAge());
        System.out.println(tc.getId());
    }
}
