package io.github.ompc.erniebot4j.executor;

import static java.util.Optional.ofNullable;

/**
 * 可合并的
 *
 * @param <T> 合并后的类型
 */
public interface Mergeable<T> {

    /**
     * 合并
     *
     * @param other 其他
     * @return 合并后的类型
     */
    T merge(T other);

    /**
     * 聚合
     *
     * @param left  左
     * @param right 右
     * @param <T>   聚合后的类型
     * @return 聚合结果
     */
    static <T extends Mergeable<T>> T aggregate(T left, T right) {
        return ofNullable(left).map(v -> v.merge(right)).orElse(right);
    }

}
