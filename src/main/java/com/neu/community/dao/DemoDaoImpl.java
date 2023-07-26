package com.neu.community.dao;

import org.springframework.stereotype.Repository;

@Repository("oldImplement")
public class DemoDaoImpl implements DemoDao {
    @Override
    public String select() {
        return "old implement";
    }
}
