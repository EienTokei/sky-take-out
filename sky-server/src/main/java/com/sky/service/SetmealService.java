package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /**
     * 新增套餐
     * @param setmealDTO 套餐数据传输对象
     */
    void addWithSetmealDishes(SetmealDTO setmealDTO);

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO 套餐分页查询数据传输对象
     * @return 分页查询结果
     */
    PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 套餐起售、停售
     * @param status 状态
     * @param id 套餐id
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据id查询套餐
     * @param id 套餐id
     * @return 套餐视图对象
     */
    SetmealVO getWithSetmealDishesById(Long id);

    /**
     * 修改套餐及其包含的菜品
     * @param setmealDTO 套餐数据传输对象
     */
    void updateWithSetmealDishes(SetmealDTO setmealDTO);

    /**
     * 批量删除套餐
     * @param ids id列表
     */
    void deleteBatch(List<Long> ids);
}
