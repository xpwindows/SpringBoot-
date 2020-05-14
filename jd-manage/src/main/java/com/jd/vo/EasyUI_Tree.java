package com.jd.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用来封装商品分类信息
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class EasyUI_Tree {
    private Long id; //分类Id号
    private String text; //分类名称
    private String state; //open 节点打开，close 节点关闭


}