package com.zhang.service;

import com.zhang.pojo.Order;
import com.zhang.pojo.OrderItem;

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

    /**
     * 生成订单order
     * @param c
     * @param ois
     * @return
     */
    float add(Order c,List<OrderItem> ois);

    /**
     *得到用户的某种状态的订单
     * @param uid
     * @param excludedStatus
     * @return
     */
    List list(int uid, String excludedStatus);
}
