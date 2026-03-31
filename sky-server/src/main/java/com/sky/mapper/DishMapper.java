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

    /**
     * 根据id查询菜品
     * @param id 菜品id
     * @return 菜品
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据id列表批量删除菜品
     * @param ids id列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据id批量查询菜品
     * @param ids 菜品id
     * @return 菜品列表
     */
    List<Dish> getByIds(List<Long> ids);

    /**
     * 更新菜品信息
     * @param dish 菜品对象
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);
}
