package io.github.ompc.erniebot4j.chat.function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能函数
 * <p>
 * 用于标记功能函数相关信息
 * </p>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatFn {

    /**
     * 函数名称
     *
     * @return 函数名称
     */
    String name() default "";

    /**
     * 函数描述
     *
     * @return 函数描述
     */
    String description() default "";

    /**
     * 是否忽略返回值的schema
     *
     * @return 是否忽略返回值的schema
     */
    boolean isIgnoreResponseSchema() default false;

}
