package com.example.flowtest.test;

/**
 * Copyright (c) 2023 Raysharp.cn. All rights reserved.
 * <p>
 * Main
 *
 * @author liujianming
 * @date 2023-05-17
 */
public class Main {

    static boolean flag;

    public static void main(String... args) {
        new Thread1().start();
        new Thread2().start();
    }

    static class Thread1 extends Thread {
        @Override
        public void run() {

            while(true) {
                if (flag) {
                    flag = false;
                    System.out.println("Thread1 set flag to false");
                }
            }
        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {

            while(true) {
                if (!flag) {
                    flag = true;
                    System.out.println("Thread2 set flag to false");
                }
            }
        }
    }
}
