package com.zxb.liqi.exception;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 找到多个相关注解
 */
public class FoundMoreRelevantAnnotationException extends RuntimeException {

    public FoundMoreRelevantAnnotationException(String errorMsg) {
        super(errorMsg);
    }
}
