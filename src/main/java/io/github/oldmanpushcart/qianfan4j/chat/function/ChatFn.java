package io.github.oldmanpushcart.qianfan4j.chat.function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对话函数注解
 * <p>用于标记函数相关信息</p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatFn {

    /**
     * 函数名称
     *
     * @return 函数名称
     */
    String name();

    /**
     * 函数描述
     *
     * @return 函数描述
     */
    String description() default "";

    /**
     * 函数例子
     *
     * @return 函数例子
     */
    Example[] examples() default {};

    /**
     * 函数调用例子
     */
    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Example {

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
         * 思考过程
         *
         * @return 思考过程
         */
        String thoughts() default "";

        /**
         * 请求参数(JSON格式)
         * <p>如果{@link #negative}为true, 则忽略该参数</p>
         *
         * @return 请求参数
         */
        String arguments() default "{}";

    }

}
