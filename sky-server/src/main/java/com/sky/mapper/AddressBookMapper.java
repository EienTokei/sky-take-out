package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AddressBookMapper {

    /**
     * 取消默认地址
     * @param userId 用户id
     */
    @Update("update address_book set is_default = 0 where user_id = #{userId}")
    void resetDefault(Long userId);

    /**
     * 插入地址
     * @param addressBook 地址
     */
    @Insert("insert into address_book (user_id, consignee, sex, phone, " +
            "province_code, province_name, city_code, city_name, " +
            "district_code, district_name, detail, label, is_default) VALUES " +
            "(#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, " +
            "#{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, " +
            "#{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);
}
