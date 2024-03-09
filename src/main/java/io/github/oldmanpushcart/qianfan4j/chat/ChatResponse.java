package io.github.oldmanpushcart.qianfan4j.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;
import io.github.oldmanpushcart.qianfan4j.util.Aggregator;

/**
 * 对话响应
 */
public interface ChatResponse extends AlgoResponse, Aggregator<ChatResponse> {

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
     * 是否函数调用
     *
     * @return TRUE | FALSE
     */
    boolean isFunctionCall();

    /**
     * 获取应答内容
     *
     * @return 应答内容
     */
    String content();

    /**
     * 获取搜索结果
     *
     * @return 搜索结果
     */
    Search search();

    /**
     * 获取函数调用
     * <p>当{@link #isFunctionCall()}=={@code true}时不为{@code null}</p>
     *
     * @return 函数调用
     */
    FunctionCall call();

    /**
     * 应答格式
     */
    enum Format {

        /**
         * 文本格式（默认）
         */
        @JsonProperty("text")
        TEXT,

        /**
         * JSON格式
         */
        @JsonProperty("json_object")
        JSON

    }

}
