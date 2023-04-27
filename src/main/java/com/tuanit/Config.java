package com.tuanit;

import com.tuanit.di.Autowired;
import com.tuanit.di.Bean;
import com.tuanit.di.Configuration;

@Configuration
public class Config {

    @Bean
    public Test6 test6() {
        Test6 test6 = new Test6();
        test6.t = "chicken";
        return test6;
    }
}
