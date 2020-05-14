package com.jd.controller;

import com.jd.anno.Cache;
import com.jd.enu.KEY_ENUM;
import com.jd.pojo.Item;
import com.jd.pojo.ItemDesc;
import com.jd.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web/item")

public class WebItemController {
    //后台Controller
    @Autowired
    private ItemService itemService;
    //查询商品信息
    @RequestMapping("/findItemById/{itemId}")
    @Cache(keyType = KEY_ENUM.AUTO)
    public Item findItemById(@PathVariable Long itemId){
        return itemService.findItemById(itemId);

    }
    //商品描述信息查询
    @RequestMapping("/findItemDescById/{itemId}")
    @Cache(keyType = KEY_ENUM.AUTO)
    public ItemDesc findItemDescByid(@PathVariable Long itemId){
        return itemService.findItemDescById(itemId);
    }
}
