package io.github.ompc.erniebot4j.chat.function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChatFnSchemaResource {

    String parameters() default "";

    String responses() default "";

}
