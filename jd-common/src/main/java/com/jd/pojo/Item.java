package com.jd.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import sun.net.ftp.FtpDirEntry;

@TableName("tb_item")
@Data
@Accessors(chain = true)
//如果get/set方法不全,添加该注解忽略转化.否则json转化会报错，找不到setImages方法
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item extends BasePojo{
    @TableId(type = IdType.AUTO)
    private Long id;		    //定义主键
    private String title;       //标题
    private String sellPoint;	//卖点信息
    private Long   price;		//商品价格
    private Integer num;		//商品数量
    private String barcode;		//二维码
    private String image;		//商品品图片信息 1.jpg,2.jpg,3.jpg
    private Long cid;			//商品分类信息
    private Integer status;		//商品状态 1正常，2下架，3删除'

    //el表达式获取数据时调用对象的get方法...
    public String[] getImages() {
        return image.split(",");
    }
}
