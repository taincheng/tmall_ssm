package com.zhang.controller;

import com.zhang.pojo.Category;
import com.zhang.service.CategoryService;
import com.zhang.util.ImageUtil;
import com.zhang.util.Page;
import com.zhang.util.UploadedImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author TianCheng
 * @Date 2019/9/7 15:58
 */
@Controller
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @RequestMapping("admin_category_list")
    public String list(ModelMap modelMap, Page page){
        List<Category> cs = categoryService.list(page);
        int total = categoryService.total();
        page.setTotal(total);
        modelMap.addAttribute("page",page);
        modelMap.addAttribute("cs",cs);
        return "admin/listCategory";
    }

    @RequestMapping("admin_category_add")
    public String add(Category c, HttpSession session, UploadedImageFile uploadedImageFile) throws IOException {
        categoryService.add(c);
        File imageFolder = new File(session.getServletContext().getRealPath("img/category"));
        File file = new File(imageFolder,c.getId()+".jpg");
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        //把图片文件保存在指定的file中。
        uploadedImageFile.getImage().transferTo(file);
        BufferedImage image = ImageUtil.change2jpg(file);
        ImageIO.write(image,"jpg",file);
        return "redirect:/admin_category_list";
    }

}
