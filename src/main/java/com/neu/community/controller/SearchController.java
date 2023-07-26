package com.neu.community.controller;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.neu.community.entity.DiscussPost;
import com.neu.community.entity.Page;
import com.neu.community.service.ElasticsearchService;
import com.neu.community.service.LikeService;
import com.neu.community.service.UserService;
import com.neu.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class SearchController implements CommunityConstant {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    // search?keyword=XXX
    @RequestMapping(path = "/search", method = RequestMethod.GET)
    public String search (String keyword, Model model, Page page) {
        // 搜索帖子
        Map<String, Object> resultMap = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        List<DiscussPost> searchResult = (List<DiscussPost>) resultMap.get("searchResult");
        // 聚合数据
        List<Map<String, Object>> list = new ArrayList<>();
        if (searchResult != null) {
            for (DiscussPost post : searchResult) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("user", userService.findUserById(post.getUserId()));
                map.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
                list.add(map);
            }
        }
        model.addAttribute("discussPosts", list);
        model.addAttribute("keyword", keyword);

        // 分页
        page.setPath("search?keyword=" + keyword);
        page.setRows(((Number) resultMap.get("resultCount")).intValue());

        return "/site/search";
    }
}
