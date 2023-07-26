package com.neu.community.util;

public interface CommunityConstant {

    /*
    * 激活成功
    * */
    int ACTIVATION_SUCCESS = 0;

    /*
    * 重复激活
    * */
    int ACTIVATION_REPEAT = 0;

    /*
     * 激活失败
     * */
    int ACTIVATION_FAILURE = 0;

    /*
    * 默认状态下的登录信息保存时长
    * */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 24 * 10;

    /*
    * 记住状态下的登录信息保存时长
    * */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 30;

    /*
     * 实体类型：帖子
     * */
    int ENTITY_TYPE_POST = 1;

    /*
     * 实体类型：评论
     * */
    int ENTITY_TYPE_COMMENT = 2;

    /*
     * 实体类型：拥护
     * */
    int ENTITY_TYPE_USER = 3;

    /*
     * 主题：评论
     * */
    String TOPIC_COMMENT = "comment";

    /*
     * 主题：点赞
     * */
    String TOPIC_LIKE = "like";

    /*
     * 主题：关注
     * */
    String TOPIC_FOLLOW = "follow";

    /*
     * 主题：发帖
     * */
    String TOPIC_PUBLISH = "publish";

    /*
     * 系统用户ID
     * */
    int SYSTEM_USER_ID = 1;
}
