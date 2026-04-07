package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 新增菜品及对应的口味
     * @param dishDTO 菜品数据传输对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)   // 确保任何异常都回滚, 防止默认只对 RuntimeException 回滚
    public void addWithFlavor(DishDTO dishDTO) {
        // 清空该分类下的菜品缓存
        // 先删缓存，再更新数据库, 避免暂存脏数据
        clearDishCacheByCategories(Collections.singleton(dishDTO.getCategoryId()));

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

    /**
     * 批量删除菜品
     * @param ids 删除菜品id
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 是否停售
        List<Dish> dishes = dishMapper.getByIds(ids);
        Set<Long> categoryIds = new HashSet<>();

        for (Dish dish : dishes) {
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            categoryIds.add(dish.getCategoryId());
        }
        // 是否被套餐关联
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds != null && !setmealIds.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 清空对应分类的缓存
        clearDishCacheByCategories(categoryIds);

        dishMapper.deleteByIds(ids);
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 根据id查询菜品及其口味
     * @param id 菜品id
     * @return 菜品视图对象
     */
    @Override
    public DishVO getWithFlavorById(Long id) {
        Dish dish = dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);

        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 修改菜品及其口味
     * @param dishDTO 菜品数据传输对象
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDTO dishDTO) {
        // 精确删除对应缓存, 多一次数据库查询换缓存精确删除，考虑到菜品修改频率远低于查询频率
        Dish oldDish = dishMapper.getById(dishDTO.getId());
        Set<Long> categoryIds = new HashSet<>();
        categoryIds.add(oldDish.getCategoryId());
        categoryIds.add(dishDTO.getCategoryId());
        clearDishCacheByCategories(categoryIds);

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 删除原口味
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 添加新口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        Long dishId = dishDTO.getId();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dishId));

            dishFlavorMapper.insertBatch(flavors);  // 即使需要遍历设置dishId
            // 通过批量插入可减少数据库连接和 IO 次数，提升高并发下的吞吐量
        }
    }

    /**
     * 更新菜品状态
     * @param status 状态
     * @param id 菜品id
     */
    @Override
    @Transactional
    public void updateStatus(Integer status, Long id) {
        // 精确删除对应缓存, 多一次数据库查询换缓存精确删除，考虑到菜品修改频率远低于查询频率
        Dish oldDish = dishMapper.getById(id);
        clearDishCacheByCategories(Collections.singleton(oldDish.getCategoryId()));

        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId 分类id
     * @return 菜品列表
     */
    @Override
    public List<Dish> queryByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

    /**
     * 动态查询菜品及其口味
     * @param dish 菜品对象
     * @return 菜品视图对象列表
     */
    @Override
    public List<DishVO> listWithFlavor(Dish dish) {

        String key = "dish_" + dish.getCategoryId();

        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof List<?>) {
            @SuppressWarnings("unchecked")  // 忽略警告
            List<DishVO> result = (List<DishVO>) cached;
            return result;
        }
        // 如果类型不匹配，删除该缓存并重新查询
        redisTemplate.delete(key);

        List<Dish> dishes = dishMapper.list(dish);
        List<DishVO> dishVOS = new ArrayList<>();

        for (Dish d : dishes) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d, dishVO);

            List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(d.getId());
            dishVO.setFlavors(dishFlavors);

            dishVOS.add(dishVO);
        }
        // 设置过期时间，避免内存膨胀
        redisTemplate.opsForValue().set(key, dishVOS, 30, TimeUnit.MINUTES);

        return dishVOS;
    }

    /**
     * 清除缓存公共方法
     * <p>传递集合而非模式字符串, 因为Redis 的 KEYS 命令会阻塞主线程, 导致性能问题</p>
     * @param categoryIds 分类id集合
     */
    private void clearDishCacheByCategories(Collection<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty())
            return;
        Set<String> keys = categoryIds.stream()
                .map((id-> "dish_" + id))
                .collect(Collectors.toSet());
        redisTemplate.delete(keys);
    }
}
