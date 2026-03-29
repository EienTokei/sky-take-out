package com.sky.mapper;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 根据名称查询分类
     * @param name 分类名称
     * @return 分类实体
     */
    @Select("select * from category where name = #{name}")
    Category getByName(String name);

    /**
     * 插入分类
     * @param category 分类实体对象
     */
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "values " +
            "(#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Category category);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO  分类分页查询数据传输对象
     * @return 符合条件的员工列表
     */
    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
