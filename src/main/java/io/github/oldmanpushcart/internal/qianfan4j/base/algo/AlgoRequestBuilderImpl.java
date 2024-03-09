package io.github.oldmanpushcart.internal.qianfan4j.base.algo;

import io.github.oldmanpushcart.internal.qianfan4j.base.api.ApiRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;
import io.github.oldmanpushcart.qianfan4j.base.algo.Model;

import static java.util.Objects.requireNonNull;

public abstract class AlgoRequestBuilderImpl<M extends Model, T extends AlgoRequest<M, ?>, B extends AlgoRequest.Builder<M, T, B>>
        extends ApiRequestBuilderImpl<T, B>
        implements AlgoRequest.Builder<M, T, B> {

    protected AlgoRequestBuilderImpl() {

    }

    protected AlgoRequestBuilderImpl(T request) {
        super(request);
        this.user = request.user();
        this.model = request.model();
    }

    private String user;
    private M model;

    @Override
    public B model(M model) {
        this.model = requireNonNull(model);
        return self();
    }

    @Override
    public B user(String user) {
        this.user = requireNonNull(user);
        return self();
    }

    protected M model() {
        return model;
    }

    protected String user() {
        return user;
    }

}
