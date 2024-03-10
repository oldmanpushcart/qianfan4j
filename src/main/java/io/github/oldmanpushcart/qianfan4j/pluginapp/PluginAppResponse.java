package io.github.oldmanpushcart.qianfan4j.pluginapp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;
import io.github.oldmanpushcart.qianfan4j.chat.ChatOptions;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.NotSafeChatResponseException;
import io.github.oldmanpushcart.qianfan4j.util.Aggregator;

import java.io.IOException;

/**
 * 插件应用应答
 */
public interface PluginAppResponse extends AlgoResponse, Aggregator<PluginAppResponse> {

    /**
     * 是否最后一条对话
     * <p>当{@link ChatRequest#option()}设置了{@link ChatOptions#IS_STREAM}={@code true}的时候有意义</p>
     *
     * @return TRUE | FALSE
     */
    boolean isLast();

    /**
     * 是否安全
     * <p>当应答不安全的时候通常意味着你的对话违反了千帆的管理协定。请求将会抛出{@link NotSafeChatResponseException}</p>
     *
     * @return TRUE | FALSE
     */
    boolean isSafe();

    /**
     * 获取应答内容
     *
     * @return 应答内容
     */
    String content();

    /**
     * 获取插件请求原始信息
     *
     * @return 插件请求原始信息
     */
    Meta meta();

    /**
     * 插件应答元数据
     * <p>
     * 在这里可以查看插件调用的相关信息
     * </p>
     *
     * @param pluginId 插件ID
     * @param raw      原始请求和响应
     */
    record Meta(String pluginId, Raw raw) {

        @JsonCreator
        static Meta of(

                @JsonProperty("plugin_id")
                String pluginId,

                @JsonDeserialize(using = RawStringJsonDeserializer.class)
                @JsonProperty("request")
                String rawRequest,

                @JsonDeserialize(using = RawStringJsonDeserializer.class)
                @JsonProperty("response")
                String rawResponse

        ) {
            return new Meta(pluginId, new Raw(rawRequest, rawResponse));
        }

        private static class RawStringJsonDeserializer extends JsonDeserializer<String> {

            @Override
            public String deserialize(JsonParser parser, DeserializationContext context) throws IOException {
                final var node = context.readTree(parser);
                return node.toString();
            }

        }

    }

    /**
     * 插件原始请求和响应
     *
     * @param request  原始请求
     * @param response 原始响应
     */
    record Raw(String request, String response) {

    }

}
