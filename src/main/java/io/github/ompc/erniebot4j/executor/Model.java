package io.github.ompc.erniebot4j.executor;

/**
 * 模型
 */
public interface Model {

    /**
     * 模型名称
     *
     * @return 模型名称
     */
    String name();

    /**
     * 模型地址
     *
     * @return 模型地址
     */
    String remote();

}
