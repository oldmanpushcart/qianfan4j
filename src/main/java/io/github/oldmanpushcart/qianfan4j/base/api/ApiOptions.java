package io.github.oldmanpushcart.qianfan4j.base.api;

/**
 * API选项
 */
public interface ApiOptions {

    /**
     * 是否以流式接口(SSE)的形式返回数据
     */
    Option.SimpleOpt<Boolean> IS_STREAM = new Option.SimpleOpt<>("stream", Boolean.class);

}
