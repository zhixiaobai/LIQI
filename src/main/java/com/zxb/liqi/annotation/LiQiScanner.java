package com.zxb.liqi.annotation;

import com.zxb.liqi.config.DbConfig;
import com.zxb.liqi.core.RepositoryScanner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 扫描器
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RepositoryScanner.class, DbConfig.class})
public @interface LiQiScanner {
}
