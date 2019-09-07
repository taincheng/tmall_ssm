package com.zhang.service;

import com.zhang.pojo.Product;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 0:02
 */
public interface ProductService {
    void add(Product product);
    void delete(int id);
    void update(Product product);
    Product get(int id);
    List list(int cid);
    void setFirstProductImage(Product p);

}
