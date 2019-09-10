package com.zhang.controller;

import com.zhang.pojo.*;
import com.zhang.service.*;
import comparator.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    @Autowired
    CategoryService categoryService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    OrderService orderService;

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

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(@RequestParam("name") String name, @RequestParam("password")String password,
                            HttpSession session){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name,password);

        if(null==user){
            return "fail";
        }
        session.setAttribute("user", user);
        return "success";
    }

    @RequestMapping("forecategory")
    public String category(int cid, String sort, ModelMap modelMap){
        Category c = categoryService.get(cid);
        productService.fill(c);
        productService.setSaleAndReviewNumber(c.getProducts());

        if(null!=sort){
            switch(sort){
                case "review":
                    Collections.sort(c.getProducts(),new ProductReviewComparator());
                    break;
                case "date" :
                    Collections.sort(c.getProducts(),new ProductDateComparator());
                    break;

                case "saleCount" :
                    Collections.sort(c.getProducts(),new ProductSaleCountComparator());
                    break;

                case "price":
                    Collections.sort(c.getProducts(),new ProductPriceComparator());
                    break;

                case "all":
                    Collections.sort(c.getProducts(),new ProductAllComparator());
                    break;
                default:
                    System.out.println("...排序这里");
            }
        }

        modelMap.addAttribute("c", c);
        return "fore/category";
    }

    @RequestMapping("forebuyone")
    public String buyone(int pid, int num, HttpSession session){
        Product p = productService.get(pid);
        //订单条的id
        int oiid = 0;

        User user =(User)  session.getAttribute("user");
        boolean found = false;
        //得到用户立即购买下的单
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for (OrderItem oi : ois) {
            //如果存在购买的产品在数据库中已经有了，就更新数量。
            if(oi.getProduct().getId().intValue()==p.getId().intValue()){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                oiid = oi.getId();
                break;
            }
        }

        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemService.add(oi);
            oiid = oi.getId();
        }
        return "redirect:forebuy?oiid="+oiid;
    }
    @RequestMapping("forebuy")
    public String buy(ModelMap modelMap, String[] oiid, HttpSession session){
        List<OrderItem> orderItems = new ArrayList<>();
        int total = 0;
        //得到订单总金额
        for(String sid : oiid){
            int id = Integer.parseInt(sid);
            OrderItem orderItem = orderItemService.get(id);
            total += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            orderItems.add(orderItem);
        }
        modelMap.addAttribute("total",total);
        //将订单条目放入session中。
        session.setAttribute("ois",orderItems);
        return "fore/buy";
    }

    @RequestMapping("foreaddCart")
    public String addCart(ModelMap modelMap, HttpSession session, int pid, int num){
        Product p = productService.get(pid);
        User user =(User)  session.getAttribute("user");
        boolean found = false;

        //得到该用户的所有订单
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId().intValue()==p.getId().intValue()){
                oi.setNumber(oi.getNumber()+num);
                orderItemService.update(oi);
                found = true;
                break;
            }
        }

        if(!found){
            OrderItem oi = new OrderItem();
            oi.setUid(user.getId());
            oi.setNumber(num);
            oi.setPid(pid);
            orderItemService.add(oi);
        }
        return "success";
    }

    @RequestMapping("forecart")
    public String cart(Model model, HttpSession session) {
        User user =(User)  session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        model.addAttribute("ois", ois);
        return "fore/cart";
    }

    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem( Model model,HttpSession session, int pid, int number) {
        User user =(User)  session.getAttribute("user");
        if(null==user) {
            return "fail";
        }
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId().intValue()==pid){
                oi.setNumber(number);
                orderItemService.update(oi);
                break;
            }

        }
        return "success";
    }

    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem( Model model,HttpSession session,int oiid){
        User user =(User)  session.getAttribute("user");
        if(null==user){
            return "fail";
        }
        orderItemService.delete(oiid);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String createOrder( Model model,Order order,HttpSession session){
        User user =(User)  session.getAttribute("user");
        //生成订单号
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setCreateDate(new Date());
        order.setUid(user.getId());
        order.setStatus(OrderService.waitPay);
        List<OrderItem> ois= (List<OrderItem>)  session.getAttribute("ois");

        float total =orderService.add(order,ois);
        return "redirect:forealipay?oid="+order.getId() +"&total="+total;
    }

    @RequestMapping("forepayed")
    public String payed(int oid, float total, Model model) {
        Order order = orderService.get(oid);
        order.setStatus(OrderService.waitDelivery);
        order.setPayDate(new Date());
        orderService.update(order);
        model.addAttribute("o", order);
        return "fore/payed";
    }

    @RequestMapping("forebought")
    public String bought( Model model,HttpSession session) {
        User user =(User)  session.getAttribute("user");
        List<Order> os= orderService.list(user.getId(),OrderService.delete);

        orderItemService.fill(os);

        model.addAttribute("os", os);

        return "fore/bought";
    }

    /**
     * 确认支付功能
     * 将订单填充数据发送给支付页面
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("foreconfirmPay")
    public String confirmPay( Model model,int oid) {
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        model.addAttribute("o", o);
        return "fore/confirmPay";
    }

    /**
     * 支付功能
     * 改变订单的状态为待评价
     * 设置支付时间。
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed( Model model,int oid) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);
        return "fore/orderConfirmed";
    }

    /**
     * 删除订单
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder( Model model,int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return "success";
    }

    /**
     * 去评价的页面
     * 1.填充订单的信息
     * 2.得到订单里的第一个产品，得到该所有评价
     * 3.填充产品的销量和评价数
     * @param model
     * @param oid
     * @return
     */
    @RequestMapping("forereview")
    public String review( Model model,int oid) {
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p.getId());
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("p", p);
        model.addAttribute("o", o);
        model.addAttribute("reviews", reviews);
        return "fore/review";
    }

    /**
     * 提交评价
     * 1.改变订单的的状态为完成
     * 2.评价对象中填充数据，包括评价内容，时间，和用户id，对应的产品id
     * @param model
     * @param session
     * @param oid
     * @param pid
     * @param content
     * @return
     */
    @RequestMapping("foredoreview")
    public String doreview( Model model,HttpSession session,@RequestParam("oid") int oid,@RequestParam("pid") int pid,String content) {
        Order o = orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);

        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);

        User user =(User)  session.getAttribute("user");
        Review review = new Review();
        review.setContent(content);
        review.setPid(pid);
        review.setCreateDate(new Date());
        review.setUid(user.getId());
        reviewService.add(review);

        return "redirect:forereview?oid="+oid+"&showonly=true";
    }
}


