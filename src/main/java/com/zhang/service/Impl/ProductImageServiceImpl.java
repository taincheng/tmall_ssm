package com.zhang.service.Impl;

import com.zhang.mapper.ProductImageMapper;
import com.zhang.pojo.ProductExample;
import com.zhang.pojo.ProductImage;
import com.zhang.pojo.ProductImageExample;
import com.zhang.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 1:03
 */
@Service
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    ProductImageMapper productImageMapper;


    @Override
    public void add(ProductImage pi) {
        productImageMapper.insert(pi);
    }

    @Override
    public void delete(int id) {
        productImageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(ProductImage pi) {
        productImageMapper.updateByPrimaryKeySelective(pi);
    }

    @Override
    public ProductImage get(int id) {
        return productImageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List list(int pid, String type) {
        ProductImageExample imageExample = new ProductImageExample();
        imageExample.createCriteria()
                .andPidEqualTo(pid)
                .andTypeEqualTo(type);
        imageExample.setOrderByClause("id desc");
        return productImageMapper.selectByExample(imageExample);
    }
}
