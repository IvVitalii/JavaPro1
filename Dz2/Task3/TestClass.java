package com.ivy;

public class TestClass {
    @Save
    private String name;
    @Save
    private int age;
    private int id;

    public TestClass(String name, int age, int id) {
        this.name = name;
        this.age = age;
        this.id = id;
    }

    public TestClass() {

    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int getId() {
        return id;
    }
}
