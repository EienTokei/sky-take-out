package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询数据传输对象
     * @return 分页查询结果, 包含菜品视图对象
     */
    List<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
