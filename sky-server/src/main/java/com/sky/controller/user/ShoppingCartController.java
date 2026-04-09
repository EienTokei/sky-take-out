package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "用户端购物车相关接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCartDTO 购物车数据传输对象
     * @return 统一响应结果
     */
    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<Void> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车, 商品信息: {}", shoppingCartDTO);
        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查看购物车
     * @return 统一响应结果
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list() {
        log.info("查看购物车");
        List<ShoppingCart> shoppingCarts = shoppingCartService.show();
        return Result.success(shoppingCarts);
    }

    /**
     * 清空购物车
     * @return 统一响应结果
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result<Void> clean() {
        log.info("清空购物车");
        shoppingCartService.clean();
        return Result.success();
    }

    /**
     * 删除购物车中一个商品
     * @param shoppingCartDTO 购物车视图对象，包含要删除的商品信息
     * @return 统一响应结果，操作成功返回成功响应
     */
    @PostMapping("/sub") // HTTP DELETE请求映射到/sub路径
    @ApiOperation("删除购物车中一个商品") // API接口文档说明
    public Result<Void> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) { // 定义删除购物车商品的方法
        log.info("删除购物车中一个商品: {}", shoppingCartDTO); // 记录日志，输出删除的商品信息
        shoppingCartService.sub(shoppingCartDTO); // 调用服务层方法执行删除操作
        return Result.success(); // 返回操作成功的结果
    }
}
