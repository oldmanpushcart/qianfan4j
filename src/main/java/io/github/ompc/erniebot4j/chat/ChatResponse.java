package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.executor.*;

import java.util.List;
import java.util.Optional;

/**
 * 对话响应
 *
 * @param id        标识
 * @param type      类型
 * @param timestamp 时间戳
 * @param usage     用量
 * @param sentence  句子
 * @param call      函数调用
 * @param search    搜索结果
 */
public record ChatResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        Sentence sentence,
        FunctionCall call,
        Search search

) implements Response, Mergeable<ChatResponse> {

    /**
     * 是否是函数调用
     *
     * @return TRUE | FALSE
     */
    public boolean isFunctionCall() {
        return null != call;
    }

    @Override
    public ChatResponse merge(ChatResponse response) {
        return Optional.ofNullable(response)
                .map(other -> new ChatResponse(
                        this.id(),
                        this.type(),
                        this.timestamp(),
                        other.usage(),
                        new Sentence(
                                this.sentence().index(),
                                this.sentence().isLast() || other.sentence().isLast(),
                                this.sentence().content() + other.sentence().content()
                        ),
                        other.call(),
                        other.search()
                ))
                .orElse(this);
    }

    /**
     * 应答格式
     */
    public enum Format implements Textualizable {
        JSON("json"),
        TEXT("json_object");

        private final String text;

        Format(String text) {
            this.text = text;
        }

        @Override
        public String getText() {
            return text;
        }
    }

    /**
     * 搜索信息
     *
     * @param items 搜索条目
     */
    public record Search(List<Item> items) {

        public boolean isEmpty() {
            return null == items || items.isEmpty();
        }

        /**
         * 搜索条目
         *
         * @param url   URL
         * @param title 标题
         */
        public record Item(String url, String title) {

        }

    }

    /**
     * 函数调用
     *
     * @param name      函数名
     * @param thoughts  思考过程
     * @param arguments 参数
     */
    public record FunctionCall(String name, String thoughts, String arguments) {

    }

}
