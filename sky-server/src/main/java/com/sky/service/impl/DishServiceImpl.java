package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    /**
     * 新增菜品及对应的口味
     * @param dishDTO 菜品数据传输对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)   // 确保任何异常都回滚, 防止默认只对 RuntimeException 回滚
    public void addWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);

        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));

            dishFlavorMapper.insertBatch(flavors);  // 即使需要遍历设置dishId
            // 通过批量插入可减少数据库连接和 IO 次数，提升高并发下的吞吐量
        }
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO 菜品分页查询数据传输对象
     * @return 分页查询结果, 包含菜品视图对象
     */
    @Override
    public PageResult<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        List<DishVO> dishVOS = dishMapper.pageQuery(dishPageQueryDTO);

        PageInfo<DishVO> pageInfo = new PageInfo<>(dishVOS);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }
}
