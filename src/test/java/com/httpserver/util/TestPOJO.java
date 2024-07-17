package com.httpserver.util;

import java.util.Objects;

/**
 * A simple Plain Old Java Object (POJO) class representing a test object with name and age attributes.
 * This class is used for testing JSON serialisation and deserialisation in the Json utility class.
 */
public class TestPOJO {

    private String name;
    private int age;

    /**
     * Default constructor.
     */
    public TestPOJO() {}

    /**
     * Parameterised constructor to initialise TestPOJO with the given name and age.
     *
     * @param name the name of the person
     * @param age  the age of the person
     */
    public TestPOJO(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
     * Gets the name of the person.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the person.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the age of the person.
     *
     * @return the age
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the person.
     *
     * @param age the age to set
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Checks if this TestPOJO object is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestPOJO testPOJO = (TestPOJO) o;

        if (age != testPOJO.age) return false;
        return Objects.equals(name, testPOJO.name);
    }

    /**
     * Generates a hash code for the TestPOJO object.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + age;
        return result;
    }
}