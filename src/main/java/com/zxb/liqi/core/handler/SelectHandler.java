package com.zxb.liqi.core.handler;

import com.zxb.liqi.exception.GetBeanInfoErrorException;
import com.zxb.liqi.exception.NoFoundEntityFieldException;
import com.zxb.liqi.exception.ObjGetMethodInvokeErrorException;
import com.zxb.liqi.interfaces.BaseHandler;
import com.zxb.liqi.interfaces.BaseParser;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Mr.M
 * @date 2023/6/16
 * @Description
 */
public class SelectHandler implements BaseHandler {
    private final BaseParser parser;
    private final static String SPLIT_SUFFIX = "\\.";
    private final static String REGEX = "#\\{( *\\b\\w+.?\\w+\\b *)}";
    private Map<String, Object> realParamData;
    private String prepareSql;

    public SelectHandler(BaseParser parser) {
        this.parser = parser;
        this.handle();
    }

    private String preparedStatementSql(String sql) {
        Map<String, Object> paramMap = this.parser.getParamMap();
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(sql);

        realParamData = new LinkedHashMap<>();

        while (matcher.find()) {
            String matcherStr = matcher.group(1).trim();

            String paramKey = null;
            if (matcherStr.contains(".")) {
                paramKey = matcherStr.split(SPLIT_SUFFIX)[1];
                matcherStr = matcherStr.split(SPLIT_SUFFIX)[0];
            }
            if (paramMap.containsKey(matcherStr)) {
                String paramName = Objects.isNull(paramKey) ? matcherStr : matcherStr + "." + paramKey;
                Object obj;
                if (Objects.isNull(paramKey)) {
                    obj = paramMap.get(matcherStr);
                    if (obj instanceof List && !((List<?>) obj).isEmpty()) {
                        if (((List<?>) obj).get(0) instanceof String) {
                            String inSql = ((List<?>) obj).stream().map(item -> "'" + item + "'")
                                    .collect(Collectors.joining(","));
                            sql = sql.replace("#{" + matcherStr + "}", inSql);
                            continue;
                        } else {
                            throw new RuntimeException("泛型非实体类的List，请传入基础类型的封包类");
                        }
                    }
                } else {
                    obj = paramMap.get(matcherStr);
                    if (obj instanceof List && !((List<?>) obj).isEmpty()) {
                        final String finalParamKey = paramKey;
                        String inSql = ((List<?>) obj).stream().map(x -> "'" + getParamValue(x, finalParamKey) + "'")
                                .collect(Collectors.joining(","));
                        sql = sql.replace("#{" + paramName + "}", inSql);
                        continue;
                    } else {
                        obj = getParamValue(obj, paramKey);
                    }
                }
                if (realParamData.containsKey(paramName)) {
                    throw new RuntimeException("不能存在相同paramKey");
                }
                sql = sql.replace("#{" + paramName + "}", "?");
                realParamData.put(paramName, obj);
            }
        }
        return sql;
    }

    private Object getParamValue(Object obj, String objPropertyName) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor pd = null;
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                if (propertyDescriptor.getName().equals(objPropertyName)) {
                    pd = propertyDescriptor;
                    break;
                }
            }
            // 获取 objPropertyName 对应属性值
            if (Objects.nonNull(pd)) {
                return pd.getReadMethod().invoke(obj);
            }
            throw new NoFoundEntityFieldException("Not found in the current entity class " + objPropertyName + " field");
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw new GetBeanInfoErrorException("Get the current " + obj.getClass() + " class beanInfo object failed");
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new ObjGetMethodInvokeErrorException("The current object " + objPropertyName + " property getter method invoke failure");
        }
    }

    @Override
    public void handle() {
        this.prepareSql = this.preparedStatementSql(this.parser.getParseSql());
    }

    @Override
    public Map<String, Object> getRealDataMap() {
        return this.realParamData;
    }

    @Override
    public String getPrepareSql() {
        return this.prepareSql;
    }

    @Override
    public BaseParser getBaseParser() {
        return this.parser;
    }

    @Override
    public int getSqlCount() {
        return 1;
    }
}
