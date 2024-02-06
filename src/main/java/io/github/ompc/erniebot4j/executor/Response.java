package io.github.ompc.erniebot4j.executor;

/**
 * 响应
 */
public interface Response {

    /**
     * 响应标识
     *
     * @return 响应标识
     */
    String id();

    /**
     * 响应类型
     *
     * @return 响应类型
     */
    String type();

    /**
     * 响应时间戳
     *
     * @return 响应时间戳
     */
    long timestamp();

    /**
     * 用量
     *
     * @return 用量
     */
    Usage usage();

}
