package com.zy.demo;


public class MyTest {

    public static void main(String[] args){
        long start = System.currentTimeMillis();
        test();
        long end = System.currentTimeMillis();
        System.out.println("程序执行耗费时间="+(end-start)+"ms");
    }

    private static void test(){
    }
}
