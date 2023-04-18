package com.tuanit.test;

import com.tuanit.Autowired;
import com.tuanit.Component;
import com.tuanit.Test2;

@Component
public class Test1 {

    @Autowired
    public Test2 test2;
    @Autowired
    public Test3 test3;

    public String test() {
        return "test1" + test2.test()+test3.test();
    }
}
