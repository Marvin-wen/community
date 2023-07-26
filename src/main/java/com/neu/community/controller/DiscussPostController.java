package com.neu.community.controller;


import com.neu.community.entity.*;
import com.neu.community.event.EventProducer;
import com.neu.community.service.CommentService;
import com.neu.community.service.DiscussPostService;
import com.neu.community.service.LikeService;
import com.neu.community.service.UserService;
import com.neu.community.util.CommunityConstant;
import com.neu.community.util.CommunityUtil;
import com.neu.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost (String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录");
        }

        DiscussPost post = new DiscussPost();
        post.setContent(content);
        post.setTitle(title);
        post.setCreateTime(new Date());
        post.setUserId(user.getId());

        discussPostService.insertDiscussPost(post);

        // 触发发帖事件
        Event event = new Event()
                .setTopic(TOPIC_PUBLISH)
                .setUserId(user.getId())
                .setEntityType(ENTITY_TYPE_POST)
                .setEntityId(post.getId());
        eventProducer.fireEvent(event);

        // 报错的情况,将来统一处理
        return CommunityUtil.getJSONString(0, "发布成功！");
    }

    @RequestMapping(path = "/detail/{id}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("id") int id, Model model, Page page){
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(id);
        model.addAttribute("post", post);

        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 点赞数量
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
        model.addAttribute("likeCount", likeCount);

        // 点赞状态
        int likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, id);
        model.addAttribute("likeStatus", likeStatus);

        // 评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + id);
        page.setRows(post.getCommentCount());

        // 评论：给帖子的评论
        // 回复：给评论的评论
        // 评论VO列表
        List<Comment> commentList = commentService.selectCommentByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffSet(), page.getLimit());
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment: commentList){
                // 评论的VO
                Map<String, Object> commentMap = new HashMap<>();
                // 评论
                commentMap.put("comment", comment);
                // 作者
                commentMap.put("user", userService.findUserById(comment.getUserId()));

                // 点赞数量
                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentMap.put("likeCount", likeCount);

                // 点赞状态
                likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentMap.put("likeStatus", likeStatus);

                // 回复列表
                List<Comment> replayList = commentService.selectCommentByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                // 回复VO列表
                List<Map<String, Object>> replayVOList = new ArrayList<>();
                if (replayList != null) {
                    for (Comment replay: replayList) {
                        Map<String, Object> replayMap = new HashMap<>();
                        // 回复
                        replayMap.put("replay", replay);
                        // 作者
                        replayMap.put("user", userService.findUserById(replay.getUserId()));
                        // 回复的目标
                        User target = replay.getTargetId() == 0 ? null : userService.findUserById(replay.getTargetId());

                        // 点赞数量
                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, replay.getId());
                        replayMap.put("likeCount", likeCount);

                        // 点赞状态
                        likeStatus = hostHolder.getUser() == null ? 0 : likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, replay.getId());
                        replayMap.put("likeStatus", likeStatus);

                        replayMap.put("target", target);

                        replayVOList.add(replayMap);
                    }
                }
                commentMap.put("replays", replayVOList);
                // 回复的数量
                int replayCount = commentService.selectCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
                commentMap.put("replayCount", replayCount);

                commentVOList.add(commentMap);
            }
        }

        model.addAttribute("comments", commentVOList);

        model.addAttribute("offset", page.getOffSet());

        return "site/discuss-detail";
    }
}
