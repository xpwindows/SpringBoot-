package com.jd.controller;
import com.jd.pojo.Item;
import com.jd.service.ItemService;
import com.jd.vo.EasyUI_Table;
import com.jd.vo.SysResult;
import com.jd.pojo.ItemDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;


    /**
     * 商品分页查询；
     * 请求路径：/item/query?page=1&rows=20
     * 参数接收；
     */

    @RequestMapping("/item/query")
    public EasyUI_Table findItemByPage(Integer page, Integer rows){

        return   itemService.findItemByPage(page,rows);

    }
    /**
     * 实现商品新增
     *
     * 准备AOP 统一异常处理机制.
     */
    @RequestMapping("/item/save")
    public SysResult saveItem(Item item, ItemDesc itemDesc){
        itemService.savaItem(item,itemDesc);  //新增成功返回
        return SysResult.success();
    }


    @RequestMapping("/item/update")
    public SysResult updateItem(Item item,ItemDesc itemDesc){
        itemService.updateItem(item,itemDesc);
        //有了统一异常处理，不在单独写调用失败的方法
        return SysResult.success();
    }
    //删除商品信息
    @RequestMapping("/item/delete")
    public SysResult deleteItems(Long[] ids){
        itemService.deleteItems(ids);
        return SysResult.success();
    }
    //商品下架
    @RequestMapping("/item/instock")
    public SysResult itemInstock(Long[] ids){
        int status = 2;//表示下架
        itemService.updateItem(ids,status);
        return SysResult.success();
    }
    //商品上架
    @RequestMapping("/item/reshelf")
    public SysResult itemResheIf(Long[] ids){
        int status = 1;//表示下架
        itemService.updateItem(ids,status);
        return SysResult.success();
    }
    //商品描述信息的回显
    @RequestMapping("/item/query/item/desc/{itemId}")
    public SysResult findItemDescByid(@PathVariable Long itemId){
        ItemDesc itemDesc=itemService.findItemDescById(itemId);
        return SysResult.success(itemDesc);
    }



}

