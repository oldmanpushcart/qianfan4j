package io.github.ompc.erniebot4j.plugin;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * 获取图像URL
 */
@FunctionalInterface
public interface ImageUrlSupplier extends Supplier<CompletableFuture<URL>> {
}
