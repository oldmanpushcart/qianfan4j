module qianfan4j {

    requires org.slf4j;
    requires java.net.http;
    requires transitive java.desktop;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.module.jsonSchema;

    opens io.github.oldmanpushcart.internal.qianfan4j.chat to com.fasterxml.jackson.databind;
    opens io.github.oldmanpushcart.internal.qianfan4j.base.api to com.fasterxml.jackson.databind;
    opens io.github.oldmanpushcart.internal.qianfan4j.base.algo to com.fasterxml.jackson.databind;

    opens io.github.oldmanpushcart.qianfan4j.base.api to com.fasterxml.jackson.databind;
    opens io.github.oldmanpushcart.qianfan4j.base.algo to com.fasterxml.jackson.databind;
    opens io.github.oldmanpushcart.qianfan4j.chat to com.fasterxml.jackson.databind;
    opens io.github.oldmanpushcart.qianfan4j.chat.message to com.fasterxml.jackson.databind;
    opens io.github.oldmanpushcart.qianfan4j.chat.function to com.fasterxml.jackson.databind;

    exports io.github.oldmanpushcart.qianfan4j;
    exports io.github.oldmanpushcart.qianfan4j.chat.message;
    exports io.github.oldmanpushcart.qianfan4j.chat.function;
    exports io.github.oldmanpushcart.qianfan4j.chat;
    exports io.github.oldmanpushcart.qianfan4j.base.algo;
    exports io.github.oldmanpushcart.qianfan4j.base.api;


}