package io.github.ompc.erniebot4j.image.generation;

import io.github.ompc.erniebot4j.executor.Response;
import io.github.ompc.erniebot4j.executor.Usage;

import java.awt.image.BufferedImage;

/**
 * 文生图响应
 *
 * @param id        ID
 * @param type      类型
 * @param timestamp 时间戳
 * @param usage     用量
 * @param images    生成的图片
 */
public record GenImageResponse(

        String id,
        String type,
        long timestamp,
        Usage usage,
        BufferedImage[] images

) implements Response {

}
