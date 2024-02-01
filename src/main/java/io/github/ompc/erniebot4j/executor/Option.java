package io.github.ompc.erniebot4j.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Option {

    private final Map<String, Object> map = new HashMap<>();

    public Option copy() {
        final var copy = new Option();
        copy.map.putAll(map);
        return copy;
    }

    public <T, R> Option option(Opt<T, R> opt, T value) {
        map.put(opt.name(), opt.convert(value));
        return this;
    }

    public <T, R> Option remove(Opt<T, R> opt) {
        map.remove(opt.name());
        return this;
    }

    public Map<String, Object> dump() {
        return map;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public interface Opt<T, R> {

        String name();

        Class<R> type();

        R convert(T value);

    }

    public record SimpleOpt<T>(String name, Class<T> type) implements Opt<T, T> {

        @Override
        public T convert(T value) {
            return value;
        }

    }

    public record StdOpt<T, R>(String name, Class<R> type, Function<T, R> convert) implements Opt<T, R> {

        @Override
        public R convert(T value) {
            return convert.apply(value);
        }

    }

}
