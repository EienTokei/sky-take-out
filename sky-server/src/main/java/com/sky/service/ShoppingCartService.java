package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {

    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车数据传输对象
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查看购物车
     * @return 当前用户的购物车列表
     */
    List<ShoppingCart> show();

    /**
     * 清空购物车
     */
    void clean();

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO 购物车视图对象
     */
    void sub(ShoppingCartDTO shoppingCartDTO);
}
