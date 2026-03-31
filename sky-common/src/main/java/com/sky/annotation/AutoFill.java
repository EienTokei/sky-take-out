package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)         // 作用于方法
@Retention(RetentionPolicy.RUNTIME) // 指定注解保留到什么时候, 运行时保留，供反射读取
public @interface AutoFill {
    OperationType value();          // 指定操作类型：INSERT 或 UPDATE
    // ↑在注解里，value() 是一个抽象方法，使用时通过 @AutoFill(OperationType.INSERT) 这种方式赋值
    // 相当于告诉注解：调用 value() 方法时，返回 OperationType.INSERT 这个值
}
