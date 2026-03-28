package com.sky.constant;

/**
 * JWT 负载中声明（Claims）的常量键名
 * <p>
 * 该类集中定义了 JWT 令牌中使用的字段名称，用于统一管理，避免硬编码字符串。
 * 在生成或解析 JWT 时，均使用这些常量作为键值。
 * </p>
 */
public class JwtClaimsConstant {

    public static final String EMP_ID = "empId";
    public static final String USER_ID = "userId";
    public static final String PHONE = "phone";
    public static final String USERNAME = "username";
    public static final String NAME = "name";

}
