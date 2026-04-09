package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "用户端地址簿相关接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址
     * @param addressBook 地址
     * @return 统一响应结果
     */
    @PostMapping
    @ApiOperation("新增地址")
    public Result<Void> add(@RequestBody AddressBook addressBook) {
        log.info("新增地址: {}", addressBook);
        addressBookService.add(addressBook);
        return Result.success();
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return 统一响应结果
     */
    @GetMapping("/list")
    @ApiOperation("查询当前登录用户的所有地址信息")
    public Result<List<AddressBook>> list() {
        log.info("查询当前登录用户的所有地址信息");
        List<AddressBook> addressBooks = addressBookService.list();
        return Result.success(addressBooks);
    }

    /**
     * 设置默认地址
     * @param addressBook 地址
     * @return 统一响应结果
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result<Void> setDefault(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址: {}", addressBook);
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 根据id查询地址
     * @param id 地址id
     * @return 统一响应结果
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询地址")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据id查询地址: {}", id);
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据id删除地址
     * @param id 地址id
     * @return 统一响应结果
     */
    @DeleteMapping
    @ApiOperation("根据id删除地址")
    public Result<Void> deleteById(Long id) {
        log.info("根据id删除地址: {}", id);
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 根据id修改地址
     * @param addressBook 地址
     * @return 统一响应结果
     */
    @PutMapping
    @ApiOperation("根据id修改地址")
    public Result<Void> update(@RequestBody AddressBook addressBook) {
        log.info("根据id修改地址");
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 查询默认地址
     * @return 统一响应结果
     */
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefault() {
        log.info("查询默认地址");
        AddressBook addressBook = addressBookService.getDefault();
        return Result.success(addressBook);
    }
}
