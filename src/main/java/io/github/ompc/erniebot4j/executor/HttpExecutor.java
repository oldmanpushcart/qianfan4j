package io.github.ompc.erniebot4j.executor;

import java.net.http.HttpClient;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface HttpExecutor<T extends Request, R extends Response> {

    CompletableFuture<R> execute(HttpClient http, T request, Consumer<R> consumer);

}
