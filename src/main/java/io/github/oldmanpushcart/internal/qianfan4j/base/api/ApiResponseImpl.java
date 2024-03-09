package io.github.oldmanpushcart.internal.qianfan4j.base.api;

import io.github.oldmanpushcart.qianfan4j.base.api.ApiResponse;
import io.github.oldmanpushcart.qianfan4j.base.api.Ret;

public class ApiResponseImpl implements ApiResponse {

    private final String uuid;
    private final Ret ret;

    public ApiResponseImpl(String uuid, Ret ret) {
        this.uuid = uuid;
        this.ret = ret;
    }

    @Override
    public String uuid() {
        return uuid;
    }

    @Override
    public Ret ret() {
        return ret;
    }


}
