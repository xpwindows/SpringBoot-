package com.jd.util;

import com.alibaba.druid.util.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class HttpClientService {
    @Autowired
    private CloseableHttpClient httpClient;
    @Autowired
    private RequestConfig requestConfig;
    /**
     *url://www.jd.com?id=1&name=tom
     * 目的:发起请求获取服务器数据
     * 参数说明:
     * 	1.url地址
     *  2.用户提交的参数使用Map封装（暂时用不到，为以后准备）
     *  3.指定编码格式
     *步骤:
     *	1.校验字符集. 如果字符集为null 设定默认值
     *  2.校验params是否为null
     *  	null:表示用户get请求无需传参.
     *  	!null:需要传参,  get请求规则 url?key=value&key2=value2...
     *  3.发起http的get请求获取返回值结果
     */
    public String doGet(String url, Map<String,String> params, String charshet) {
        //1.校验传过来的字符集是否为null，如果为空设置字符集为utf-8
        if(StringUtils.isEmpty(charshet)) {
            charshet = "UTF-8";
        }
        /**
         * 2.校验参数是否为null，然后循环出参数拼接url
         * url:web.jd.com?id=1&name=tom&age=20
         * Map<entry<k,v>>
         */
        if(params!=null) {
            url +="?";
            //2.1遍历map集合 迭代器 fore循环；把每个参数的key和值循环出来拼接
            for (Map.Entry<String,String> entry : params.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
//把所有参数拼接到url后面组成最后的url地址
                url = url+key+"="+value+"&";
            }
            //2.2经过循环遍历最终url多个&
            url = url.substring(0,url.length()-1);
        }
        //3.发起get请求
        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);//定义请求超时时间
        String result = null;
        try { //发送请求，返回响应对象
            CloseableHttpResponse response =
                    httpClient.execute(get);
            //获取状态行信息并获取状态码
            if(response.getStatusLine().getStatusCode()==200) {
                //获取响应的消息实体并按照指定的字符格式转换成字符串
                result = EntityUtils.toString(response.getEntity(),charshet);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
//返回字符串的响应页面内容
        return result;
    }
    //重载方法.对方法进行扩充方便使用者调用
    public String doGet(String url) {
        return doGet(url, null, null);
    }
    public String doGet(String url,Map<String,String> params) {
        return doGet(url, params, null);
    }
}
