package com.zhang.service.Impl;

import com.zhang.mapper.OrderMapper;
import com.zhang.pojo.Order;
import com.zhang.pojo.OrderExample;
import com.zhang.pojo.OrderItem;
import com.zhang.pojo.User;
import com.zhang.service.OrderItemService;
import com.zhang.service.OrderService;
import com.zhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    OrderItemService orderItemService;

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

    /**
     * 进行事务管理，出错回滚，拒绝脏数据。
     * @param c
     * @param ois
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
    @Override
    public float add(Order c, List<OrderItem> ois) {
        float total = 0;
        //将订单添加到数据库
        add(c);

        for(OrderItem orderItem : ois){
            orderItem.setOid(c.getId());
            orderItemService.update(orderItem);
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
        }
        return total;
    }

    @Override
    public List list(int uid, String excludedStatus) {
        OrderExample example =new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        example.setOrderByClause("id desc");
        return orderMapper.selectByExample(example);
    }
}
