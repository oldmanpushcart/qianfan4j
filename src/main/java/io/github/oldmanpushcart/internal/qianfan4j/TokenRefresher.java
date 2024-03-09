package io.github.oldmanpushcart.internal.qianfan4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 百度的令牌刷新器
 */
public class TokenRefresher {

    private static final String remote = "https://aip.baidubce.com/oauth/2.0/token";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final AtomicReference<CompletableFuture<Ret>> futureRef = new AtomicReference<>();
    private final ObjectMapper mapper = JacksonUtils.mapper();

    private final String ak;
    private final String sk;

    /**
     * 令牌刷新器
     *
     * @param ak APP-KEY
     * @param sk SECRET-KEY
     */
    public TokenRefresher(String ak, String sk) {
        this.ak = ak;
        this.sk = sk;
    }

    @Override
    public String toString() {
        return "erniebot://token";
    }

    /**
     * 刷新令牌
     * <ul>
     *     <li>已过期：主动刷新令牌</li>
     *     <li>未过期：直接返回令牌</li>
     * </ul>
     *
     * @param http HTTP客户端
     * @return 令牌
     */
    public CompletableFuture<String> refresh(HttpClient http) {
        return futureRef
                .updateAndGet(existed -> {
                    if (isNecessaryRefresh(existed)) {
                        synchronized (this) {
                            if (isNecessaryRefresh(existed)) {
                                return _refresh(http);
                            }
                        }
                    }
                    return existed;
                })
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
                        + "&client_id=" + ak
                        + "&client_secret=" + sk))
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

    // 令牌结果封装
    private record Ret(String token, long expired) {

        /**
         * 是否过期
         *
         * @return TRUE | FALSE
         */
        public boolean isExpired() {
            return expired <= System.currentTimeMillis();
        }

    }

}
