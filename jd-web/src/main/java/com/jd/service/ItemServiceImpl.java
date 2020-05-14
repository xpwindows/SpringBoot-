package com.jd.service;

import com.jd.pojo.Item;
import com.jd.pojo.ItemDesc;
import com.jd.util.HttpClientService;
import com.jd.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class  ItemServiceImpl implements ItemService {
    @Autowired
    private HttpClientService httpClient;
    /**
     *查询商品的方法
     */
    @Override
    public Item fiandItemById(Long itemId) {
        String url="http://manage.jd.com/web/item/findItemById/"+itemId;
        //调用工具类的方法来发送url地址
        String itemJson = httpClient.doGet(url);
        //利用工具类把获取的字符串转换成对象返回
        System.out.println("itemJson="+itemJson);
        return ObjectMapperUtil.toObject(itemJson, Item.class);
    }

    /**
     *查询商品详情
     */
    @Override
    public ItemDesc findItemDescById(Long itemId) {
        String url2="http://manage.jd.com/web/item/findItemDescById/"+itemId;
        //调用httpClientService工具类中的doGet
        String itemDescJson = httpClient.doGet(url2);
        return ObjectMapperUtil.toObject(itemDescJson,ItemDesc.class);
    }
}
