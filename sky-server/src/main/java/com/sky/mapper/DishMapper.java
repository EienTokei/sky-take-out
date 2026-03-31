package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    /**
     * 统计分类ID下的关联菜品数
     * @param categoryId 分类ID
     * @return 关联菜品数
     */
    @Select("select COUNT(*) from dish where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);

    /**
     * 插入菜品
     * @param dish 菜品实体
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);
}
