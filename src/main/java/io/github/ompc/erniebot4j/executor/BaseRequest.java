package io.github.ompc.erniebot4j.executor;

import java.time.Duration;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;

/**
 * 基础请求
 */
public abstract class BaseRequest<M extends Model> implements Request {

    private final M model;
    private final Option option;
    private final Duration timeout;
    private final String user;

    protected BaseRequest(BaseBuilder<M, ?, ?> builder) {
        this.model = requireNonNull(builder.model());
        this.option = requireNonNullElseGet(builder.option(), Option::new);
        this.timeout = builder.timeout();
        this.user = builder.user();
    }

    @Override
    public M model() {
        return model;
    }

    @Override
    public Option option() {
        return option;
    }

    @Override
    public Duration timeout() {
        return timeout;
    }

    @Override
    public String user() {
        return user;
    }

    /**
     * 基础构造器
     *
     * @param <R> 构造结果类型
     * @param <B> 构造器类型
     */
    protected static abstract class BaseBuilder<M extends Model, R extends BaseRequest<M>, B extends BaseBuilder<M, R, B>> {

        private M model;
        private Option option = new Option();
        private Duration timeout;
        private String user;

        protected BaseBuilder() {

        }

        protected BaseBuilder(R request) {
            this.model = request.model();
            this.option = request.option();
            this.timeout = request.timeout();
            this.user = request.user();
        }

        /**
         * 设置模型
         *
         * @param model 模型
         * @return this
         */
        public B model(M model) {
            this.model = requireNonNull(model);
            return self();
        }

        /**
         * 模型
         *
         * @return 模型
         */
        protected M model() {
            return model;
        }

        /**
         * 设置选项
         *
         * @param option 选项
         * @return this
         */
        public B option(Option option) {
            this.option = requireNonNull(option);
            return self();
        }

        /**
         * 选项
         *
         * @param opt   选项
         * @param value 选项值
         * @param <OT>  选项类型
         * @param <OR>  选项值类型
         * @return this
         */
        public <OT, OR> B option(Option.Opt<OT, OR> opt, OT value) {
            option.option(opt, value);
            return self();
        }

        /**
         * 设置选项
         *
         * @param name  选项名
         * @param value 选项值
         * @return this
         */
        public B option(String name, Object value) {
            option.option(name, value);
            return self();
        }

        /**
         * 选项
         *
         * @return 选项
         */
        protected Option option() {
            return option;
        }

        /**
         * 设置超时
         *
         * @param timeout 超时
         * @return this
         */
        public B timeout(Duration timeout) {
            this.timeout = timeout;
            return self();
        }

        /**
         * 超时
         *
         * @return 超时
         */
        protected Duration timeout() {
            return timeout;
        }

        /**
         * 设置用户
         *
         * @param user 用户
         * @return this
         */
        public B user(String user) {
            this.user = user;
            return self();
        }

        /**
         * 用户
         *
         * @return 用户
         */
        protected String user() {
            return user;
        }

        /**
         * 自身
         *
         * @return this
         */
        @SuppressWarnings("unchecked")
        protected B self() {
            return (B) this;
        }

        /**
         * 构建
         *
         * @return 构建结果
         */
        protected abstract R build();

    }

}
