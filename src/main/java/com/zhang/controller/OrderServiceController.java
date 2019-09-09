package com.zhang.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.pojo.Order;
import com.zhang.service.OrderItemService;
import com.zhang.service.OrderService;
import com.zhang.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/8 16:29
 */
@Controller
public class OrderServiceController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @RequestMapping("admin_order_list")
    public String list(ModelMap modelMap, Page page){
        PageHelper.offsetPage(page.getStart(),page.getCount());
        List<Order> orders = orderService.list();

        int total = (int) new PageInfo<>(orders).getTotal();
        page.setTotal(total);
        //填充order的信息
        if(null != orders){
            orderItemService.fill(orders);
        }
        modelMap.addAttribute("page",page);
        modelMap.addAttribute("os",orders);

        return "admin/listOrder";
    }

    /**
     * 发货
     * @param o
     * @return
     * @throws IOException
     */
    @RequestMapping("admin_order_delivery")
    public String delivery(Order o) throws IOException {
        o.setDeliveryDate(new Date());
        o.setStatus(OrderService.waitConfirm);
        orderService.update(o);
        return "redirect:admin_order_list";
    }
}
