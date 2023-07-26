package com.neu.community;

import com.neu.community.config.DemoConfig;
import com.neu.community.controller.DemoController;
import com.neu.community.dao.DemoDao;
import com.neu.community.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
        // 标注测试时以谁为配置类
class CommunityApplicationTests implements ApplicationContextAware {

    //实现ApplicationContextAware接口以获得容器

    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //ApplicationContext其实就是容器，它的最上层父接口就是BeanFactory
        this.applicationContext = applicationContext;
    }

    /*
     * 测试通过容器获取bean的方式
     * */

    @Test
    public void testApplicationContext() {
        System.out.println(applicationContext);
        //通过容器获取bean
        DemoDao demoDao = applicationContext.getBean(DemoDao.class);
        System.out.println(demoDao.select());
        //通过名字获取低优先级实现类
        demoDao = applicationContext.getBean("oldImplement", DemoDao.class);
        System.out.println(demoDao.select());
    }

    /*
     * 测试容器管理bean的方式
     * */

    @Test
    public void testBeanManagement() {
        DemoService demoService = applicationContext.getBean(DemoService.class);
        System.out.println(demoService);

        demoService = applicationContext.getBean(DemoService.class);
        System.out.println(demoService);
    }

    @Test
    public void testBeanConfig() {
        SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(simpleDateFormat.format(new Date()));
    }

    @Autowired
    @Qualifier("oldImplement") //指定注入哪个bean
    private DemoDao demoDao;

    @Autowired
    private DemoService demoService;

    @Autowired
    private DemoConfig demoConfig;

    @Autowired
    private DemoController demoController;

    @Test
    public void testDI() {
        System.out.println(demoConfig);
        System.out.println(demoController);
        System.out.println(demoService);
        System.out.println(demoDao);
    }

}
