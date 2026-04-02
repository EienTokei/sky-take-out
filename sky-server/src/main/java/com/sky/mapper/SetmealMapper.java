package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {
    /**
     * 统计分类ID下的关联套餐数
     * @param categoryId 分类ID
     * @return 关联套餐数
     */
    @Select("select COUNT(*) from setmeal where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);

    /**
     * 插入套餐
     * @param setmeal 套餐对象
     */
    @Insert("insert into setmeal (category_id, name, price, status, description, " +
            "image, create_time, update_time, create_user, update_user) " +
            "VALUES (#{categoryId}, #{name}, #{price}, #{status}, #{description}, " +
            "#{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);
}
