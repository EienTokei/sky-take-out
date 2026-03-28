package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 员工数据访问层接口
 * <p>
 * 使用 MyBatis 注解方式定义 SQL 映射，负责员工相关的数据库操作。<br>
 * 通过 @Mapper 注解标识为 MyBatis 的 Mapper 接口，由 Spring 自动扫描并注入代理实现。
 * </p>
 */
@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username 员工登录名
     * @return 匹配的员工实体对象，若不存在则返回 null
     */
    @Select("select * from employee where username = #{username}")
    // 表字段名与实体类属性通过 #{} 占位符对应，MyBatis 会调用实体对象的 getter 方法获取值
    Employee getByUsername(String username);

    /**
     * 插入员工
     * @param employee 员工实体对象，包含除 id 外的完整信息
     */
    @Insert("insert into employee (username, name, password, phone, sex, id_number, status, " +
            "create_time, update_time, create_user, update_user) " +
            "values (#{username}, #{name}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, " +
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    // 驼峰命名与下划线字段自动映射
    @Options(useGeneratedKeys = true, keyProperty = "id")
    // 告诉 MyBatis 使用数据库自动生成的主键（如 MySQL 的自增 ID）并将自动生成的主键值设置回传入的实体类对象的 id 属性上
    void insert(Employee employee);
    /*
      调用 employeeMapper.insert(employee) 时，传入一个 Employee 对象，此时 employee.getId() 为 null（或默认值）。
      MyBatis 执行 INSERT 语句，数据库生成自增主键。
      MyBatis 获取该主键值，通过反射或 setter 方法，将主键值设置到 employee 对象的 id 属性上。
      方法执行完毕后，employee.getId() 就是刚刚生成的主键值。
    */
}
