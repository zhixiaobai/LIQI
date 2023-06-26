package com.zxb.liqi.core;

import com.zxb.liqi.annotation.TableField;
import com.zxb.liqi.annotation.TableId;
import com.zxb.liqi.annotation.TableName;
import com.zxb.liqi.core.cache.Cache;
import com.zxb.liqi.core.factory.repository.RepositoryFactory;
import com.zxb.liqi.interfaces.BaseRepository;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Mr.M
 * @date 2023/6/8
 * @Description
 */
public class RepositoryScanner implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, ApplicationContextAware {
    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private MetadataReaderFactory metadataReaderFactory;
    private ResourcePatternResolver resourcePatternResolver;
    private ApplicationContext applicationContext;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        // 获取启动类所在包
        List<String> packages = AutoConfigurationPackages.get(applicationContext);
        // 开始扫描包，获取字节码
        Set<Class<?>> beanClazzSet = scannerPackages(packages.get(0));
        for (Class<?> beanClazz : beanClazzSet) {
            // 判断是否是需要被代理的接口
            if (isNotNeedProxy(beanClazz)) {
                if (AnnotatedElementUtils.findMergedAnnotation(beanClazz, TableName.class) == null) {
                    continue;
                }
            }
            TableName tableName = beanClazz.getAnnotation(TableName.class);
            if (ObjectUtils.isEmpty(tableName)) {
                // BeanDefinition构建器
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
                GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

                //在这里，我们可以给该对象的属性注入对应的实例。
                definition.getConstructorArgumentValues()
                        .addGenericArgumentValue(beanClazz);
                // 如果构造函数中不止一个值,可以使用这个api,指定是第几个参数
                //   definition.getConstructorArgumentValues()
                //					.addIndexedArgumentValue(1,null);

                // 定义Bean工程(最终会用上面add的构造函数参数值作为参数调用RepositoryFactory的构造方法)
                definition.setBeanClass(RepositoryFactory.class);
                //这里采用的是byType方式注入，类似的还有byName等
//            definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                String simpleName = beanClazz.getSimpleName();
                // 首字母小写注入容器
                simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
                beanDefinitionRegistry.registerBeanDefinition(simpleName, definition);
            } else {
                Field[] declaredFields = beanClazz.getDeclaredFields();
                Map<String, String> fieldMap = new HashMap<>();
                for (Field field: declaredFields) {
                    TableField tableField = field.getAnnotation(TableField.class);
                    TableId tableId = field.getAnnotation(TableId.class);
                    if (Objects.nonNull(tableId)) {
                        fieldMap.put(field.getName(), tableId.value());
                    } else if (Objects.nonNull(tableField)) {
                        if (tableField.exist()) {
                            fieldMap.put(field.getName(), tableField.value());
                        }
                    } else {
                        fieldMap.put(field.getName(), field.getName());
                    }
                }
                Cache.lruCache.put(beanClazz, fieldMap);
            }
        }
    }

    /**
     * description: 是否是需要被代理的接口
     * version: 1.0
     * date: 2021/3/7 17:35
     * author: Silwings
     *
     * @param beanClazz 类对象
     * @return boolean 如果不是需要被代理的接口返回true
     */
    private boolean isNotNeedProxy(Class<?> beanClazz) {
        // 如果不是接口,或者其实现的接口小于等于0,或者其实现的第一个接口不是Repository,或者没有添加@NeedProxy注解,则说明不是需要被代理的接口
        // || null == AnnotatedElementUtils.findMergedAnnotation(beanClazz, LqProxy.class)
        return !beanClazz.isInterface() || beanClazz.getInterfaces().length <= 0 || beanClazz.getInterfaces()[0] != BaseRepository.class;
    }

    /**
     * description: 根据包路径获取包及子包下的所有类
     * version: 1.0
     * date: 2021/3/7 17:34
     * author: Silwings
     *
     * @param basePackage 需要扫描的包
     * @return java.util.Set<java.lang.Class < ?>>
     */
    private Set<Class<?>> scannerPackages(String basePackage) {
        Set<Class<?>> set = new LinkedHashSet<>();
        // 此处固定写法即可,含义就是包及子包下的所有类
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(basePackage) + '/' + DEFAULT_RESOURCE_PATTERN;
        try {
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    Class<?> clazz;
                    try {
                        clazz = Class.forName(className);
                        set.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    /**
     * description: 解析包名
     * version: 1.0
     * date: 2021/3/7 17:31
     * author: Silwings
     *
     * @param basePackage 需要解析的路径
     * @return java.lang.String 解析后的路径
     */
    private String resolveBasePackage(String basePackage) {
        // 将类名转换为资源路径
        return ClassUtils.convertClassNameToResourcePath(
                // 解析占位符
                this.applicationContext.getEnvironment().resolveRequiredPlaceholders(basePackage));
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // 该方法空实现即可,用不到
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
