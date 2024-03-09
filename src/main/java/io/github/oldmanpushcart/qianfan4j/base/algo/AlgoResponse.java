package io.github.oldmanpushcart.qianfan4j.base.algo;

import io.github.oldmanpushcart.qianfan4j.base.api.ApiResponse;

/**
 * 算法应答
 */
public interface AlgoResponse extends ApiResponse {

    /**
     * 获取用量
     *
     * @return 用量
     */
    Usage usage();

}
