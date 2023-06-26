package com.zxb.liqi.annotation;

import java.lang.annotation.*;

/**
 * @author Mr.M
 * @date 2023/3/6
 * @Description
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface ZmMultiDsTransaction {
}
