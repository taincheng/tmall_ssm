package com.zhang.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.pojo.User;
import com.zhang.service.UserService;
import com.zhang.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 15:03
 */
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("admin_user_list")
    public String list(ModelMap modelMap, Page page){
        PageHelper.offsetPage(page.getStart(), page.getCount());

        List<User> users = userService.list();
        int total = (int) new PageInfo<>(users).getTotal();
        page.setTotal(total);

        modelMap.addAttribute("us",users);
        modelMap.addAttribute("page",page);

        return "admin/listUser";
    }
}
