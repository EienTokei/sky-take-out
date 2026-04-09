package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {

    /**
     * 动态查询购物车
     * @param shoppingCart 购物车对象
     * @return 符合条件的购物车列表
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据条件将数量-1
     * @param shoppingCart 购物车
     */
    void decNumByCondition(ShoppingCart shoppingCart);

    /**
     * 清空用户购物车
     * @param userId 用户id
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);

    /**
     * 删除当前用户所有数量为0的项
     * @param userId 用户id
     */
    @Delete("delete from shopping_cart where user_id = #{userId} and number = 0")
    void deleteZeroByUserId(Long userId);

//    /**
//     * 对商品进行判断, 存在则数量+1，不存在则插入
//     * @param shoppingCart 购物车对象
//     */
//    @Insert("insert into shopping_cart " +
//            "(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) VALUES " +
//            "(#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, 1, #{amount}, #{createTime}) " +
//            "on duplicate key update number = number + 1")
//    void upsert(ShoppingCart shoppingCart);

    /**
     * 插入新记录
     * @param cart 购物车对象
     */
    @Insert("INSERT INTO shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) " +
            "VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart cart);

    /**
     * 根据条件将数量+1
     * @param shoppingCart 购物车
     * @return 影响行数
     */
    int incNumByCondition(ShoppingCart shoppingCart);
}
