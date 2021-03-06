package com.zhang.service;

import com.zhang.pojo.Category;
import com.zhang.util.Page;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/7 15:52
 */
public interface CategoryService {
    /**
     * 分页查找Category
     * 使用了分页插件
     * @return
     */
    List<Category> list();

    /**
     * 添加新category
     * @param category
     */
    void add(Category category);

    /**
     * 删除指定id的category
     * @param id
     */
    void delete(int id);

    /**
     * 查询返回该id的category对象实体
     * @param id
     * @return
     */
    Category get(int id);

    /**
     * 更新category
     * @param category
     */
    void update(Category category);
}
