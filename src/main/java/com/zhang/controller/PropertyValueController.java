package com.zhang.controller;

import com.zhang.pojo.Product;
import com.zhang.pojo.PropertyValue;
import com.zhang.service.ProductService;
import com.zhang.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 13:53
 */
@Controller
public class PropertyValueController {

    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ProductService productService;

    @RequestMapping("admin_propertyValue_edit")
    public String edit(int pid, ModelMap modelMap){
        Product product = productService.get(pid);
        propertyValueService.init(product);
        List<PropertyValue> list = propertyValueService.list(product.getId());

        for(PropertyValue propertyValue : list){
            System.out.println(propertyValue);
        }

        modelMap.addAttribute("p",product);
        modelMap.addAttribute("pvs",list);
        return "admin/editPropertyValue";
    }

    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue pv){
        propertyValueService.update(pv);
        return "success";
    }
}
