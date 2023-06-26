package com.zxb.liqi.core.repository;

import com.zxb.liqi.core.handler.UnifiedHandler;
import com.zxb.liqi.core.parser.AnnotationParser;
import com.zxb.liqi.core.executor.DefaultExecutor;
import com.zxb.liqi.interfaces.BaseHandler;
import com.zxb.liqi.interfaces.BaseRepository;

import java.lang.reflect.*;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
public class DefaultRepository<T> implements BaseRepository<T>, InvocationHandler {

    private Class<T> entityClass;

    public DefaultRepository(Class<T> mapperInterface) {
        ParameterizedType parameterizedType = (ParameterizedType) mapperInterface.getGenericInterfaces()[0];
        Type[] actualType = parameterizedType.getActualTypeArguments();
        this.entityClass = (Class<T>) actualType[0];
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AnnotationParser annotationParser = new AnnotationParser(method, args);
        BaseHandler handler = UnifiedHandler.getHandler(annotationParser);
        DefaultExecutor defaultExecutor = new DefaultExecutor(handler, this.entityClass);
        return defaultExecutor.executor();
    }
}
