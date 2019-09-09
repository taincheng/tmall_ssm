package com.zhang.service;

import com.zhang.pojo.Review;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/9 21:36
 */
public interface ReviewService {
    /**
     * 添加评价
     * @param c
     */
    void add(Review c);

    void delete(int id);
    void update(Review c);
    Review get(int id);

    /**
     * 得到产品下所有的评价
     * @param pid
     * @return
     */
    List list(int pid);

    /**
     * 得到总数
     * @param pid
     * @return
     */
    int getCount(int pid);
}
