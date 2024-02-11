package io.github.ompc.erniebot4j.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 懒加载
 *
 * @param <V> 值类型
 */
public class LazyGet<V> {

    private final AtomicReference<V> ref = new AtomicReference<>();
    private final Supplier<V> supplier;

    public LazyGet(Supplier<V> supplier) {
        this.supplier = supplier;
    }

    public V get() {
        final var v = ref.get();
        if (null != v) {
            return v;
        }
        synchronized (this) {
            final var existed = ref.get();
            if (null != existed) {
                return existed;
            }
            final var value = supplier.get();
            ref.set(value);
            return value;
        }
    }

    /**
     * 创建懒加载
     *
     * @param supplier 获取值
     * @param <T>      值类型
     * @return 懒加载
     */
    public static <T> LazyGet<T> of(Supplier<T> supplier) {
        return new LazyGet<>(supplier);
    }

}
