package io.github.oldmanpushcart.qianfan4j.chat.function;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import io.github.oldmanpushcart.internal.qianfan4j.util.StringUtils;
import io.github.oldmanpushcart.qianfan4j.chat.FunctionCall;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class ChatFunctionKit implements Iterable<ChatFunctionKit.Stub> {

    private static final ObjectMapper mapper = JacksonUtils.mapper();

    private final Map<String, Stub> stubMap;

    /**
     * 构造函数库
     */
    public ChatFunctionKit() {
        this.stubMap = new ConcurrentHashMap<>();
    }

    /**
     * 构造函数库(内部使用)，用于{@link #sub(String...)}
     *
     * @param stubMap 函数存根集合
     */
    private ChatFunctionKit(Map<String, Stub> stubMap) {
        this.stubMap = stubMap;
    }

    /**
     * 加载函数库
     *
     * @param other 函数库
     * @return this
     */
    public ChatFunctionKit load(ChatFunctionKit other) {
        other.stubMap.values().forEach(this::put);
        return this;
    }

    /**
     * 克隆函数库
     *
     * @return 函数库副本
     */
    public ChatFunctionKit copy() {
        final var copy = new ChatFunctionKit();
        copy.load(this);
        return copy;
    }

    /**
     * 加载函数
     *
     * @param functions 函数
     * @return this
     */
    public ChatFunctionKit load(ChatFunction<?, ?>... functions) {

        Objects.requireNonNull(functions);

        // 生成存根
        Arrays.stream(functions).forEach(function -> {
            final var functionClass = function.getClass();
            final var stub = new Stub(
                    Meta.of(functionClass),
                    function
            );
            put(stub);
        });
        return this;
    }

    // 注册到函数库
    private void put(Stub stub) {
        final var existed = stubMap.putIfAbsent(stub.meta().name(), stub);
        if (Objects.nonNull(existed)) {
            throw new IllegalArgumentException("duplicate function name: %s, existed: %s".formatted(
                    stub.meta().name(),
                    existed.function.getClass().getName()
            ));
        }
    }

    /**
     * 获取函数库大小
     *
     * @return 函数库中函数数量
     */
    public int size() {
        return stubMap.size();
    }

    /**
     * 判断函数库是否为空
     *
     * @return TRUE | FALSE
     */
    public boolean isEmpty() {
        return stubMap.isEmpty();
    }

    /**
     * 列出函数库中所有函数存根
     *
     * @return 所有函数存根集合
     */
    public Set<Stub> all() {
        return Set.copyOf(stubMap.values());
    }

    /**
     * 按需获取函数库中所有函数存根
     * <p>
     * 如果函数名称不存在，则抛出异常
     * </p>
     *
     * @param names 函数名称
     * @return 函数存根集合
     */
    public Set<Stub> requires(String... names) {
        requireNonNull(names, "function names cannot be null!");
        final var required = new LinkedHashSet<Stub>();
        for (final var name : names) {
            final var stub = stubMap.get(name);
            if (Objects.isNull(stub)) {
                throw new IllegalArgumentException("function not found: %s".formatted(name));
            }
            required.add(stub);
        }
        return required;
    }

    /**
     * 按需获取函数库中指定名称的函数存根
     *
     * @param name 函数名称
     * @return 函数存根
     */
    public Stub require(String name) {
        requireNonNull(name, "function name cannot be null!");
        final var stub = stubMap.get(name);
        if (Objects.isNull(stub)) {
            throw new IllegalArgumentException("function not found: %s".formatted(name));
        }
        return stub;
    }

    /**
     * 搜索函数存根
     * <p>
     * 如果函数名称不存在，则不会出现在搜索结果中
     * </p>
     *
     * @param names 函数名称
     * @return 函数存根集合
     */
    public Set<Stub> search(String... names) {
        requireNonNull(names, "function names cannot be null!");
        final var search = new LinkedHashSet<Stub>();
        for (final var name : names) {
            final var stub = stubMap.get(name);
            if (Objects.nonNull(stub)) {
                search.add(stub);
            }
        }
        return search;
    }

    /**
     * 获取函数存根
     *
     * @param name 函数名称
     * @return 函数存根
     */
    public Stub get(String name) {
        requireNonNull(name, "function name cannot be null!");
        final var stub = stubMap.get(name);
        return Objects.isNull(stub) ? null : stub;
    }

    /**
     * 构建子函数库
     *
     * @param names 函数名称
     * @return 子函数库
     */
    public ChatFunctionKit sub(String... names) {
        requireNonNull(names, "function names cannot be null!");
        final var subStubs = new ConcurrentHashMap<String, Stub>();
        for (final var name : names) {
            final var stub = stubMap.get(name);
            if (Objects.isNull(stub)) {
                throw new IllegalArgumentException("function not found: %s".formatted(name));
            }
            subStubs.put(name, stub);
        }
        return new ChatFunctionKit(subStubs);
    }

    @Override
    public Iterator<Stub> iterator() {
        final var iterator = stubMap.values().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Stub next() {
                return iterator.next();
            }
        };
    }

    /**
     * 判断函数库是否包含指定名称的函数
     *
     * @param names 函数名称
     * @return TRUE | FALSE
     */
    public boolean contains(String... names) {
        requireNonNull(names, "function names cannot be null!");
        for (final var name : names) {
            if (!stubMap.containsKey(name)) {
                return false;
            }
        }
        return true;
    }


    public record Meta(
            @JsonProperty("name")
            String name,
            @JsonProperty("description")
            String description,
            @JsonProperty("parameters")
            Schema parameter,
            @JsonProperty("responses")
            Schema response,
            @JsonProperty("examples")
            List<Example> examples
    ) {

        public record Schema(Type type) {

            @JsonValue
            JsonNode extract() {
                return JacksonUtils.schema(mapper, type);
            }

        }

        public record Example(String question, FunctionCall call) {

            @JsonValue
            Message[] extract() {
                return new Message[]{
                        Message.ofUser(question),
                        Message.ofFunctionCall(call)
                };
            }

        }

        public static Meta of(Class<?> functionClass) {

            // 检查是否实现ChatFunction接口
            if (!ChatFunction.class.isAssignableFrom(functionClass)) {
                throw new IllegalArgumentException("required implements interface: %s".formatted(
                        ChatFunction.class.getName()
                ));
            }

            // 检查是否有注解
            if (!functionClass.isAnnotationPresent(ChatFn.class)) {
                throw new IllegalArgumentException("required annotation: %s".formatted(
                        ChatFn.class.getName()
                ));
            }

            // 找到ChatFunction接口
            final var interfaceType = Stream.of(functionClass.getGenericInterfaces())
                    .filter(genericInterface -> genericInterface instanceof ParameterizedType)
                    .map(genericInterface -> (ParameterizedType) genericInterface)
                    .filter(pType -> pType.getRawType().equals(ChatFunction.class))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("required implements interface: %s".formatted(
                            ChatFunction.class.getName()
                    )));

            // 注解
            final var anChatFn = functionClass.getAnnotation(ChatFn.class);

            // 函数名
            final var name = StringUtils.isNotBlank(anChatFn.name())
                    ? anChatFn.name()
                    : StringUtils.toSnake(functionClass.getSimpleName());

            // 入参类型
            final var parameterType = interfaceType.getActualTypeArguments()[0];

            // 返回类型
            final var returnType = interfaceType.getActualTypeArguments()[1];

            // 例子
            final var examples = Stream.of(functionClass.getAnnotations())
                    .flatMap(annotation -> {
                        if (annotation instanceof ChatFnExample anExample) {
                            return Stream.of(anExample);
                        } else if (annotation instanceof ChatFnExamples anExamples) {
                            return Stream.of(anExamples.value());
                        }
                        return Stream.empty();
                    })
                    .map(anExample -> anExample.negative()
                            ? new Example(anExample.question(), new FunctionCall("", "{}", "我无需调用任何工具"))
                            : new Example(anExample.question(), new FunctionCall(name, anExample.arguments(), anExample.thoughts()))
                    )
                    .toList();

            // schema
            return new Meta(
                    name,
                    anChatFn.description(),
                    new Schema(parameterType),
                    new Schema(returnType),
                    examples
            );

        }

    }

    /**
     * 功能函数存根
     *
     * @param meta     功能函数元数据
     * @param function 对话函数
     */
    public record Stub(Meta meta, ChatFunction<?, ?> function) {

        /**
         * 函数调用
         *
         * @param argumentsJson 入参
         * @return 返回值
         */
        public CompletableFuture<String> call(String argumentsJson) {
            try {
                return function.call(JacksonUtils.toObject(mapper, meta.parameter().type(), argumentsJson))
                        .thenApply(result -> JacksonUtils.toJson(mapper, result));
            } catch (Throwable cause) {
                throw new RuntimeException("function: %s call error!".formatted(meta.name()), cause);
            }
        }

    }


}
