package com.zxb.liqi.core.factory.repository;

import com.zxb.liqi.core.repository.DefaultRepository;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
public class RepositoryFactory<T> implements FactoryBean<T> {

    private Class<T> mapperInterface;

    public RepositoryFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public T getObject() throws Exception {
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, new DefaultRepository<>(mapperInterface));
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
