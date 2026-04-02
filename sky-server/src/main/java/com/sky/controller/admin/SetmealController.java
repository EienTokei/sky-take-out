package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "套餐管理")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO 套餐数据传输对象
     * @return 统一返回对象
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result<Void> add(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐: {}", setmealDTO);
        setmealService.addWithSetmealDishes(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO 套餐分页查询数据传输对象
     * @return 统一响应结构
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult<SetmealVO>> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询, {}", setmealPageQueryDTO);
        PageResult<SetmealVO> setmealVOPageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(setmealVOPageResult);
    }
}
