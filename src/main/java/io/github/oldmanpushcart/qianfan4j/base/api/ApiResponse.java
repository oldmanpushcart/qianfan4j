package io.github.oldmanpushcart.qianfan4j.base.api;

/**
 * API应答
 */
public interface ApiResponse {

    /**
     * 获取唯一标识
     *
     * @return 唯一标识
     */
    String uuid();

    /**
     * 获取应答结果
     *
     * @return 应答结果
     */
    Ret ret();

}
