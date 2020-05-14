package com.jd.service;

import com.jd.pojo.Item;
import com.jd.pojo.ItemDesc;
import com.jd.vo.EasyUI_Table;

public interface ItemService {
    //分页查询
    public EasyUI_Table findItemByPage(Integer page, Integer rows);
    //商品新增

    void savaItem(Item item, ItemDesc itemDesc);
    //商品修改
    void updateItem(Item item,ItemDesc itemDesc);
    //删除商品
    void deleteItems(Long[] ids);
    //商品上架
    void updateItem(Long[] ids, int status);
    //商品回显
    ItemDesc findItemDescById(Long itemId);

    Item findItemById(Long itemId);


}
