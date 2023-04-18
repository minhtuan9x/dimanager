package com.tuanit;

import com.tuanit.di.Context;
import com.tuanit.test.Test1;

public class Main {

    public static void main(String[] args) throws Exception{
        Context.scanPackage(Main.class);
        Test1 test1 = Context.getInstance(Test1.class);
        System.out.println(test1.test());
    }
}