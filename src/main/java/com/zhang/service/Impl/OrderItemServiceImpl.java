package com.zhang.service.Impl;

import com.zhang.mapper.OrderItemMapper;
import com.zhang.pojo.Order;
import com.zhang.pojo.OrderItem;
import com.zhang.pojo.OrderItemExample;
import com.zhang.pojo.Product;
import com.zhang.service.OrderItemService;
import com.zhang.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 15:42
 */
@Service
public class OrderItemServiceImpl implements OrderItemService{

    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ProductService productService;

    @Override
    public void add(OrderItem c) {
        orderItemMapper.insert(c);
    }

    @Override
    public void delete(int id) {
        orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(OrderItem c) {
        orderItemMapper.updateByPrimaryKeySelective(c);
    }

    @Override
    public OrderItem get(int id) {
        OrderItem orderItem = orderItemMapper.selectByPrimaryKey(id);
        setProduct(orderItem);
        return orderItem;
    }

    private void setProduct(OrderItem orderItem){
        Product product = productService.get(orderItem.getPid());
        orderItem.setProduct(product);
    }

    private void setProduct(List<OrderItem> orderItems){
        for(OrderItem orderItem : orderItems){
            setProduct(orderItem);
        }
    }

    @Override
    public List list() {
        OrderItemExample example = new OrderItemExample();
        example.setOrderByClause("id desc");
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        return orderItems;
    }

    @Override
    public void fill(List<Order> os) {
        for(Order order : os){
            fill(order);
        }
    }

    @Override
    public void fill(Order o) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andOidEqualTo(o.getId());
        example.setOrderByClause("id desc");
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        //每个orderItem都加入Product
        setProduct(orderItems);

        //计算订单的数量，总金额
        float totalMoney = 0;
        int totalNumber = 0;
        for(OrderItem orderItem : orderItems){
            totalMoney += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            totalNumber += orderItem.getNumber();
        }

        o.setTotal(totalMoney);
        o.setTotalNumber(totalNumber);
        o.setOrderItems(orderItems);
    }

    @Override
    public int getSaleCount(int pid) {
        OrderItemExample example = new OrderItemExample();
        example.createCriteria().andPidEqualTo(pid);
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        int total = 0;
        for(OrderItem o : orderItems){
            total += o.getNumber();
        }
        return total;
    }

    @Override
    public List<OrderItem> listByUser(int uid) {
        OrderItemExample example = new OrderItemExample();
        //查找该用户的订单项，但是没有加入订单。
        example.createCriteria().andUidEqualTo(uid).andOidIsNull();
        example.setOrderByClause("id desc");
        List<OrderItem> orderItems = orderItemMapper.selectByExample(example);
        setProduct(orderItems);
        return orderItems;
    }
}
