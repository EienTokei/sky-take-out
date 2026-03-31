package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
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
}
