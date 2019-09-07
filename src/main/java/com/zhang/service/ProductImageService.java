package com.zhang.service;

import com.zhang.pojo.ProductImage;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 1:02
 */
public interface ProductImageService {
    String type_single = "type_single";
    String type_detail = "type_detail";

    void add(ProductImage pi);
    void delete(int id);
    void update(ProductImage pi);
    ProductImage get(int id);
    List list(int pid, String type);
}
