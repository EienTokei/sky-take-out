package com.sky.service;

import com.sky.dto.CategoryDTO;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO 分类信息数据传输对象
     */
    void add(CategoryDTO categoryDTO);
}
