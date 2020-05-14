package com.jd.service;

import com.jd.pojo.Cart;
import com.jd.pojo.Cart;

import java.util.List;

public interface DubboCartService {
    //通过userId查询购物车列表信息
    List<Cart> findCartListByUserId(Long userId);

    void deleteCart(Cart cart);

    void saveCart(Cart cart);

    void updateCartNum(Cart cart);
}
