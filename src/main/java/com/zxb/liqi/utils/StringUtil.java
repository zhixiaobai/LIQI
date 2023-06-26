package com.zxb.liqi.utils;

import com.zxb.liqi.enums.ConditionalOperator;
import com.zxb.liqi.enums.SqlKeyWordEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description
 */
public class StringUtil {
    public static String substringAfter(String source, String prefix) {
        return source.replaceFirst(prefix, "");
    }

    public static String uncapFirst(String source) {
        return source.substring(0, 1).toLowerCase() + source.substring(1);
    }

    public static String spliceCondition(String columnName, String fieldName, ConditionalOperator conditionalOperator) {
        return fieldName + " " + conditionalOperator.getConditional() + " #{" + columnName + "}";
    }

    public static String spliceCondition(String columnName, String fieldName, SqlKeyWordEnum sqlKeyWordEnum) {
        return fieldName + " " + sqlKeyWordEnum.getSqlKeyWord() + "#{" + columnName + "}";
    }

    public static String spliceConditionIn(String columnName, List<?> list, SqlKeyWordEnum sqlKeyWordEnum) {
        List<String> strings = new ArrayList<>();
        list.forEach(s->{strings.add("'" + s + "'");});
        return columnName + " " + sqlKeyWordEnum.getSqlKeyWord() + "(" + String.join(",", strings) + ")";
    }
}
