package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.exception.CategoryAlreadyExistsException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.ResourceNotFoundException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO 分类信息数据传输对象
     */
    @Override
    @Transactional
    public void add(CategoryDTO categoryDTO) {
        Category categoryByName = categoryMapper.getByName(categoryDTO.getName());
        if (categoryByName != null) {
            throw new CategoryAlreadyExistsException(MessageConstant.CATEGORY_ALREADY_EXISTS);
        }

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        // 默认禁用
        category.setStatus(StatusConstant.DISABLE);

        //category.setCreateTime(LocalDateTime.now());
        //category.setUpdateTime(LocalDateTime.now());
        //category.setCreateUser(BaseContext.getCurrentId());
        //category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询数据传输对象
     * @return 分页查询结果
     */
    @Override
    public PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        List<Category> categoryList = categoryMapper.pageQuery(categoryPageQueryDTO);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 更新分类状态
     * @param status 状态值
     * @param id 分类ID
     */
    @Override
    @Transactional
    public void updateStatus(Integer status, Long id) {
        // 业务校验
        Long currentId = BaseContext.getCurrentId();
        if (currentId == null) {
            throw new BaseException(MessageConstant.CURRENT_USER_NOT_FOUND);
        }

        Category category = Category.builder()
                .status(status)
                .id(id)
                //.updateTime(LocalDateTime.now())
                //.updateUser(currentId)
                .build();

        int rows = categoryMapper.update(category);
        if (rows == 0) {
            throw new ResourceNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
        }
    }

    /**
     * 更新分类
     * @param categoryDTO 分类信息数据传输对象
     */
    @Override
    @Transactional
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        //category.setUpdateTime(LocalDateTime.now());
        //category.setUpdateUser(BaseContext.getCurrentId());

        int rows = categoryMapper.update(category);
        if (rows == 0) {
            throw new ResourceNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
        }
    }

    /**
     * 根据ID删除分类
     * @param id 分类ID
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        int dishCount = dishMapper.countByCategoryId(id);
        if (dishCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        int setmealCount = setmealMapper.countByCategoryId(id);
        if (setmealCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deleteById(id);
    }

    /**
     * 根据类型查询分类
     * @param type 类型
     * @return 分类列表
     */
    @Override
    public List<Category> queryByType(Integer type) {
        return categoryMapper.queryByType(type);
    }
}
