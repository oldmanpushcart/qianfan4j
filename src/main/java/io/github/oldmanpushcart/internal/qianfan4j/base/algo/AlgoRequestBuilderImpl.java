package io.github.oldmanpushcart.internal.qianfan4j.base.algo;

import io.github.oldmanpushcart.internal.qianfan4j.base.api.ApiRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;
import io.github.oldmanpushcart.qianfan4j.base.algo.Model;

import static java.util.Objects.requireNonNull;

/**
 * 算法请求构建器实现
 *
 * @param <M> 模型类型
 * @param <T> 算法请求类型
 * @param <B> 算法请求构建器类型
 */
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
