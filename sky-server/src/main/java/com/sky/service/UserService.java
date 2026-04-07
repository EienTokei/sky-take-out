package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

public interface UserService {

    /**
     * 微信用户登录
     * @param userLoginDTO 用户登录数据传输对象
     * @return 用户登录视图对象
     */
    UserLoginVO wxLogin(UserLoginDTO userLoginDTO);
}
