package com.tuanit.test;

import com.tuanit.*;
import com.tuanit.di.Autowired;
import com.tuanit.di.Component;

@Component
public class Test1 {

    @Autowired
    private Test2 test2;
    @Autowired
    private Test3 test3;
    @Autowired
    private Test6 test6;

    public String test() {
        return "test1" + test2.test() + test3.test() + test6.test();
    }
}
