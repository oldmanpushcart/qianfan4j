package io.github.ompc.erniebot4j.image.generation;

import io.github.ompc.erniebot4j.executor.Response;

import java.awt.image.BufferedImage;

public record GenImageResponse(

        String id,
        String type,
        long timestamp,
        BufferedImage[] images,
        Usage usage

) implements Response {

}
