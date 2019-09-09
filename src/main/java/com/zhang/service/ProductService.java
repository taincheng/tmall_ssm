package com.zhang.service;

import com.zhang.pojo.Category;
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

    /**
     * 为类别下的产品添加属性
     * @param cs
     */
    void fill(List<Category> cs);

    void fill(Category c);

    void fillByRow(List<Category> cs);

    /**
     * 为产品设置其销量数和评价数
     * @param p
     */
    void setSaleAndReviewNumber(Product p);

    void setSaleAndReviewNumber(List<Product> ps);

}
