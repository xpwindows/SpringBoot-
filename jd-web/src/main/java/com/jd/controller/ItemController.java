package com.jd.controller;

import com.jd.pojo.Item;
import com.jd.pojo.ItemDesc;
import com.jd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;
    /**
     * 编辑jt-web前台服务器；
     * 1.从后台manage中获取数据，然后返回页面展示
     * url :  http://www.jt.com/items/1474391967
     */
    @RequestMapping("/items/{itemId}")
    public String findItemById(@PathVariable Long itemId, Model model){
        //调用ItemService接口中的方法获取数据
       Item item = itemService.fiandItemById(itemId);
       // 调用ItemService接口中的查询商品详情的方法
        ItemDesc itemDesc =itemService.findItemDescById(itemId);
       //将获取的item和itemDesc放入model中，方便el表达式取值
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        //返回的页面
        return "item";
    }
}
