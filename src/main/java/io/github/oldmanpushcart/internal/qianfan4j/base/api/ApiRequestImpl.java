package io.github.oldmanpushcart.internal.qianfan4j.base.api;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiRequest;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiResponse;

import java.time.Duration;
import java.util.Map;

public abstract class ApiRequestImpl<R extends ApiResponse> implements ApiRequest<R> {

    @JsonIgnore
    private final Duration timeout;

    @JsonIgnore
    private final Option option;

    protected ApiRequestImpl(Duration timeout, Option option) {
        this.timeout = timeout;
        this.option = option;
    }

    @Override
    public Duration timeout() {
        return timeout;
    }

    @Override
    public Option option() {
        return option;
    }

    @JsonAnyGetter
    Map<String,Object> export() {
        return option.export();
    }

}
