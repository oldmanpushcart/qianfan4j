package io.github.oldmanpushcart.internal.qianfan4j.completion;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestImpl;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionModel;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionRequest;
import io.github.oldmanpushcart.qianfan4j.completion.CompletionResponse;

import java.time.Duration;

public class CompletionRequestImpl extends AlgoRequestImpl<CompletionModel, CompletionResponse> implements CompletionRequest {

    @JsonProperty("prompt")
    private final String prompt;

    protected CompletionRequestImpl(Duration timeout, CompletionModel model, Option option, String user, String prompt) {
        super(timeout, model, option, user, CompletionResponseImpl.class);
        this.prompt = prompt;
    }

    @Override
    public String prompt() {
        return prompt;
    }

}
