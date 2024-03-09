package io.github.oldmanpushcart.qianfan4j.base.algo;

import io.github.oldmanpushcart.qianfan4j.base.api.ApiOptions;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;

import static io.github.oldmanpushcart.internal.qianfan4j.util.CheckUtils.check;

/**
 * 算法选项
 */
public interface AlgoOptions extends ApiOptions {

    /**
     * TEMPERATURE
     * <ul>
     *      <li>较高的数值会使输出更加随机，而较低的数值会使其更加集中和确定</li>
     *      <li>默认0.8，范围 (0, 1.0]，不能为0</li>
     * </ul>
     */
    Option.SimpleOpt<Float> TEMPERATURE = new Option.SimpleOpt<>("temperature", Float.class,
            v -> check(v, v > 0.0f && v <= 1.0f, "TEMPERATURE must be in range (0.0, 1.0]")
    );

    /**
     * TOP_P
     * <ul>
     *     <li>影响输出文本的多样性，取值越大，生成文本的多样性越强</li>
     *     <li>默认0.8，取值范围 [0, 1.0]</li>
     * </ul>
     */
    Option.SimpleOpt<Float> TOP_P = new Option.SimpleOpt<>("top_p", Float.class,
            v -> check(v, v >= 0.0f && v <= 1.0f, "TOP_P must be in range [0.0, 1.0]")
    );

    /**
     * TOP_K
     * <ul>
     *     <li>影响输出文本的多样性，取值越大，生成文本的多样性越强</li>
     *     <li>取值范围：正整数</li>
     * </ul>
     */
    Option.SimpleOpt<Integer> TOP_K = new Option.SimpleOpt<>("top_k", Integer.class,
            v -> check(v, v > 0, "TOP_K must be in range (0, Integer.MAX_VALUE]")
    );

    /**
     * PENALTY_SCORE；通过对已生成的token增加惩罚，减少重复生成的现象
     * <ul>
     *     <li>值越大表示惩罚越大</li>
     *     <li>取值范围：[1.0, 2.0]</li>
     * </ul>
     */
    Option.SimpleOpt<Float> PENALTY_SCORE = new Option.SimpleOpt<>("penalty_score", Float.class,
            v -> check(v, v >= 1.0f && v <= 2.0f, "PENALTY_SCORE must be in range [1.0, 2.0]")
    );

}
