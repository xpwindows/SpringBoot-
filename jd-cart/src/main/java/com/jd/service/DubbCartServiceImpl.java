package com.jd.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jd.mapper.CartMapper;
import com.jd.pojo.Cart;
import com.jd.service.DubboCartService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class DubbCartServiceImpl implements DubboCartService {
    @Autowired
    private CartMapper cartMapper;

    @Override
    public List<Cart> findCartListByUserId(Long userId) {
        //根据userId查询出cartList集合
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        return cartMapper.selectList(queryWrapper);
    }

    /**
     *删除购物车商品；
     * 利用对象中不为null的属性充当where条件 来执行删除；
     */
    @Override
    public void deleteCart(Cart cart) {
        //sql语句：  delete from tb_cart where user_id=7 item_id=1474391969
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
        //调用mapper接口方法
        cartMapper.delete(queryWrapper);
    }

    /**
     * 新增购物车商品
     * 思路：
     * 根据 user_id item_id查询数据库；
     * null   表示第一次购买     新增入库
     * ！null 表示已经购买过   做数量的更新
     */
    @Override
    public void saveCart(Cart cart) {
        //构造查询条件
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",cart.getUserId())
                .eq("item_id",cart.getItemId());
        //查询出数据库中用户原来对应的购物车商品数量
        Cart cartDB = cartMapper.selectOne(queryWrapper);
        if (cartDB==null){
            //表示第一次新增，完善cart对象属性，插入到表中
            cart.setCreated(new Date()).setUpdated(cart.getCreated());
            cartMapper.insert(cart);
        }else {//购物车有该商品记录
            //把购物车商品的数量查询出来和前端传来的购物车商品数量相加
            int num1 = cartDB.getNum(); //获取到的购物车表中已经有的数量
            int num2=cart.getNum(); //前端传来的购物车新增商品数量
            int num=num1+num2;
            //完善cart对象属性，插入到表中
            Cart cartTemp = new Cart();
            cartTemp.setId(cartDB.getId())
                    .setNum(num)
                    .setUpdated(new Date());
            // sql语句： update tb_cart set num=3,updated=now() where id=57
            cartMapper.updateById(cartTemp);
        }
    }

    /**
     * 修改购物车商品数量
     * mybatis-plus 的update方法；需要两个参数：
     * 需要两个对象 一个要修改的数据对象cart ；还需要一个修改条件对象；
     */
    @Override
    public void updateCartNum(Cart cart) {
        Cart cartTemp = new Cart();
        cartTemp.setNum(cart.getNum())
                .setUpdated(new Date());
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",cart.getUserId())
                .eq("item_id",cart.getItemId());
        cartMapper.update(cartTemp,queryWrapper);
    }
}
