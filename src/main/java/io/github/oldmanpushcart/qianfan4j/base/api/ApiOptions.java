package io.github.oldmanpushcart.qianfan4j.base.api;

import static io.github.oldmanpushcart.internal.qianfan4j.util.CheckUtils.check;

public interface ApiOptions {

    /**
     * 是否以流式接口(SSE)的形式返回数据
     */
    Option.SimpleOpt<Boolean> IS_STREAM = new Option.SimpleOpt<>("stream", Boolean.class);

}
