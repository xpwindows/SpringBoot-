package com.jd.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("tb_item_cat")
public class ItemCat {
    @TableId(type = IdType.AUTO)
    private Long id;   //分类id
    private Long parentId;  //父id
    private String name;   //分类名称
    private Integer status;   //状态值
    private  Integer sortOrder; //分类排序号
    private Boolean isParent;  //是否是父级
}
