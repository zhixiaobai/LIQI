package com.zxb.liqi.exception;

/**
 * @author Mr.M
 * @date 2023/6/10
 * @Description getBeanInfo失败
 */
public class GetBeanInfoErrorException extends RuntimeException {
    public GetBeanInfoErrorException(String errorMsg) {
        super(errorMsg);
    }
}
