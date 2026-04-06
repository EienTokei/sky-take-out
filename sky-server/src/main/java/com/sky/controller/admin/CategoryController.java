package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController("adminCategoryController")
// 标记这个类是一个 RESTful 风格的控制器，所有方法返回的数据（如 JSON）会直接写入 HTTP 响应体
// 而不是跳转页面。
@RequestMapping("/admin/category")
// 为整个控制器定义一个 基础路径。该类中所有方法的 URL 都会以 /admin/category 开头
@Slf4j
// 自动生成一个名为 log 的日志对象，可以直接使用 log.info("...")、log.error("...") 打印日志
@Api(tags = "分类管理")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO 分类信息数据传输对象
     * @return 统一响应结果
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result<Void> add(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类: {}", categoryDTO);
        categoryService.add(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO 分类分页查询数据传输对象
     * @return 统一响应结果对象，其中包含分页查询结果
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult<Category>> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询, 参数: {}", categoryPageQueryDTO);
        PageResult<Category> pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用、禁用分类
     * @param status 状态值
     * @param id 分类ID
     * @return 统一响应结果对象
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result<Void> updateStatus(@PathVariable Integer status, Long id) {
        log.info("启用/禁用分类: {}, 状态: {}", id, status);
        // 基础校验
        if (status == null || (!status.equals(StatusConstant.DISABLE)
                && !status.equals(StatusConstant.ENABLE))) {
            throw new BaseException(MessageConstant.STATUS_PARAM_ERROR);
        }
        if (id == null) {
            throw new BaseException(MessageConstant.ID_EMPTY);
        }

        categoryService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO 分类信息数据传输对象
     * @return 统一响应结果
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result<Void> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类: {}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 根据ID删除分类
     * @param id 分类ID
     * @return 统一响应结果
     */
    @DeleteMapping
    @ApiOperation("根据ID删除分类")
    public Result<Void> deleteById(Long id) {
        log.info("删除分类, ID: {}", id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据类型查询分类
     * @param type 类型
     * @return  统一响应结果
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> queryByType(Integer type) {
        log.info("根据类型 {} 查询分类", type);
        List<Category> categories = categoryService.queryByType(type);
        return Result.success(categories);
    }
}
