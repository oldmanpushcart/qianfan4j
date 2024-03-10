package io.github.oldmanpushcart.internal.qianfan4j.image.generation;

import io.github.oldmanpushcart.internal.qianfan4j.base.algo.AlgoRequestBuilderImpl;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageModel;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageRequest;

import java.util.Objects;

public class GenerationImageRequestBuilderImpl extends AlgoRequestBuilderImpl<GenerationImageModel, GenerationImageRequest, GenerationImageRequest.Builder>
        implements GenerationImageRequest.Builder {

    private String prompt;
    private String negative;

    @Override
    public GenerationImageRequest.Builder prompt(String prompt) {
        this.prompt = Objects.requireNonNull(prompt);
        return this;
    }

    @Override
    public GenerationImageRequest.Builder negative(String negative) {
        this.negative = Objects.requireNonNull(negative);
        return this;
    }

    @Override
    public GenerationImageRequest build() {
        return new GenerationImageRequestImpl(
                timeout(),
                model(),
                option(),
                user(),
                prompt,
                negative
        );
    }

}
