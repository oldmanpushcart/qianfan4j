package io.github.ompc.erniebot4j.chat.function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 函数
 * <p>
 * 用于标记函数相关信息
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
     * schema资源文件路径
     *
     * @return schema资源文件路径
     */
    String resource() default "";

    /**
     * 是否忽略返回值的schema
     *
     * @return 是否忽略返回值的schema
     */
    boolean isIgnoreResponseSchema() default false;

}
