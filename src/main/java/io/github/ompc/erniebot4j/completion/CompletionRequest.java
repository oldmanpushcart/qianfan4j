package io.github.ompc.erniebot4j.completion;

import io.github.ompc.erniebot4j.executor.BaseRequest;
import io.github.ompc.erniebot4j.executor.Request;

import static java.util.Objects.requireNonNull;

/**
 * 续写请求
 */
public final class CompletionRequest extends BaseRequest<CompletionModel> implements Request {

    private final String prompt;

    private CompletionRequest(Builder builder) {
        super(builder);
        this.prompt = requireNonNull(builder.prompt);
    }

    /**
     * 获取提示
     *
     * @return 提示
     */
    public String prompt() {
        return prompt;
    }

    /**
     * 补全请求构建器
     */
    public static class Builder extends BaseRequest.BaseBuilder<CompletionModel, CompletionRequest, Builder> {

        private String prompt;

        /**
         * 设置提示
         *
         * @param prompt 提示
         * @return this
         */
        public Builder prompt(String prompt) {
            this.prompt = requireNonNull(prompt);
            return this;
        }

        @Override
        public CompletionRequest build() {
            return new CompletionRequest(this);
        }

    }

}
