module erniebot4j {

    requires org.slf4j;
    requires java.net.http;
    requires transitive java.desktop;

    requires transitive com.fasterxml.jackson.databind;
    requires transitive com.fasterxml.jackson.annotation;
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.module.jsonSchema;

    opens io.github.ompc.erniebot4j.chat.message to com.fasterxml.jackson.databind;

    exports io.github.ompc.erniebot4j;
    exports io.github.ompc.erniebot4j.executor;
    exports io.github.ompc.erniebot4j.chat;
    exports io.github.ompc.erniebot4j.chat.message;
    exports io.github.ompc.erniebot4j.chat.function;
    exports io.github.ompc.erniebot4j.image.caption;
    exports io.github.ompc.erniebot4j.image.generation;

}