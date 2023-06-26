package com.zxb.liqi.exception;

/**
 * @author Mr.M
 * @date 2023/6/9
 * @Description 参数没找到@Param注解
 */
public class ParamNoFoundAnnotationException extends RuntimeException{

    public ParamNoFoundAnnotationException(String errorMsg) {
        super(errorMsg);
    }
}
