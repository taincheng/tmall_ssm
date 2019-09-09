package com.zhang.service;

import com.zhang.pojo.User;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 14:56
 */
public interface UserService {
    void add(User c);
    void delete(int id);
    void update(User c);
    User get(int id);

    /**
     * 得到所有的用户
     * @return List
     */
    List<User> list();

    /**
     * 判断用户是否存在
     * @param name
     * @return
     */
    boolean isExist(String name);
}
