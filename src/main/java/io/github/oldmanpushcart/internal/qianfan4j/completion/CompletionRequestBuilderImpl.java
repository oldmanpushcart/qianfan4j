package io.github.oldmanpushcart.internal.qianfan4j.completion;

import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionModel;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionRequest;

import static java.util.Objects.requireNonNull;

public class CompletionRequestBuilderImpl extends AlgoRequestBuilderImpl<CompletionModel, CompletionRequest, CompletionRequest.Builder>
        implements CompletionRequest.Builder {

    private String prompt;

    @Override
    public CompletionRequest.Builder prompt(String prompt) {
        this.prompt = requireNonNull(prompt);
        return this;
    }

    @Override
    public CompletionRequest build() {
        return new CompletionRequestImpl(
                timeout(),
                requireNonNull(model()),
                option(),
                user(),
                requireNonNull(prompt)
        );
    }

}
