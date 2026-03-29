package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理
 */
@RestController
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

}
