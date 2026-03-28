package com.sky.exception;

/**
 * 员工已存在异常
 */
public class EmployeeAlreadyExistsException extends BaseException {
    public EmployeeAlreadyExistsException(String msg) {
        super(msg);
    }
}
