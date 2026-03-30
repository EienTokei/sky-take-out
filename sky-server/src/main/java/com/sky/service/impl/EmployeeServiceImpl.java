package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.*;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 登录请求数据传输对象，包含用户名和密码
     * @return 验证通过的员工实体对象
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        // Objects 的 equals 方法会先进行非空判断, 再调用前者的 equals 方法来比较
        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO 员工信息传输对象
     */
    @Override
    @Transactional
    public void add(EmployeeDTO employeeDTO) {
        Employee employeeByUsername = employeeMapper.getByUsername(employeeDTO.getUsername());
        if (employeeByUsername != null) {
            throw new EmployeeAlreadyExistsException(MessageConstant.EMPLOYEE_ALREADY_EXISTS);
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);    // Spring 提供的工具，将同名属性复制过去

        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 从线程局部变量获取当前登录用户 ID（由拦截器在 JWT 校验时存入）
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     * <p>
     * 使用PageHelper分页插件自动拦截后续的查询语句，实现物理分页。
     * 根据DTO中的分页参数（page、pageSize）和可选条件（如姓名等）查询员工列表，
     * 并封装成包含总记录数和当前页数据的分页结果对象。
     * </p>
     * @param employeePageQueryDTO 员工分页查询数据传输对象
     * @return 分页结果对象
     */
    @Override
    public PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 启动分页：通过PageHelper设置当前页码和每页大小
        // 该方法通过线程变量（ThreadLocal）存储分页信息，使得后续执行的第一个 MyBatis 查询语句会被自动追加 LIMIT 子句实现物理分页。
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());

        // 此时PageHelper会拦截该查询，自动追加LIMIT子句
        List<Employee> employeeList = employeeMapper.pageQuery(employeePageQueryDTO);
        // 使用 PageInfo 包装查询结果
        PageInfo<Employee> pageInfo = new PageInfo<>(employeeList);
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     *
     * @param status 状态值
     * @param id 员工ID
     */
    @Override
    @Transactional
    // 声明该方法需要在 Spring 管理的事务中执行。如果方法执行过程中抛出未捕获的运行时异常，事务将回滚，保证数据库操作的原子性
    public void updateStatus(Integer status, Long id) {

        // 业务校验
        Long currentId = BaseContext.getCurrentId();
        if (currentId == null) {
            throw new BaseException(MessageConstant.CURRENT_USER_NOT_FOUND);
        }

        if (currentId.equals(id) && status == 0) {
            throw new BaseException(MessageConstant.CANNOT_DISABLE_SELF);
        }
        if (id == 1 && status == 0) {
            throw new BaseException(MessageConstant.CANNOT_DISABLE_ADMIN);
        }

        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .updateTime(LocalDateTime.now())
                .updateUser(currentId)
                .build();

        int rows = employeeMapper.update(employee);
        if (rows == 0) {
            // throw new BaseException("员工不存在");
            throw new ResourceNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
        }
    }

    /**
     * 根据ID查询员工
     * @param id 员工ID
     * @return 员工实体
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        if (employee == null) {
            //throw new BaseException("员工不存在");
            throw new ResourceNotFoundException(MessageConstant.RESOURCE_NOT_FOUND);
        }
        employee.setPassword("****");
        return employee;
    }

    /**
     * 更新员工信息
     * @param employeeDTO 员工信息传输对象
     */
    @Override
    @Transactional
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
    }

}
