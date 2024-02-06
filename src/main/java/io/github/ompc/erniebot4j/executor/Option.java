package io.github.ompc.erniebot4j.executor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 选项
 */
public class Option {

    private final Map<String, Object> map = new HashMap<>();

    /**
     * 克隆
     *
     * @return 选项副本
     */
    public Option copy() {
        final var copy = new Option();
        copy.map.putAll(map);
        return copy;
    }

    /**
     * 加载选项
     *
     * @param other 其他选项
     * @return this
     */
    public Option load(Option other) {
        map.putAll(other.map);
        return this;
    }

    /**
     * 设置选项
     *
     * @param opt   选项
     * @param value 值
     * @param <T>   值类型
     * @param <R>   转换后的值类型
     * @return this
     */
    public <T, R> Option option(Opt<T, R> opt, T value) {
        map.put(opt.name(), opt.convert(value));
        return this;
    }

    /**
     * 删除选项
     *
     * @param opt 选项
     * @param <T> 值类型
     * @param <R> 转换后的值类型
     * @return this
     */
    public <T, R> Option remove(Opt<T, R> opt) {
        map.remove(opt.name());
        return this;
    }

    /**
     * 设置选项
     *
     * @param name  选项名称
     * @param value 选项值
     * @return this
     */
    public Option option(String name, Object value) {
        map.put(name, value);
        return this;
    }

    /**
     * 删除选项
     *
     * @param name 选项名称
     * @return this
     */
    public Option remove(String name) {
        map.remove(name);
        return this;
    }

    /**
     * 导出选项为KV集合
     *
     * @return 选项KV集合
     */
    public Map<String, Object> export() {
        return Collections.unmodifiableMap(map);
    }

    /**
     * 选项是否为空
     *
     * @return TRUE | FALSE
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * 选项项目
     *
     * @param <T> 值类型
     * @param <R> 转换后的值类型
     */
    public interface Opt<T, R> {

        /**
         * 选项名称
         *
         * @return 选项名称
         */
        String name();

        /**
         * 选项类型
         *
         * @return 选项类型
         */
        Class<R> type();

        /**
         * 转换
         *
         * @param value 值
         * @return 转换后的值
         */
        R convert(T value);

    }

    /**
     * 标准项目
     *
     * @param name    选项名称
     * @param type    选项类型
     * @param convert 转换函数
     * @param <T>     值类型
     * @param <R>     转换后的值类型
     */
    public record StdOpt<T, R>(String name, Class<R> type, Function<T, R> convert) implements Opt<T, R> {

        @Override
        public R convert(T value) {
            return convert.apply(value);
        }

    }

    /**
     * 简单项目
     *
     * @param name    选项名称
     * @param type    选项类型
     * @param convert 转换函数
     * @param <T>     值类型
     */
    public record SimpleOpt<T>(String name, Class<T> type, Function<T, T> convert) implements Opt<T, T> {

        /**
         * 简单项目
         *
         * @param name 选项名称
         * @param type 选项类型
         */
        public SimpleOpt(String name, Class<T> type) {
            this(name, type, Function.identity());
        }

        @Override
        public T convert(T value) {
            return convert.apply(value);
        }

    }

}
