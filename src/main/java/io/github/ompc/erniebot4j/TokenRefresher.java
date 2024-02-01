package io.github.ompc.erniebot4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ompc.erniebot4j.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class TokenRefresher {

    private static final String remote = "https://aip.baidubce.com/oauth/2.0/token";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AtomicReference<CompletableFuture<Ret>> futureRef = new AtomicReference<>();
    private final ObjectMapper mapper = JacksonUtils.mapper();

    private final String identity;
    private final String secret;

    public TokenRefresher(String identity, String secret) {
        this.identity = identity;
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "erniebot://token";
    }

    public CompletableFuture<String> refresh(HttpClient http) {

        return futureRef
                .updateAndGet(expect -> isNecessaryRefresh(expect) ? _refresh(http) : expect)
                .thenApply(Ret::token);
    }

    private static boolean isNecessaryRefresh(CompletableFuture<Ret> future) {
        return null == future
                || future.isCancelled()
                || future.isCompletedExceptionally()
                || future.join().isExpired();
    }

    private CompletableFuture<Ret> _refresh(HttpClient http) {
        final var request = HttpRequest.newBuilder()
                .uri(URI.create(remote
                        + "?grant_type=client_credentials"
                        + "&client_id=" + identity
                        + "&client_secret=" + secret))
                .GET()
                .build();
        return http.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {

                    // 检查HTTP状态码
                    if (response.statusCode() != 200) {
                        throw new RuntimeException("refresh token failure! status=%d; body=%s".formatted(
                                response.statusCode(),
                                response.body()
                        ));
                    }

                    // 解析应答json
                    final var node = JacksonUtils.toNode(mapper, response.body());
                    final var ret = new Ret(
                            node.get("access_token").asText(),
                            System.currentTimeMillis() + node.get("expires_in").asLong() * 1000
                    );

                    logger.debug("{}/refresh success! expired={};", this, ret.expired());

                    // 返回正确的token
                    return ret;
                });
    }

    record Ret(String token, long expired) {

        public boolean isExpired() {
            return expired <= System.currentTimeMillis();
        }

    }

}
