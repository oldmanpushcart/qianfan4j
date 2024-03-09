package io.github.oldmanpushcart.qianfan4j.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.util.List;

/**
 * 搜索信息
 *
 * @param items 搜索条目
 */
public record Search(List<Item> items) {

    /**
     * 搜索结果是否为空
     *
     * @return TRUE | FALSE
     */
    public boolean isEmpty() {
        return null == items || items.isEmpty();
    }

    /**
     * 搜索条目
     *
     * @param title 标题
     * @param uri   URI
     */
    public record Item(

            @JsonProperty("title")
            String title,

            @JsonProperty("url")
            URI uri

    ) {

    }

}
