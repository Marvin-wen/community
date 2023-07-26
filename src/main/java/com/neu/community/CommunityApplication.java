package com.neu.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;

/*
 * Spring的核心思想是IoC（Inversion of Control，控制反转）容器，它负责管理Bean的生命周期
 * 所谓Bean是Spring包装过的Object
 * 依赖注入是IoC设计思想的实现方式，类似于set方法
 * */

/*
 * Bug记录：往@SpringBootApplication注解中加exclude= {DataSourceAutoConfiguration.class}会报错，原因不明
 * 总之，@SpringBootApplication()的括号中建议不要有东西，避免不必要的错误
 * */

@SpringBootApplication
//@EnableElasticsearchRepositories("com.neu.community")
public class CommunityApplication {

    @PostConstruct
    public void init() {
        // 解决netty启动冲突问题
        // see Netty4Utils.setAvailableProcessors()
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    /*
     * 在项目启动过程中，spring自动创建容器，启动tomcat，容器根据配置类自动装配bean
     * @SpringBootApplication注解标注的就是配置类，它由多个注解
     * （@SpringBootConfiguration、@EnableAutoConfiguration、@ComponentScan等）组成：
     * @EnableAutoConfiguration自动配置
     * @ComponentScan自动扫描配置类所在包及其子包下的所有bean
     * 但是只有被@Controller,@Service, @Component,@Repository标注的bean才会被是扫描
     * 这几个注解本质上都是由 @Component所实现，只是语义有所不同
     * */

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
