package io.github.oldmanpushcart.qianfan4j.util;

import static java.util.Optional.ofNullable;

/**
 * 可聚合的
 *
 * @param <T> 聚合后的类型
 */
public interface Aggregatable<T> {

    /**
     * 聚合
     *
     * @param other 其他
     * @return 聚合后的类型
     */
    T aggregate(T other);

    /**
     * 累加
     *
     * @param left  左
     * @param right 右
     * @param <T>   累加后的类型
     * @return 累加结果
     */
    static <T extends Aggregatable<T>> T accumulate(T left, T right) {
        return ofNullable(left).map(v -> v.aggregate(right)).orElse(right);
    }

}