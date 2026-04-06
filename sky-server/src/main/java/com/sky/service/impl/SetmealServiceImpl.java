package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐及其关联关系
     * @param setmealDTO 套餐数据传输对象
     */
    @Override
    @Transactional
    public void addWithSetmealDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);

        setmealMapper.insert(setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
            }

            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 分页查询
     *
     * @param setmealPageQueryDTO 套餐分页查询数据传输对象
     * @return 分页查询结果
     */
    @Override
    public PageResult<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        List<SetmealVO> setmealVOS = setmealMapper.pageQuery(setmealPageQueryDTO);

        PageInfo<SetmealVO> pageInfo = new PageInfo<>(setmealVOS);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 套餐起售、停售
     * @param status 状态
     * @param id 套餐id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        if (StatusConstant.ENABLE.equals(status)) {
            Integer count = setmealDishMapper.countDisableDishesBySetmealId(id);
            if (count != null && count > 0) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 根据id查询套餐
     * @param id 套餐id
     * @return 套餐视图对象
     */
    @Override
    public SetmealVO getWithSetmealDishesById(Long id) {
        SetmealVO setmealVO = new SetmealVO();
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    /**
     * 修改套餐及其包含的菜品
     * @param setmealDTO 套餐数据传输对象
     */
    @Override
    @Transactional
    public void updateWithSetmealDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        setmealMapper.update(setmeal);

        Long setmealId = setmealDTO.getId();
        setmealDishMapper.deleteBySetmealId(setmealId);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish
                    -> setmealDish.setSetmealId(setmealId));

            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 批量删除套餐
     * @param ids id列表
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        List<Setmeal> setmeals = setmealMapper.getByIds(ids);
        for (Setmeal setmeal : setmeals) {
            if (Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealMapper.deleteByIds(ids);
        setmealDishMapper.deleteBySetmealIds(ids);
    }

    /**
     * 动态查询套餐
     * @param setmeal 套餐对象
     * @return 套餐列表
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        return setmealMapper.list(setmeal);
    }


}
