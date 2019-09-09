package com.zhang.service;

import com.zhang.pojo.Order;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 16:09
 */
public interface OrderService {
    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    void add(Order c);
    void delete(int id);
    void update(Order c);
    Order get(int id);
    List list();
}
