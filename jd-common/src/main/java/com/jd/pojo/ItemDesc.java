package com.jd.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 商品描述表
 */
@Data
@Accessors(chain = true)
@TableName("tb_item_desc")
public class ItemDesc extends BasePojo{
    //item表id与itemDesc表一致的
    @TableId
    private Long itemId;    //商品ID
    private String itemDesc;  //描述信息
}
