package io.github.ompc.erniebot4j.executor;

import io.github.ompc.erniebot4j.executor.Option.SimpleOpt;

import static io.github.ompc.erniebot4j.util.CheckUtils.check;

/**
 * 选项集合；用于定义模型的可选参数
 */
public interface Options {

    /**
     * TEMPERATURE
     * <ul>
     *      <li>较高的数值会使输出更加随机，而较低的数值会使其更加集中和确定</li>
     *      <li>默认0.8，范围 (0, 1.0]，不能为0</li>
     * </ul>
     */
    SimpleOpt<Float> TEMPERATURE = new SimpleOpt<>("temperature", Float.class,
            v -> check(v, v > 0.0f && v <= 1.0f, "TEMPERATURE must be in range (0.0, 1.0]")
    );

    /**
     * TOP_P
     * <ul>
     *     <li>影响输出文本的多样性，取值越大，生成文本的多样性越强</li>
     *     <li>默认0.8，取值范围 [0, 1.0]</li>
     * </ul>
     */
    SimpleOpt<Float> TOP_P = new SimpleOpt<>("top_p", Float.class,
            v -> check(v, v >= 0.0f && v <= 1.0f, "TOP_P must be in range [0.0, 1.0]")
    );

    /**
     * TOP_K
     * <ul>
     *     <li>影响输出文本的多样性，取值越大，生成文本的多样性越强</li>
     *     <li>取值范围：正整数</li>
     * </ul>
     */
    SimpleOpt<Integer> TOP_K = new SimpleOpt<>("top_k", Integer.class,
            v -> check(v, v > 0, "TOP_K must be in range (0, Integer.MAX_VALUE]")
    );

    /**
     * PENALTY_SCORE；通过对已生成的token增加惩罚，减少重复生成的现象
     * <ul>
     *     <li>值越大表示惩罚越大</li>
     *     <li>取值范围：[1.0, 2.0]</li>
     * </ul>
     */
    SimpleOpt<Float> PENALTY_SCORE = new SimpleOpt<>("penalty_score", Float.class,
            v -> check(v, v >= 1.0f && v <= 2.0f, "PENALTY_SCORE must be in range [1.0, 2.0]")
    );

    /**
     * 停止词；当模型生成结果以停止词集合中某个元素结尾时，停止文本生成。
     * <ul>
     *     <li>每个停止词长度不超过20字符</li>
     *     <li>最多4个停止词</li>
     * </ul>
     */
    SimpleOpt<String[]> STOP_WORDS = new SimpleOpt<>("stop", String[].class);

    /**
     * 是否以流式接口(SSE)的形式返回数据
     */
    SimpleOpt<Boolean> IS_STREAM = new SimpleOpt<>("stream", Boolean.class);


}
