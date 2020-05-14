package com.jd.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * JSON与对象相互转换的工具API
 */
public class ObjectMapperUtil {
    //创建ObjectMapper对象
    private static final ObjectMapper mapper=new ObjectMapper();

    /**
     * 将对象转化成JSON
     */
    public static String toJSON(Object target){
        String result=null; //定义要返回的字符串
        //1.调用mapper对象里面的方法，把对象转换成字符串
        try {
            result= mapper.writeValueAsString(target);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return result;  //返回json字符串
    }
    /**
     * 2.将json串转换为对象
     * 泛型，即“参数化类型”，类似于方法中的变量参数，调用的时候传入具体的类型；
     * 泛型可以用在什么地方？类上可以用，接口上和方法都可以用；
     * 1).泛型类：class 类名<泛型>{}
     * 2).泛型接口：interface 接口名<泛型>{}
     * 3).泛型方法：访问修饰符 <泛型> 方法返回值类型 方法名(形参) {}
     */
    public static <T> T toObject(String json,Class<T> targetClass){
        T t=null;
        try {
            //把json串转换成对象
           t= mapper.readValue(json,targetClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return t;
    }

}
