package com.jd.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jd.pojo.Cart;
import com.jd.service.DubboCartService;
import com.jd.util.UserThreadLocal;
import com.jd.vo.SysResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 与购物车相关的
 */
@Controller
@RequestMapping("/cart")
public class CartController {
    @Reference(timeout = 3000,check = false)
    private DubboCartService cartService;


    /**
     * 当用户点击购物车按钮时，应该展现用户的购物记录；
     * (由于userId暂时没办法获取，我们暂时写死)
     * 业务实现：
     * 1、查询用户购物车表；
     * 2、把查询出的集合装入model中；
     *3、返回cart.jsp购物车页面；
     */
    @RequestMapping("/show")
    public String show(Model model){
        //1.调用第三方接口方法来查询出购物车数据
        Long userId = UserThreadLocal.get().getId();
       List<Cart> cartList= cartService.findCartListByUserId(userId);
        //2.把查询出的集合装入model中
        model.addAttribute("cartList",cartList);
        //3.返回cart.jsp购物车页面
        return "cart";
    }
    /**
     * 购物车商品删除；
     * url: /cart/delete/${cart.itemId}
     *最后返回购物车页面；
     * rest风格传参；接收参数的时候可以使用对象接收；
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteCart(Cart cart){
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        cartService.deleteCart(cart);
        //应该采用重定向方式获取数据，返回购物车页面
        return "redirect:/cart/show.html";
    }
    /**购物车商品新增；
     * url： /cart/add/{itemId}
     * 最后返回购物车页面；
     * rest风格传参；接收参数的时候可以使用对象接收；
     *
     */
    @RequestMapping("/add/{itemId}")
    public String saveCart(Cart cart){
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        cartService.saveCart(cart);
        //重定向到购物车页面
        return "redirect:/cart/show.html";
    }
    /**
     * 购物车商品数量修改方法
     * url: /cart/update/num/{itemId}/{num}
     * 返回给前端的数据  SysResult
     */
    @ResponseBody
    @RequestMapping("/update/num/{itemId}/{num}")
    public SysResult updateCartNum(Cart cart){
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        //调用dubbo服务的修改方法
        cartService.updateCartNum(cart);
        return SysResult.success();
    }
}
