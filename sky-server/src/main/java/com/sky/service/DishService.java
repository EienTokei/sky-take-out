package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品及对应的口味
     * @param dishDTO 菜品数据传输对象
     */
    void addWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询数据传输对象
     * @return 分页查询结果对象
     */
    PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param ids 删除菜品id
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询菜品及其口味
     * @param id 菜品id
     * @return 菜品视图对象
     */
    DishVO getWithFlavorById(Long id);

    /**
     * 修改菜品及其口味
     * @param dishDTO 菜品数据传输对象
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 更新菜品状态
     * @param status 状态
     * @param id 菜品id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类id
     * @return 菜品列表
     */
    List<Dish> queryByCategoryId(Long categoryId);

    /**
     * 动态查询菜品及其口味
     * @param dish 菜品对象
     * @return 菜品视图对象列表
     */
    List<DishVO> listWithFlavor(Dish dish);
}
