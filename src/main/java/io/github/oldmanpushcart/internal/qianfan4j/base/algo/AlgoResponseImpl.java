package io.github.oldmanpushcart.internal.qianfan4j.base.algo;

import io.github.oldmanpushcart.internal.qianfan4j.base.api.ApiResponseImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;
import io.github.oldmanpushcart.qianfan4j.base.algo.Usage;
import io.github.oldmanpushcart.qianfan4j.base.api.Ret;

public class AlgoResponseImpl extends ApiResponseImpl implements AlgoResponse {

    private final Usage usage;

    public AlgoResponseImpl(String uuid, Ret ret, Usage usage) {
        super(uuid, ret);
        this.usage = usage;
    }

    @Override
    public Usage usage() {
        return usage;
    }

}
