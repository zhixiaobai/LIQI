package com.zxb.liqi.core.parser;

import com.zxb.liqi.annotation.*;
import com.zxb.liqi.enums.SqlOperationType;
import com.zxb.liqi.exception.FoundMoreRelevantAnnotationException;
import com.zxb.liqi.exception.NoFoundRelevantAnnotationException;
import com.zxb.liqi.exception.ParamNoFoundAnnotationException;
import com.zxb.liqi.interfaces.BaseParser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description 注解解析器
 */
public class AnnotationParser implements BaseParser {

    private final Method method;
    private final Object[] args;
    private SqlOperationType sqlOperationType;
    private final Class<?> methodReturnType;
    private String parseSql;
    private Map<String, Object> paramMap;
    private int batchSize;

    public AnnotationParser(Method method, Object[] args) {
        this.args = args;
        this.method = method;
        this.methodReturnType = method.getReturnType();
        this.annotationParse();
    }

    /**
     * 解析method 获取value
     */
    private void annotationParse() {
        this.parseSql = getAnnotationValue(
                parseOperationTypeAndReturnAnnotation(
                        this.method.getDeclaredAnnotations()));

        int parameterCount = this.method.getParameterCount();
        if (parameterCount == 1) {
            this.singleParamParse();
        } else if (parameterCount > 1){
            this.multipleParamParse();
        }
    }

    /**
     * 解析操作类型并且返回注解
     * @param annotations annotations
     * @return value
     */
    private Annotation parseOperationTypeAndReturnAnnotation(Annotation[] annotations) {
        int operationAnnotationCount = 0;
        Annotation backAnnotation = null;
        for (Annotation annotation: annotations) {
            String simpleName = annotation.annotationType().getSimpleName();
            for (SqlOperationType sqlOperationType : SqlOperationType.values()) {
                if (Objects.equals(sqlOperationType.getOperationType(), simpleName)) {
                    this.sqlOperationType = sqlOperationType;
                    operationAnnotationCount ++;
                    backAnnotation = annotation;
                }
            }
        }
        // 未找到相应注解
        if (operationAnnotationCount == 0) {
            throw new NoFoundRelevantAnnotationException("No found relevant annotation in the method name is " + this.method.getName());
        }
        // 找到多个相应的注解
        if (operationAnnotationCount > 1) {
            throw new FoundMoreRelevantAnnotationException("Found more relevant annotations in the method name is " + this.method.getName());
        }
        return backAnnotation;
    }

    /**
     * 获取注解value
     * @param annotation annotation
     * @return value
     */
    private String getAnnotationValue(Annotation annotation) {
        if (annotation instanceof Select) {
            return ((Select) annotation).value();
        } else if (annotation instanceof Insert) {
            this.batchSize = ((Insert) annotation).batchSize();
            return ((Insert) annotation).value();
        } else if (annotation instanceof Update) {
            return ((Update) annotation).value();
        } else if (annotation instanceof Delete) {
            return ((Delete) annotation).value();
        } else {
            throw new RuntimeException();
        }
    }

    /**
     * 单个参数解析
     */
    private void singleParamParse() {
        this.paramMap = new HashMap<>(1);
        Parameter parameter = this.method.getParameters()[0];
        Param param = parameter.getAnnotation(Param.class);
//        ListParam listParam = parameter.getAnnotation(ListParam.class);
        if (Objects.nonNull(param)) {
            this.paramMap.put(param.value(), this.args[0]);
//        } else if (Objects.nonNull(listParam)){
//            this.paramListMap.put(listParam.itemName(), (List<?>) this.args[0]);
        } else {
            this.paramMap.put(parameter.getName(), this.args[0]);
        }
    }

    /**
     * 多个参数解析
     */
    private void multipleParamParse() {
        this.paramMap = new HashMap<>();
        Parameter[] parameters = this.method.getParameters();
        for (int i = 0; i < parameters.length; i ++) {
            Param param = parameters[i].getAnnotation(Param.class);
//            ListParam listParam = parameters[i].getAnnotation(ListParam.class);
            if (Objects.nonNull(param)) {
                this.paramMap.put(param.value(), this.args[i]);
//            } else if (Objects.nonNull(listParam)) {
//                this.paramListMap.put(listParam.itemName(), (List<?>) this.args[i]);
            } else {
                throw new ParamNoFoundAnnotationException("Method of multiple parameters in the " + parameters[i].getName() + " field didn't find Param annotation");
            }
        }
    }

    @Override
    public String getParseSql() {
        return this.parseSql;
    }

    @Override
    public SqlOperationType getOperationType() {
        return this.sqlOperationType;
    }

    @Override
    public Class<?> getMethodReturnType() {
        return this.methodReturnType;
    }

    @Override
    public Map<String, Object> getParamMap() {
        return this.paramMap;
    }

    @Override
    public int getBatchSize() {
        return this.batchSize;
    }
}
