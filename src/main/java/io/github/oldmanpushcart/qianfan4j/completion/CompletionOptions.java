package io.github.oldmanpushcart.qianfan4j.completion;

import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoOptions;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;

/**
 * 续写选项
 */
public interface CompletionOptions extends AlgoOptions {

    /**
     * 停止词；当模型生成结果以停止词集合中某个元素结尾时，停止文本生成。
     * <ul>
     *     <li>每个停止词长度不超过20字符</li>
     *     <li>最多4个停止词</li>
     * </ul>
     */
    Option.SimpleOpt<String[]> STOP_WORDS = new Option.SimpleOpt<>("stop", String[].class);

}
