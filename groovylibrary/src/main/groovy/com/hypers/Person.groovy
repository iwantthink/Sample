package com.hypers

public class Person {
    final String name;

    public Person(String name) {
        this.name = name
    }

    int age;

    boolean boy;

    @Override
    public String toString() {
        return "I am $name, $age years old, " + (boy ? "I am a boy" : "I am a gril");
    }
}