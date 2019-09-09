package com.zhang.controller;

import com.zhang.pojo.*;
import com.zhang.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/9 20:43
 */
@Controller
public class ForeController {

    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ReviewService reviewService;

    @RequestMapping("foreregister")
    public String register(User user, ModelMap modelMap){
        String username = user.getName();
        username = HtmlUtils.htmlEscape(username);
        user.setName(username);
        boolean boo = userService.isExist(username);

        //返回true表示不存在该用户
        if(!boo){
            modelMap.addAttribute("msg", "用户名已经被使用,不能使用");
            modelMap.addAttribute("user", null);
            return "fore/register";
        }
        userService.add(user);
        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogin")
    public String login(@RequestParam("name")String name, @RequestParam("password")String password,
                        HttpSession session, ModelMap modelMap){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name, password);
        if(null == user){
            modelMap.addAttribute("msg","账号密码错误");
            return "fore/login";
        }
        session.setAttribute("user",user);
        return "redirect:forehome";
    }

    @RequestMapping("forelogout")
    public String logout( HttpSession session) {
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product(int pid, ModelMap modelMap){
        Product product = productService.get(pid);

        //把图片添加
        List<ProductImage> singleImage = productImageService.list(pid, ProductImageService.type_single);
        List<ProductImage> detailImage = productImageService.list(pid, ProductImageService.type_detail);
        product.setProductDetailImages(detailImage);
        product.setProductSingleImages(singleImage);

        List<PropertyValue> pvs = propertyValueService.list(product.getId());
        List<Review> reviews = reviewService.list(product.getId());
        //把评价数和销量添加
        productService.setSaleAndReviewNumber(product);
        modelMap.addAttribute("reviews", reviews);
        modelMap.addAttribute("p", product);
        modelMap.addAttribute("pvs", pvs);
        return "fore/product";
    }
}

