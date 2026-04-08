package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

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
     * 根据id更新数量
     * @param shoppingCart 购物车对象
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     * @param shoppingCart 购物车对象
     */
    @Insert("insert into shopping_cart " +
            "(name, image, user_id, dish_id, setmeal_id, dish_flavor, number, amount, create_time) VALUES " +
            "(#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{number}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 清空用户购物车
     * @param userId 用户id
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);
}
