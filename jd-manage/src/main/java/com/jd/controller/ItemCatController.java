package com.jd.controller;

import com.jd.anno.Cache;
import com.jd.enu.KEY_ENUM;
import com.jd.service.ItemCatService;
import com.jd.vo.EasyUI_Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 管理商品的分类情况
 */

/**类上的映射路径和方法上的映射路径合并就是
请求的最终路径名/item/cat/queryItemName
 */
@RequestMapping("/item/cat")
@RestController
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;
/**
 * 根据前端传来的分类id
 * 查询出对应的分类名称
 */
@RequestMapping("/queryItemName")
public String findItemCatNameById(Long itemCatId){
     return    itemCatService.findItemCatNameById(itemCatId);
}

/**
 * 实现商品分类树形结构查询，前端传来分类id号，根据id查询;
 * @RequestParam 注解用来绑定前端参数名称和 方法中的对应参数名称；
 * defaultValue设置默认值0
 */
@RequestMapping("/list")
@Cache(keyType = KEY_ENUM.AUTO)
public List<EasyUI_Tree> findItemCatByParentId(@RequestParam(value = "id",defaultValue ="0") Long parentId){
    //System.out.println("parentId="+parentId);
   return itemCatService.findItemCatByParentId(parentId);
   // return  itemCatService.findItemCatByCache(parentId);

}
}
