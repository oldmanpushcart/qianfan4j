package io.github.oldmanpushcart.qianfan4j.chat.function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 函数调用例子集合
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatFnExamples {

    /**
     * 函数调用例子集合
     *
     * @return 例子集合
     */
    ChatFnExample[] value();
}
