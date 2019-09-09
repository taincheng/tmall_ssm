package com.zhang.service.Impl;

import com.zhang.mapper.OrderMapper;
import com.zhang.pojo.Order;
import com.zhang.pojo.OrderExample;
import com.zhang.pojo.User;
import com.zhang.service.OrderService;
import com.zhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 16:16
 */
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    UserService userService;

    @Override
    public void add(Order c) {
        orderMapper.insert(c);
    }

    @Override
    public void delete(int id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order c) {
        orderMapper.updateByPrimaryKeySelective(c);
    }

    @Override
    public Order get(int id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public List list() {
        OrderExample orderExample = new OrderExample();
        orderExample.setOrderByClause("id desc");
        List<Order> orders = orderMapper.selectByExample(orderExample);
        //每个订单添加user
        setUser(orders);
        return orders;
    }

    private void setUser(Order order){
        User user = userService.get(order.getUid());
        order.setUser(user);
    }

    private void setUser(List<Order> orders){
        for(Order order : orders){
            setUser(order);
        }
    }
}
