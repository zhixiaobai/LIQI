package com.zxb.liqi.interfaces;

import java.io.Serializable;



/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description getter方法接口定义
 */

@FunctionalInterface
public interface IGetter<T> extends Serializable {
    Object apply(T source);
}
