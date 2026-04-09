package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {

    /**
     * 新增地址
     * @param addressBook 地址
     */
    void add(AddressBook addressBook);

    /**
     * 查询当前登录用户的所有地址信息
     * @return 地址列表
     */
    List<AddressBook> list();

    /**
     * 设置默认地址
     * @param addressBook 地址
     */
    void setDefault(AddressBook addressBook);

    /**
     * 根据id查询地址
     * @param id 地址id
     * @return 地址信息
     */
    AddressBook getById(Long id);

    /**
     * 根据id删除地址
     * @param id 地址id
     */
    void deleteById(Long id);

    /**
     * 根据id修改地址
     * @param addressBook 地址
     */
    void update(AddressBook addressBook);
}
