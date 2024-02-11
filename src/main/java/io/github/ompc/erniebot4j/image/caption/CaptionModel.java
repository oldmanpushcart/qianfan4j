package io.github.ompc.erniebot4j.image.caption;

import io.github.ompc.erniebot4j.executor.Model;

public record CaptionModel(String name, String remote) implements Model {

}
