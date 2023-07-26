package com.neu.community;


import com.neu.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SensitiveFilterTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void filterTest() {
        String sensitiveText = "这里可以赌博，可以吸毒，也可以☆卖##淫☆和☆☆嫖☆☆娼☆，简直是人间天堂！";
        System.out.printf(sensitiveFilter.filter(sensitiveText));
    }

}
