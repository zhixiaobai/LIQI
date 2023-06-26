package com.zxb.liqi.exception;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 未找到有关的注解
 */
public class NoFoundRelevantAnnotationException extends RuntimeException {

    public NoFoundRelevantAnnotationException(String errorMsg) {
        super(errorMsg);
    }
}
