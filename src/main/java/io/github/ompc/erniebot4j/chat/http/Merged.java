package io.github.ompc.erniebot4j.chat.http;

import io.github.ompc.erniebot4j.chat.ChatResponse;
import io.github.ompc.erniebot4j.executor.Usage;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

class Merged {

    private final AtomicReference<Usage> usageRef = new AtomicReference<>(new Usage(0, 0, 0));
    private final Set<ChatResponse.Search.Item> searchItemSet = ConcurrentHashMap.newKeySet();
    private final Set<String> uniqueIdSet = ConcurrentHashMap.newKeySet();

    public void merge(ChatResponse response) {

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

    public Usage usage() {
        return usageRef.get();
    }

    public ChatResponse.Search search() {
        return new ChatResponse.Search(new ArrayList<>(searchItemSet));
    }

}
