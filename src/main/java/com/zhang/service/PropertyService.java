package com.zhang.service;


import com.zhang.pojo.Property;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/7 21:32
 */

public interface PropertyService {
    void add(Property property);
    void delete(int id);
    void update(Property property);

    /**
     * 按category的id来查找它的property
     * property和category是多对一的关系
     * @param cid
     * @return
     */
    List list(int cid);
    Property get(int id);
}
