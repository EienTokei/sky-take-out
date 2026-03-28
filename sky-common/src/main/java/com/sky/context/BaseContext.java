package com.sky.context;

/**
 * 线程局部变量上下文工具类
 * <p>
 * 用于在同一个线程内（通常是同一个 HTTP 请求）传递当前登录用户的 ID。<br>
 * 利用 ThreadLocal 的特性，保证线程安全，避免使用全局变量导致的数据错乱。<br>
 * 该类的值由 JWT 拦截器在校验令牌后设置，在后续的业务处理中（如 Service 层）可随时获取。<br>
 * 请求结束后应在拦截器的 afterCompletion 方法中调用 removeCurrentId() 清除数据，防止内存泄漏。
 * </p>
 */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
