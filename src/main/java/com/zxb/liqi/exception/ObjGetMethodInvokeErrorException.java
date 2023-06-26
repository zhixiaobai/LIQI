package com.zxb.liqi.exception;

/**
 * @author Mr.M
 * @date 2023/6/10
 * @Description 对象属性 getter方法invoke失败
 */
public class ObjGetMethodInvokeErrorException extends RuntimeException {
    public ObjGetMethodInvokeErrorException(String errorMsg) {
        super(errorMsg);
    }
}
