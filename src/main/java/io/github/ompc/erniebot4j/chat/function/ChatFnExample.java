package io.github.ompc.erniebot4j.chat.function;

import java.lang.annotation.*;

/**
 * 函数调用例子
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ChatFnExamples.class)
public @interface ChatFnExample {

    /**
     * 是否负向例子
     */
    boolean negative() default false;

    /**
     * 触发问题
     *
     * @return 触发问题
     */
    String question();

    /**
     * 请求参数(JSON格式)
     * <p>
     * 如果{@link #negative}为true, 则忽略该参数
     * </p>
     *
     * @return 请求参数
     */
    String arguments() default "{}";

}
