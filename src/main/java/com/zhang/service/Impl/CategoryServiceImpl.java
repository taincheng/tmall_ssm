package com.zhang.service.Impl;

import com.zhang.mapper.CategoryMapper;
import com.zhang.pojo.Category;
import com.zhang.service.CategoryService;
import com.zhang.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/7 15:55
 */
@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryMapper categoryMapper;


    @Override
    public List<Category> list(Page page) {
        return categoryMapper.list(page);
    }

    @Override
    public int total() {
        return categoryMapper.total();
    }

    @Override
    public void add(Category category) {
        categoryMapper.add(category);
    }

    @Override
    public void delete(int id) {
        categoryMapper.delete(id);
    }
}
