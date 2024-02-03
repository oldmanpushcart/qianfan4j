package io.github.ompc.erniebot4j.image.generation;

import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Usage;

import java.awt.image.BufferedImage;

public record GenImageResponse(

        String id,
        String type,
        long timestamp,
        Usage usage,
        BufferedImage[] images

) implements Response {

}
