package com.zhang.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.pojo.Category;
import com.zhang.pojo.Property;
import com.zhang.service.CategoryService;
import com.zhang.service.PropertyService;
import com.zhang.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/7 22:46
 */
@Controller
public class PropertyController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_property_list")
    public String list(int cid, ModelMap modelMap, Page page){
        Category c = categoryService.get(cid);

        // 通过分页插件指定分页参数
        PageHelper.offsetPage(page.getStart(),page.getCount());
        //查找有指定的cid的property
        List<Property> ps = propertyService.list(cid);

        int total = (int) new PageInfo<>(ps).getTotal();
        page.setTotal(total);
        page.setParam("&cid="+c.getId());

        modelMap.addAttribute("ps", ps);
        modelMap.addAttribute("c", c);
        modelMap.addAttribute("page", page);
        return "admin/listProperty";
    }

    @RequestMapping("admin_property_add")
    public String add(Property property){
        propertyService.add(property);
        return "redirect:/admin_property_list?cid="+property.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(int id, ModelMap modelMap){
        Property property = propertyService.get(id);
        Category category = categoryService.get(property.getCid());
        property.setCategory(category);
        modelMap.addAttribute("p",property);
        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property property){
        propertyService.update(property);
        return "redirect:/admin_property_list?cid="+property.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id){
        Property property = propertyService.get(id);
        propertyService.delete(id);
        return "redirect:/admin_property_list?cid="+property.getCid();
    }
}
