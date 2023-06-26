package com.zxb.liqi.exception;

/**
 * @author Mr.M
 * @date 2023/6/10
 * @Description 未找到实体类中得字段属性
 */
public class NoFoundEntityFieldException extends RuntimeException{
    public NoFoundEntityFieldException(String errorMsg) {
        super(errorMsg);
    }
}
