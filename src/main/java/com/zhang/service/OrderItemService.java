package com.zhang.service;

import com.zhang.pojo.Order;
import com.zhang.pojo.OrderItem;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 15:42
 */
public interface OrderItemService {
    void add(OrderItem c);

    void delete(int id);
    void update(OrderItem c);
    OrderItem get(int id);
    List list();

    void fill(List<Order> os);

    void fill(Order o);

    /**
     * 根据产品获取销售量
     * @param pid
     * @return
     */
    int getSaleCount(int  pid);
}
