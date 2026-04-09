package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.exception.AddressBookBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 新增地址
     * @param addressBook 地址
     */
    @Override
    @Transactional
    public void add(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(StatusConstant.DISABLE);

        addressBookMapper.insert(addressBook);
    }

    /**
     * 查询当前登录用户的所有地址信息
     * @return 地址列表
     */
    @Override
    public List<AddressBook> list() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.list(userId);
    }

    /**
     * 设置默认地址
     * @param addressBook 地址
     */
    @Override
    @Transactional
    public void setDefault(AddressBook addressBook) {
        Long userId = BaseContext.getCurrentId();
        addressBook.setUserId(userId);
        addressBookMapper.resetDefault(userId);
        int affected = addressBookMapper.setDefault(addressBook);
        if (affected == 0) {
            throw new AddressBookBusinessException("地址不存在或不属于当前用户");
        }
    }

    /**
     * 根据id查询地址
     * @param id 地址id
     * @return 地址信息
     */
    @Override
    public AddressBook getById(Long id) {
        return addressBookMapper.getById(id);
    }

    /**
     * 根据id删除地址
     * @param id 地址id
     */
    @Override
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

    /**
     * 根据id修改地址
     * @param addressBook 地址
     */
    @Override
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 查询默认地址
     * @return 默认地址
     */
    @Override
    public AddressBook getDefault() {
        Long userId = BaseContext.getCurrentId();
        return addressBookMapper.getDefault(userId);
    }
}
