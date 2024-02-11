package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.Model;

/**
 * 图生文模型
 *
 * @param name   名称
 * @param remote 远程地址
 */
public record CaptionModel(String name, String remote) implements Model {

}
