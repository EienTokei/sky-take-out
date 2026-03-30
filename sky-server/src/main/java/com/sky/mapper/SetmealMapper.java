package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {
    /**
     * 统计分类ID下的关联套餐数
     * @param categoryId 分类ID
     * @return 关联套餐数
     */
    @Select("select COUNT(*) from setmeal where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);
}
