package com.zxb.liqi.enums;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description sql key word
 */
public enum SqlKeyWordEnum {
    /**
     * sql key word
     */
    ORDER_BY_DESC("ORDER BY DESC "),
    ORDER_BY_ASC("ORDER BY ASC "),
    IN("IN "),
    LIKE("LIKE "),
    GROUP_BY("GROUP BY "),
    HAVING("HAVING ");
    private final String sqlKeyWord;

    SqlKeyWordEnum(String sqlKeyWord) {
        this.sqlKeyWord = sqlKeyWord;
    }

    public String getSqlKeyWord() {
        return sqlKeyWord;
    }
}
