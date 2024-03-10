open module qianfan4j {

    requires org.slf4j;
    requires java.net.http;
    requires transitive java.desktop;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.module.jsonSchema;

    exports io.github.oldmanpushcart.qianfan4j;
    exports io.github.oldmanpushcart.qianfan4j.base.api;
    exports io.github.oldmanpushcart.qianfan4j.base.algo;
    exports io.github.oldmanpushcart.qianfan4j.chat.message;
    exports io.github.oldmanpushcart.qianfan4j.chat.function;
    exports io.github.oldmanpushcart.qianfan4j.chat;
    exports io.github.oldmanpushcart.qianfan4j.completion;
    exports io.github.oldmanpushcart.qianfan4j.image.caption;
    exports io.github.oldmanpushcart.qianfan4j.image.generation;

}