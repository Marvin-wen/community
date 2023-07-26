package com.neu.community.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary  //提高当前实现的优先级，优先被容器调用
public class DemoDaoNewImpl implements DemoDao {
    @Override
    public String select() {
        return "new implement";
    }
}
