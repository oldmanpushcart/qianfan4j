package io.github.ompc.erniebot4j.chat;

import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Sentence;
import io.github.ompc.erniebot4j.executor.Usage;
import io.github.ompc.erniebot4j.util.Textualizable;

import java.util.List;

public record ChatResponse(
        String id,
        String type,
        long timestamp,
        Usage usage,
        Sentence sentence,
        FunctionCall call,
        Search search

) implements Response {

    public boolean isFunctionCall() {
        return null != call;
    }

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

    public record FunctionCall(String name, String thoughts, String arguments) {

    }

}
