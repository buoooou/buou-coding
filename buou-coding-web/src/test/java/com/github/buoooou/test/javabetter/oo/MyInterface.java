package com.github.buoooou.test.javabetter.oo;

public interface MyInterface {
    default void myDefaultMethod() {
        System.out.println("My default method");
    }

    static void myStaticMethod() {
        System.out.println("My static method");
    }
}