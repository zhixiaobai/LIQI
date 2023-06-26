package com.zxb.liqi.annotation;

import com.zxb.liqi.annotation.aop.ZmMultiDsTransactionAspect;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 开启动态数据源
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(ZmMultiDsTransactionAspect.class)
public @interface EnableDynamicDB {
}
