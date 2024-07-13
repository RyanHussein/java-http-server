package com.httpserver.util;

public class TestPOJO {

    private String name;
    private int age;

    // Constructors
    public TestPOJO() {}

    public TestPOJO(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestPOJO testPOJO = (TestPOJO) o;

        if (age != testPOJO.age) return false;
        return name.equals(testPOJO.name);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }
}
