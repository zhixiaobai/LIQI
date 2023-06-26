package com.zxb.liqi.utils;

import com.zxb.liqi.interfaces.IGetter;
import com.zxb.liqi.interfaces.ISetter;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description
 */
public class BeanUtil {
    /**
     * 缓存类-Lambda的映射关系
     */
    private static Map<Class, SerializedLambda> CLASS_LAMDBA_CACHE = new ConcurrentHashMap<>();

    /***
     * 转换方法引用为属性名
     * @param fn
     * @return
     */
    public static <T> String convertToFieldName(IGetter<T> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if(methodName.startsWith("get")){
            prefix = "get";
        }
        else if(methodName.startsWith("is")){
            prefix = "is";
        }
        if(prefix == null){
            throw new RuntimeException("无效的getter方法: "+methodName);
        }

        return StringUtil.uncapFirst(StringUtil.substringAfter(methodName, prefix));
    }

    /***
     * 转换setter方法引用为属性名
     * @param fn
     * @return
     */
    public static <T,R> String convertToFieldName(ISetter<T,R> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        if(!methodName.startsWith("set")){
            throw new RuntimeException("无效的setter方法: "+methodName);
        }
        // 截取set之后的字符串并转换首字母为小写（S为diboot项目的字符串工具类，可自行实现）
        return StringUtil.uncapFirst(StringUtil.substringAfter(methodName, "set"));
    }

    /***
     * 获取类对应的Lambda
     * @param fn
     * @return
     */
    private static SerializedLambda getSerializedLambda(Serializable fn){
        //先检查缓存中是否已存在
        SerializedLambda lambda = CLASS_LAMDBA_CACHE.get(fn.getClass());
        if(lambda == null){
            try{//提取SerializedLambda并缓存
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMDBA_CACHE.put(fn.getClass(), lambda);
            }
            catch (Exception e){
                throw new RuntimeException("获取SerializedLambda异常, class="+fn.getClass().getSimpleName(), e);
            }
        }
        return lambda;
    }
}

