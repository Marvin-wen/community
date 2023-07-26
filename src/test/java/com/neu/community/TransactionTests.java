package com.neu.community;


import com.neu.community.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TransactionTests {

    @Autowired
    private DemoService demoService;

    @Test
    public void save1() {
        System.out.println(demoService.save1());
    }

    @Test
    public void save2() {
        System.out.println(demoService.save2());
    }
}
