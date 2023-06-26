package com.zxb.liqi.enums;

/**
 * @author Mr.M
 * @date 2023/6/14
 * @Description 条件运算符
 */
public enum ConditionalOperator {
    /**
     * lt：less than 小于
     * le：less than or equal to 小于等于
     * eq：equal to 等于
     * ne：not equal to 不等于
     * ge：greater than or equal to 大于等于
     * gt：greater than 大于
     */
    EQ("="),
    LT("<"),
    LE("<="),
    NE("!="),
    GE(">="),
    GT(">");

    private final String conditional;

    ConditionalOperator(String conditional) {
        this.conditional = conditional;
    }

    public String getConditional() {
        return conditional;
    }
}
