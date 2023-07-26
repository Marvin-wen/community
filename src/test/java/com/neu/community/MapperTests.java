package com.neu.community;

import com.neu.community.dao.DiscussPostMapper;
import com.neu.community.dao.LoginTicketMapper;
import com.neu.community.dao.MessageMapper;
import com.neu.community.dao.UserMapper;
import com.neu.community.entity.DiscussPost;
import com.neu.community.entity.LoginTicket;
import com.neu.community.entity.Message;
import com.neu.community.entity.User;
import com.neu.community.util.CommunityUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;


@SpringBootTest
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testSelectUser() {
        System.out.println("开始测试");
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
        System.out.println("测试成功");
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test123");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.jpg");
        user.setCreateTime(new Date());

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void testUpdateUser() {
//        int rows = userMapper.updateStatus(150, 1);
//        System.out.println(rows);
//
//        rows = userMapper.updateHeader(150,"http://www.nowcoder.com/102.jpg");
//        System.out.println(rows);
//
        int rows = userMapper.updatePassword(150, CommunityUtil.md5("helloabc"));
        System.out.println(rows);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(0,0,10);
        for(DiscussPost post: list){
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(0);
        System.out.println(rows);

        rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }

    @Test
    public void testInsertTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(32);
        loginTicket.setTicket("ticket");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);
    }

    @Test
    public void testSelectAndUpdateTicket(){
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("ticket");
        System.out.println(loginTicket);

        loginTicketMapper.updateStatus("ticket", 1);

        loginTicket = loginTicketMapper.selectByTicket("ticket");
        System.out.println(loginTicket);
    }

    @Test
    public void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message: list) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println(count);

        list = messageMapper.selectLetters("111_112", 0, 20);
        for (Message message: list) {
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println(count);

        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println(count);
    }

    @Test
    public void testSelectNotice() {
        System.out.println(messageMapper.selectLatestNotice(111, "comment"));
        System.out.println(messageMapper.selectNoticeCount(111, "comment"));
        System.out.println(messageMapper.selectNoticeUnreadCount(111, "comment"));
        List<Message> list = messageMapper.selectNotices(111, "comment", 0, 20);
        for (Message message: list) {
            System.out.println(message);
        }
    }
}
