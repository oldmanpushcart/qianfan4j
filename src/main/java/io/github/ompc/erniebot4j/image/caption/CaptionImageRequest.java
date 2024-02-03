package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.Model;
import io.github.ompc.erniebot4j.executor.Option;
import io.github.ompc.erniebot4j.executor.Request;

import java.time.Duration;

public class CaptionImageRequest implements Request {
    @Override
    public Model model() {
        return null;
    }

    @Override
    public String user() {
        return null;
    }

    @Override
    public Option options() {
        return null;
    }

    @Override
    public Duration timeout() {
        return null;
    }
}
