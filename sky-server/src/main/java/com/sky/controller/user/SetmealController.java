package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Slf4j
@Api(tags = "用户端套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据分类id查询套餐
     * @param categoryId 分类id
     * @return 统一响应结果
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<Setmeal>> getByCategoryId(Long categoryId) {
        log.info("根据分类id {} 查询套餐", categoryId);
        Setmeal setmeal = Setmeal.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        List<Setmeal> setmeals = setmealService.list(setmeal);
        return Result.success(setmeals);
    }

    /**
     * 根据套餐id查询包含的菜品
     * @param id 套餐id
     * @return 统一响应结果
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品")
    public Result<List<DishItemVO>> getDishItemsById(@PathVariable Long id) {
        log.info("根据套餐id {} 查询包含的菜品", id);
        List<DishItemVO> dishItemVOS = setmealService.getDishItemsById(id);
        return Result.success(dishItemVOS);
    }
}
