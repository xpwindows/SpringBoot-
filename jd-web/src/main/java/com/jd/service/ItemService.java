package com.jd.service;

import com.jd.pojo.Item;
import com.jd.pojo.ItemDesc;

public interface ItemService {
    //商品查询方法
    Item fiandItemById(Long itemId);
//查询商品详情方法
    ItemDesc findItemDescById(Long itemId);

}
