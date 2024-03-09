package io.github.oldmanpushcart.qianfan4j.chat;

import io.github.oldmanpushcart.internal.qianfan4j.chat.ChatRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;
import io.github.oldmanpushcart.qianfan4j.chat.function.ChatFunction;
import io.github.oldmanpushcart.qianfan4j.chat.message.Message;

import java.util.List;

/**
 * 对话请求
 */
public interface ChatRequest extends AlgoRequest<ChatModel, ChatResponse> {

    /**
     * 获取对话消息列表
     *
     * @return 对话消息列表
     */
    List<Message> messages();

    /**
     * 获取对话函数列表
     *
     * @return 对话函数列表
     */
    List<ChatFunction<?, ?>> functions();

    /**
     * 对话请求构造器
     *
     * @return 构造器
     */
    static Builder newBuilder() {
        return new ChatRequestBuilderImpl();
    }

    /**
     * 对话请求构造器
     *
     * @param request 对话请求
     * @return 构造器
     */
    static Builder newBuilder(ChatRequest request) {
        return new ChatRequestBuilderImpl(request);
    }

    /**
     * 对话请求构造器
     */
    interface Builder extends AlgoRequest.Builder<ChatModel, ChatRequest, Builder> {

        /**
         * 添加对话消息列表
         *
         * @param messages 对话消息列表
         * @return this
         */
        Builder messages(Message... messages);

        /**
         * 添加对话函数列表
         *
         * @param functions 对话函数列表
         * @return this
         */
        default Builder functions(ChatFunction<?, ?>... functions) {
            return functions(false, functions);
        }

        /**
         * 设置对话函数列表
         *
         * @param isReplace 是否替换
         * @param functions 对话函数列表
         * @return this
         */
        Builder functions(boolean isReplace, ChatFunction<?, ?>... functions);

    }

}
