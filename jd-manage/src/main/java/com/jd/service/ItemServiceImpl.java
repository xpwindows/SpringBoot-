package com.jd.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jd.mapper.ItemMapper;
import com.jd.pojo.Item;
import com.jd.pojo.ItemDesc;
import com.jd.vo.EasyUI_Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jd.mapper.ItemDescMapper;
import javax.swing.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{
    @Autowired(required = false)
    private ItemMapper itemMapper;
    @Autowired(required = false)
    private ItemDescMapper itemDescMapper;
    /**查询第一页   起始位置,显示条数
     SELECT * FROM tb_item LIMIT 0,20
     查询第二页
     //SELECT * FROM tb_item LIMIT 20,20
     查询第三页
     //SELECT * FROM tb_item LIMIT 40,20
     查询第N页
     SELECT * FROM tb_item LIMIT (n-1)*20,20
     */
    @Override
    public EasyUI_Table findItemByPage(Integer page, Integer rows) {
        //1.获取商品总数
        int total = itemMapper.selectCount(null);//分页查询 装入List集合
        int start=(page-1)*rows;
        List<Item> itemList=itemMapper.findItemByPage(start,rows);
        return new EasyUI_Table(total,itemList);
    }
    //新增商品，商品描述新增，声明式事务控制，
    @Transactional   //添加事务控制
    @Override
    public void savaItem(Item item, ItemDesc itemDesc) {
//前端传来的商品数据缺少：商品状态信息，创建时间，修改时间
        item.setStatus(1) // 1 表示商品正常状态
                .setCreated(new Date())
                .setUpdated(item.getCreated());
        itemMapper.insert(item);  //插入数据
        //因为商品描述表与商品表id示一致的，但是item表数据时主键自增，
        //自增只有在入库之后才能获取主键信息
        itemDesc.setItemId(item.getId())
                .setCreated(item.getCreated())  //创建时间
                .setUpdated(item.getCreated()); //修改时间
        itemDescMapper.insert(itemDesc);  //插入商品描述表
    }


    //修改商品
    @Override
    public void updateItem(Item item,ItemDesc itemDesc) {
        item.setUpdated(new Date());
        //sql: xxxx where id = 主键值
        itemMapper.updateById(item);
        //修改商品描述表信息,先完善itemDesc对象，设置id和修改时间
        itemDesc.setItemId(item.getId()).setUpdated(item.getUpdated());
        itemDescMapper.updateById(itemDesc);

    }
    //删除商品
    @Transactional    //事务控制
    @Override
    public void deleteItems(Long[] ids) {
        //把数组转换成集合
        List<Long> idList = Arrays.asList(ids);
        //删除商品表信息
        itemMapper.deleteBatchIds(idList);
        //删除描述表信息
        itemDescMapper.deleteBatchIds(idList);
    }

    //商品上下架
    @Override
    public void updateItem(Long[] ids, int status) {
            /**
             * 1.要与mapper接口打交道，需要按照mapper接口方法指定的参数类型来提供；
             *
             */
            //构建Item对象
            Item item = new Item();
            item.setStatus(status).setUpdated(new Date());
            //数组转换成集合
            List<Long> longList = Arrays.asList(ids);
            //构建修改条件对象
            UpdateWrapper<Item> updateWrapper = new UpdateWrapper<>();
            //用id作为修改的where
            updateWrapper.in("id",longList);
            //调用方法执行修改
            itemMapper.update(item,updateWrapper);

        }

    @Override
    public ItemDesc findItemDescById(Long itemId) {
        //调用mybatisplus方法查询
        return itemDescMapper.selectById(itemId);

    }
    @Override
    public Item findItemById(Long itemId) {

        return itemMapper.selectById(itemId);
    }

}

