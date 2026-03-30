package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 新增分类
     * @param categoryDTO 分类信息数据传输对象
     */
    void add(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询数据传输对象
     * @return 分页查询结果
     */
    PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 更新分类状态
     * @param status 状态值
     * @param id 分类ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 更新分类
     * @param categoryDTO 分类信息数据传输对象
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 根据ID删除分类
     * @param id 分类ID
     */
    void deleteById(Long id);

    /**
     * 根据类型查询分类
     * @param type 类型
     * @return  分类列表
     */
    List<Category> queryByType(Integer type);
}
