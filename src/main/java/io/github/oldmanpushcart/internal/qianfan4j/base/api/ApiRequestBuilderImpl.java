package io.github.oldmanpushcart.internal.qianfan4j.base.api;

import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.base.api.ApiRequest;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

public abstract class ApiRequestBuilderImpl<T extends ApiRequest<?>, B extends ApiRequest.Builder<T, B>>
        implements ApiRequest.Builder<T, B> {

    protected ApiRequestBuilderImpl() {

    }

    protected ApiRequestBuilderImpl(T request) {
        this.timeout = request.timeout();
        this.option = request.option();
    }

    private Duration timeout;
    private Option option = new Option();

    @Override
    public B timeout(Duration timeout) {
        this.timeout = requireNonNull(timeout);
        return self();
    }

    @Override
    public <OT, OR> B option(Option.Opt<OT, OR> opt, OT value) {
        this.option.option(opt, value);
        return self();
    }

    @Override
    public B option(String name, Object value) {
        this.option.option(name, value);
        return self();
    }

    protected Duration timeout() {
        return timeout;
    }

    protected Option option() {
        return option;
    }

}
