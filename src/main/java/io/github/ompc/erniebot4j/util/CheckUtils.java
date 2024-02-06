package io.github.ompc.erniebot4j.util;

import java.util.function.Predicate;

/**
 * 检查工具类
 */
public class CheckUtils {

    /**
     * 检查参数是否合法
     *
     * @param value     参数值
     * @param condition 条件
     * @param message   异常信息
     * @param <T>       参数类型
     * @return 参数值
     */
    public static <T> T check(T value, boolean condition, String message) {
        if (condition) {
            return value;
        }
        throw new IllegalArgumentException(message);
    }

    /**
     * 检查参数是否合法
     *
     * @param value     参数值
     * @param predicate 条件
     * @param message   异常信息
     * @param <T>       参数类型
     * @return 参数值
     */
    public static <T> T check(T value, Predicate<T> predicate, String message) {
        return check(value, predicate.test(value), message);
    }

}
