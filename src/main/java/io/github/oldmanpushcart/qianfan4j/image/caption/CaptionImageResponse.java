package io.github.oldmanpushcart.qianfan4j.image.caption;

import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;
import io.github.oldmanpushcart.qianfan4j.chat.ChatOptions;
import io.github.oldmanpushcart.qianfan4j.chat.ChatRequest;
import io.github.oldmanpushcart.qianfan4j.chat.NotSafeChatResponseException;
import io.github.oldmanpushcart.qianfan4j.util.Aggregator;

/**
 * 图生文应答
 */
public interface CaptionImageResponse extends AlgoResponse, Aggregator<CaptionImageResponse> {

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

}
