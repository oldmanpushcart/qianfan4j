package io.github.ompc.erniebot4j.chat.http;

import io.github.ompc.erniebot4j.chat.ChatResponse;
import io.github.ompc.erniebot4j.executor.Usage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 对话执行统计
 */
class Statistics {

    private final AtomicReference<Usage> usageRef = new AtomicReference<>(new Usage(0, 0, 0));
    private final Set<ChatResponse.Search.Item> searchItemSet = ConcurrentHashMap.newKeySet();
    private final Set<String> uniqueIdSet = ConcurrentHashMap.newKeySet();

    /**
     * 统计对话应答
     *
     * @param response 对话应答
     */
    public void stats(ChatResponse response) {

        // 去重，如果response.id()已经存在，则不进行合并操作
        if (!uniqueIdSet.add(response.id())) {
            return;
        }

        // 统计TOKEN用量
        if (Objects.nonNull(response.usage())) {
            while (true) {
                final var existed = usageRef.get();
                final var updated = new Usage(
                        existed.prompt() + response.usage().prompt(),
                        existed.completion() + response.usage().completion(),
                        existed.total() + response.usage().total()
                );
                if (usageRef.compareAndSet(existed, updated)) {
                    break;
                }
            }
        }

        // 合并搜索信息
        if (Objects.nonNull(response.search()) && !response.search().isEmpty()) {
            searchItemSet.addAll(response.search().items());
        }

    }

    /**
     * 获取用量
     *
     * @return 用量
     */
    public Usage usage() {
        return usageRef.get();
    }

    /**
     * 获取搜索信息
     *
     * @return 搜索信息
     */
    public ChatResponse.Search search() {
        return new ChatResponse.Search(new ArrayList<>(searchItemSet));
    }

}
