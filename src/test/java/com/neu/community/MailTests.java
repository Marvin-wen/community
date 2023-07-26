package com.neu.community;


import com.neu.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("2048107180@qq.com", "TEST", "Hello");
    }

    @Test
    public void testHTML() {
        Context context = new Context();
        context.setVariable("username", "jordan");
        String content = templateEngine.process("/mail//demo.html", context);
        System.out.println(content);
        mailClient.sendMail("2048107180@qq.com", "TEST", content);
    }
}
