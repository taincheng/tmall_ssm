package com.zhang.service;

import com.zhang.pojo.Product;
import com.zhang.pojo.PropertyValue;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 13:12
 */
public interface PropertyValueService {
    void init(Product p);
    void update(PropertyValue pv);

    PropertyValue get(int ptid, int pid);
    List<PropertyValue> list(int pid);
}
