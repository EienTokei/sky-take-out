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
}
