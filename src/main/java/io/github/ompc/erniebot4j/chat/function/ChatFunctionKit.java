package io.github.ompc.erniebot4j.chat.function;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.ompc.erniebot4j.util.JacksonUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;
import static io.github.ompc.erniebot4j.util.JacksonUtils.compact;
import static io.github.ompc.erniebot4j.util.StringUtils.isNotBlank;
import static java.util.Objects.requireNonNull;

/**
 * 功能函数库
 */
public class ChatFunctionKit implements Iterable<ChatFunctionKit.Stub> {

    private static final ObjectMapper mapper = JacksonUtils.mapper();

    private final Map<String, Stub> stubMap;

    /**
     * 构造功能函数库
     */
    public ChatFunctionKit() {
        this.stubMap = new ConcurrentHashMap<>();
    }

    /**
     * 构造功能函数库(内部使用)，用于{@link #sub(String...)}
     *
     * @param stubMap 函数存根集合
     */
    private ChatFunctionKit(Map<String, Stub> stubMap) {
        this.stubMap = stubMap;
    }

    public void load(ChatFunctionKit kit) {
        kit.stubMap.values().forEach(this::put);
    }

    public ChatFunctionKit copy() {
        final var copy = new ChatFunctionKit();
        copy.load(this);
        return copy;
    }

    /**
     * 加载函数
     *
     * @param function 函数
     * @return this
     */
    public ChatFunctionKit load(ChatFunction<?, ?> function) {

        // 生成存根
        final var functionClass = function.getClass();
        final var stub = new Stub(
                Meta.make(functionClass),
                function
        );

        put(stub);
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
     * 获取函数存根的流
     *
     * @return 函数存根流
     */
    public Stream<Stub> stream() {
        return StreamSupport.stream(spliterator(), false);
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


    /**
     * 功能函数元数据
     *
     * @param schema        schema
     * @param name          函数名称
     * @param description   函数描述
     * @param method        函数方法
     * @param parameterType 参数类型
     * @param returnType    返回类型
     */
    public record Meta(JsonNode schema, String name, String description, Method method, Type parameterType,
                       Type returnType) {

        /**
         * 生成函数元数据
         *
         * @param functionClass 函数类型
         * @return 函数元数据
         */
        public static Meta make(Class<?> functionClass) {

            // 检查是否是BotFunction的实现类
            check(functionClass, ChatFunction.class.isAssignableFrom(functionClass),
                    "required implements interface: %s".formatted(
                            ChatFunction.class.getName()
                    )
            );

            // 检查是否有BotFn注解
            check(functionClass, functionClass.isAnnotationPresent(ChatFn.class),
                    "required annotation: %s".formatted(
                            ChatFn.class.getName()
                    )
            );

            // BotFn注解
            final var anBotFn = functionClass.getAnnotation(ChatFn.class);

            // 函数名称
            final var name = Objects.isNull(anBotFn.name()) || anBotFn.name().isBlank()
                    ? functionClass.getSimpleName()
                    : anBotFn.name();

            // 函数描述
            final var description = anBotFn.description();

            // 找到BotFunction接口
            final var botFunctionInterface = Stream.of(functionClass.getGenericInterfaces())
                    .filter(genericInterface -> genericInterface instanceof ParameterizedType)
                    .map(genericInterface -> (ParameterizedType) genericInterface)
                    .filter(pType -> pType.getRawType().equals(ChatFunction.class))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("required implements interface: %s".formatted(
                            ChatFunction.class.getName()
                    )));

            final var parameterType = botFunctionInterface.getActualTypeArguments()[0];
            final var returnType = botFunctionInterface.getActualTypeArguments()[1];

            // 找到BotFunction接口的apply方法
            final var applyMethod = Stream.of(functionClass.getMethods())
                    .filter(method -> method.getName().equals("call"))
                    .filter(method -> method.getParameterCount() == 1)
                    .filter(method -> method.getParameterTypes()[0].equals(parameterType))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("required method: %s".formatted(
                            "CompletableFuture<R> call(T t)"
                    )));

            // 封装schema
            final var schema = new ObjectMapper().createObjectNode();
            schema.put("name", name);
            schema.put("description", description);

            setupSchemaParameters(schema, parameterType);
            setupSchemaResponses(schema, anBotFn, returnType);
            setupSchemaExamples(schema, name, functionClass);

            // 返回schema
            return new Meta(schema, name, description, applyMethod, parameterType, returnType);
        }

        /**
         * 添加参数的schema
         *
         * @param schema        schema
         * @param parameterType 函数参数类型
         */
        private static void setupSchemaParameters(ObjectNode schema, Type parameterType) {
            schema.set("parameters", JacksonUtils.schema(mapper, parameterType));
        }

        /**
         * 添加返回值的schema
         *
         * @param schema     schema
         * @param anChatFn   BotFn注解
         * @param returnType 函数返回类型
         */
        private static void setupSchemaResponses(ObjectNode schema, ChatFn anChatFn, Type returnType) {
            // 如果忽略返回值的schema，则不添加返回值的schema
            if (!anChatFn.isIgnoreReturnSchema()) {
                schema.set("responses", JacksonUtils.schema(mapper, returnType));
            }
        }

        // 列出函数上注解的所有例子
        private static Set<ChatFnExample> listBotFnExampleSet(Class<?> functionClass) {
            final var examples = new LinkedHashSet<ChatFnExample>();
            if (functionClass.isAnnotationPresent(ChatFnExample.class)) {
                examples.add(functionClass.getAnnotation(ChatFnExample.class));
            }
            if (functionClass.isAnnotationPresent(ChatFnExamples.class)) {
                examples.addAll(Set.of(functionClass.getAnnotation(ChatFnExamples.class).value()));
            }
            return examples;
        }

        /**
         * 添加例子的schema
         *
         * @param schema        schema
         * @param name          函数名称
         * @param functionClass 函数类型
         */
        private static void setupSchemaExamples(ObjectNode schema, String name, Class<?> functionClass) {
            final var examples = new ObjectMapper().createArrayNode();
            listBotFnExampleSet(functionClass).stream()
                    .map(anEx -> {

                        final var example = new ObjectMapper().createArrayNode();

                        // 提问必须存在
                        check(anEx.question(), isNotBlank(anEx.question()), "question is required!");

                        // 添加正向例子
                        if (anEx.negative()) {
                            example.add(new ObjectMapper().createObjectNode()
                                    .put("role", "user")
                                    .put("content", anEx.question())
                            );
                            example.add(new ObjectMapper().createObjectNode()
                                    .put("role", "assistant")
                                    .putNull("content")
                                    .set("function_call", new ObjectMapper().createObjectNode()
                                            .put("name", "")
                                            .put("thoughts", "我无需调用任何工具")
                                            .put("arguments", "{}")
                                    )
                            );
                        }

                        // 添加负向例子
                        else {
                            example.add(new ObjectMapper().createObjectNode()
                                    .put("role", "user")
                                    .put("content", anEx.question())
                            );
                            example.add(new ObjectMapper().createObjectNode()
                                    .put("role", "assistant")
                                    .putNull("content")
                                    .set("function_call", new ObjectMapper().createObjectNode()
                                            .put("name", name)
                                            .put("thoughts", anEx.thoughts())
                                            .put("arguments", isNotBlank(anEx.arguments())
                                                    ? compact(mapper, anEx.arguments())
                                                    : "{}"
                                            )
                                    )
                            );
                        }


                        return example;
                    })
                    .forEach(examples::add);

            if (!examples.isEmpty()) {
                schema.set("examples", examples);
            }

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
                return _call(meta.method(), JacksonUtils.toObject(mapper, meta.parameterType(), argumentsJson))
                        .thenApply(result -> JacksonUtils.toJson(mapper, result));
            } catch (Throwable cause) {
                throw new RuntimeException(
                        "function: %s call error!".formatted(meta.name()),
                        cause
                );
            }
        }

        @SuppressWarnings("unchecked")
        private CompletableFuture<Object> _call(Method method, Object arguments) throws InvocationTargetException, IllegalAccessException {
            return (CompletableFuture<Object>) method.invoke(function, arguments);
        }

    }

}
