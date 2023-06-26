package com.xb;

import com.zxb.liqi.core.parser.AnnotationParser;

import java.lang.reflect.Method;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException {
        Class<?> clazz = Class.forName("com.xb.mapper.UserMapper");
        Method selectList = clazz.getMethod("selectList");
        AnnotationParser annotationParser = new AnnotationParser(selectList, null);
//        DefaultExecutor defaultExecutor = new DefaultExecutor(annotationParser);
//        System.out.println(defaultExecutor.executor());
    }
}
