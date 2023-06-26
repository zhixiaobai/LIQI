package com.zxb.liqi.exception;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 获取数据库连接异常
 */
public class ConnectionException extends RuntimeException {

    public ConnectionException(String errorMsg) {
        super(errorMsg);
    }
}
