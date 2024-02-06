package io.github.ompc.erniebot4j.executor;

import java.time.Duration;

/**
 * 请求
 */
public interface Request {

    /**
     * 模型
     *
     * @return 模型
     */
    Model model();

    /**
     * 选项
     *
     * @return 选项
     */
    Option option();

    /**
     * 请求超时
     *
     * @return 请求超时
     */
    Duration timeout();

    /**
     * 请求用户标识；用于跟踪和调试请求
     *
     * @return 请求用户标识
     */
    String user();

}
