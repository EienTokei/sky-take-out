package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.BaseException;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.Api;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO 登录请求数据传输对象，包含用户名和密码
     * @return 统一响应结果，包含登录成功后的员工信息及 JWT 令牌
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录", notes = "使用用户名和密码登录, 成功返回token")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成功后，构建 JWT 令牌的负载（claims），将员工ID放入其中
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());

        // 调用 JwtUtil 工具类生成 JWT 字符串
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        // 构建返回给前端的视图对象，包含员工ID、用户名、姓名和生成的令牌
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return 统一响应结果，返回成功信息（无具体数据）
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO 员工信息传输对象
     * @return 统一响应结果
     */
    @PostMapping    // 表示这是一个POST请求
    @ApiOperation("新增员工")
    public Result<Void> save(@RequestBody EmployeeDTO employeeDTO) {
        // @RequestBody 表示将前端传的JSON自动转为EmployeeDTO对象
        log.info("新增员工: {}", employeeDTO);
        employeeService.save(employeeDTO);  // 调用 Service 层方法
        return Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO 员工分页查询数据传输对象
     * @return 统一响应结果对象，其中包含分页查询结果
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult<Employee>> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询, 参数: {}", employeePageQueryDTO);

        PageResult<Employee> pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     *
     * @param status 状态值，0表示禁用，1表示启用
     * @param id 员工ID，不能为空
     * @return 统一响应结果对象
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用/禁用员工账号")
    // Controller 层的参数建议使用包装类，特别是对于非必须的、需要校验的参数。这是 Spring 开发中的常见实践
    public Result<Void> updateStatus(@PathVariable("status") Integer status, Long id) {
        log.info("启用/禁用员工账号: {}, 状态: {}", id, status);
        // 基础校验
        if (status == null || (status != 0 && status != 1)) {
            throw new BaseException(MessageConstant.STATUS_PARAM_ERROR);
        }
        if (id == null) {
            throw new BaseException(MessageConstant.EMPLOYEE_ID_EMPTY);
        }

        employeeService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 根据ID查询员工信息
     * @param id 员工ID
     * @return 统一响应结果
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询员工")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("员工信息查询, ID: {}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 编辑员工信息
     * @param employeeDTO 员工信息传输对象
     * @return 统一响应结果
     */
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result<Void> update(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息, 内容: {}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }
}
