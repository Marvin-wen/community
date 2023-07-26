package com.neu.community.service;


import com.neu.community.dao.DemoDao;
import com.neu.community.dao.DiscussPostMapper;
import com.neu.community.dao.UserMapper;
import com.neu.community.entity.DiscussPost;
import com.neu.community.entity.User;
import com.neu.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
//@Scope("prototype") //将bean设置为多实例，它默认是单例模式；
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    public DemoService() {
        System.out.println("实例化");
    }

    @PostConstruct // 在实例化之后调用
    public void init() {
        System.out.println("初始化");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁");
    }

    public String find() {
        return demoDao.select();
    }

    /*
    * Spring事务管理：声明式事务
    *
    * REQUIRED: 支持当前事务(外部事务, 调用者),如果不存在则创建新事务.
    * REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    * NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
    *
    * */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Object save1() {

        // 新增用户
        User user = new User();
        user.setUsername("demo");
        user.setCreateTime(new Date());
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setEmail("demo@qq.com");
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle("hello");
        discussPost.setCreateTime(new Date());
        discussPost.setContent("新人报到！");
        discussPostMapper.insertDiscussPost(discussPost);

        // 植入错误以使事务失败
        Integer.valueOf("abc");

        return "ok";
    }

    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 新增用户
                User user = new User();
                user.setUsername("demo");
                user.setCreateTime(new Date());
                user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
                user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
                user.setEmail("demo@qq.com");
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost discussPost = new DiscussPost();
                discussPost.setUserId(user.getId());
                discussPost.setTitle("hello");
                discussPost.setCreateTime(new Date());
                discussPost.setContent("新人报到！");
                discussPostMapper.insertDiscussPost(discussPost);

                // 植入错误以使事务失败
                Integer.valueOf("abc");
                return "ok";
            }
        });
    }

}
