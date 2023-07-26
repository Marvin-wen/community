package com.neu.community;


import com.neu.community.entity.DiscussPost;
import com.neu.community.service.DiscussPostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ServiceTests {

    @Autowired
    private DiscussPostService discussPostService;

    @Test
    public void testDiscussPostService() {
        List<DiscussPost> list = discussPostService.findDiscussPost(0, 0, 10);
        for(DiscussPost post: list){
            System.out.println(post);
        }
    }
}
