package com.neu.community.dao;

import com.neu.community.entity.LoginTicket;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


@Mapper
@Deprecated // 不推荐使用
public interface LoginTicketMapper {

    /*
    * 除了下面这种静态的SQL，通过<script></script>也可以实现动态SQL
    * 在<script></script>标签内可以正常使用<if>等语句
    * */

    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values(#{userId}, #{ticket}, #{status}, #{expired})"
    })
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select user_id, ticket, status, expired ",
            "from login_ticket ",
            "where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

    @Update({
            "update login_ticket set status=#{status} where ticket=#{ticket}"
    })
    int updateStatus(String ticket, int status);
}
