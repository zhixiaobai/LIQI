package com.zxb.liqi.interfaces;

import java.io.Serializable;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description setter方法接口定义
 */
@FunctionalInterface
public interface ISetter<T, U> extends Serializable {
    void accept(T t, U u);
}
