package com.sky.mapper;

import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 查询关联了各菜品id的所有套餐id
     * @param dishIds 菜品id列表
     * @return 套餐id列表
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐菜品关系
     * @param setmealDishes 套餐菜品关系
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询对应关联关系
     * @param setmealId 套餐id
     * @return 套餐菜品关系列表
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);

    /**
     * 根据套餐id删除对应菜品关系
     * @param setmealId 套餐id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐id批量删除对应菜品关系
     * @param setmealIds 套餐id列表
     */
    void deleteBySetmealIds(List<Long> setmealIds);

    /**
     * 添加套餐id下的停售菜品数
     * @param setmealId 套餐id
     * @return 停售菜品数
     */
    Integer countDisableDishesBySetmealId(Long setmealId);

    /**
     * 根据套餐id查询包含的菜品
     * @param id 套餐id
     * @return 包含菜品列表
     */
    List<DishItemVO> getDishItemsById(Long id);
}
