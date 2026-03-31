package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    /**
     * 新增菜品及对应的口味
     * @param dishDTO 菜品数据传输对象
     */
    void addWithFlavor(DishDTO dishDTO);
}
