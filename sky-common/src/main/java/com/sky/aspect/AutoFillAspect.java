package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.reflect.MethodSignature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点: 所有被 @AutoFill 注解标记的方法
     */
    @Pointcut("@annotation(com.sky.annotation.AutoFill)")
    // ↑告诉 Spring 拦截所有被 @AutoFill 标记的方法
    public void autoFillPointCut() {
    }

    /**
     * 前置通知，在方法执行前为实体对象设置公共字段
     * @param joinPoint 连接点，代表程序执行过程中的一个“切入点”, 被拦截的方法的“快照”
     *                  比如我们拦截了 insert 方法，当 insert 被调用时，AOP 会把这个调用封装成一个 JoinPoint 对象，通过它可获取:
     *                  <ul>
     *                  <li>被调用的方法名</li>
     *                  <li>方法的参数</li>
     *                  <li>目标对象</li>
     *                  <li>...</li>
     *                  </ul>
     */
    @Before("autoFillPointCut()")
    // ↑表示在目标方法执行之前执行 autoFill 方法
    public void autoFill(JoinPoint joinPoint) {

        log.info("开始进行公共字段自动填充...");

        // 1. 获取被拦截方法上的注解信息（INSERT 还是 UPDATE）

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // ↑ Signature 对象表示被拦截方法的签名（方法名、参数类型等）
        // 但它是个父接口，需要强转为更具体的 MethodSignature
        // 因为我们要获取方法上的注解，必须使用 MethodSignature 才提供 getMethod() 方法
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        // signature.getMethod() 方法拿到 java.lang.reflect.Method 对象, 这个对象代表被拦截的方法
        // getAnnotation(AutoFill.class) 通过反射读取该方法上是否标注了 @AutoFill 注解
        // 有，就返回该注解的实例；没有，返回 null

        OperationType operationType = autoFill.value();
        // value() 是注解中声明的抽象方法（即注解的属性）
        // 调用 value() 就能获取到我们在注解里写的值，如 OperationType.INSERT

        // 2. 获取方法的参数（约定：第一个参数是实体对象）
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            log.warn("方法 {} 没有参数, 无法自动填充", signature.getName());
            return;
        }
        Object entity = args[0]; // 约定实体对象是第一个参数

        // 3. 准备要填充的值
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 4. 根据操作类型，通过反射为实体对象的属性赋值
        try {
            if (operationType == OperationType.INSERT) {
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            } else if (operationType == OperationType.UPDATE) {
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            }
        } catch (Exception e) {
            log.error("公共字段自动填充失败", e);
            throw new RuntimeException("自动填充失败", e);
        }
    }
}
