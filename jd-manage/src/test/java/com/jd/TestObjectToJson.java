package com.jd;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jd.pojo.ItemDesc;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class TestObjectToJson {
    //创建ObjectMapper对象
    private ObjectMapper mapper=new ObjectMapper();
    /**ObjectMapper类，提供了一些对象和JSON串的互相转换的方法；
     *
     */
    //将对象转化为JSON串；将json串转换成对象
    @Test
    public  void toJSON(){
        //1.创建ItemDesc对象并赋值
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(1000L)
                .setItemDesc("我是一个测试例子！")
                .setCreated(new Date())
                .setUpdated(new Date());
        //2.调用ObjectMapper对象的方法，将对象转换成json串
        try {
            String json = mapper.writeValueAsString(itemDesc);
            //打印输出
            System.out.println(json);
            //将json转换为对象，打印
            ItemDesc itemDesc1 = mapper.readValue(json, ItemDesc.class);
            System.out.println(itemDesc1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
