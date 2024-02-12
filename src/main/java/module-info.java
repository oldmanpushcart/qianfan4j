module erniebot4j {

    requires org.slf4j;
    requires java.net.http;
    requires transitive java.desktop;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.module.jsonSchema;

    opens io.github.ompc.erniebot4j.chat.message to com.fasterxml.jackson.databind;

    exports io.github.ompc.erniebot4j;
    exports io.github.ompc.erniebot4j.exception;
    exports io.github.ompc.erniebot4j.executor;
    exports io.github.ompc.erniebot4j.chat;
    exports io.github.ompc.erniebot4j.chat.message;
    exports io.github.ompc.erniebot4j.chat.function;
    exports io.github.ompc.erniebot4j.completion;
    exports io.github.ompc.erniebot4j.embedding;
    exports io.github.ompc.erniebot4j.image.caption;
    exports io.github.ompc.erniebot4j.image.generation;
    exports io.github.ompc.erniebot4j.pluginapp;

}