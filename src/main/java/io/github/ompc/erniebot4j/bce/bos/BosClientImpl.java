package io.github.ompc.erniebot4j.bce.bos;

import io.github.ompc.erniebot4j.bce.BceCredential;
import io.github.ompc.erniebot4j.bce.util.BceSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static io.github.ompc.erniebot4j.bce.util.BceSigner.DATE_FORMATTER_ISO8601;
import static io.github.ompc.erniebot4j.util.StringUtils.isBlank;
import static java.util.Objects.requireNonNull;

class BosClientImpl implements BosClient {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HttpClient http;
    private final BceCredential credential;
    private final Executor executor;

    BosClientImpl(BosClient.Builder builder) {
        this.credential = requireNonNull(builder.credential());
        this.executor = requireNonNull(builder.executor());
        this.http = HttpClient.newBuilder()
                .executor(executor)
                .build();
    }

    @Override
    public String toString() {
        return "erniebot://bce/bos";
    }

    @Override
    public CompletableFuture<URL> putBinary(String bucket, String key, byte[] data) {
        return put(bucket, "application/octet-stream", key, data);
    }


    @Override
    public CompletableFuture<URL> putPngImage(String bucket, String key, BufferedImage image) {
        try (final var output = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", output);
            return put(bucket, "image/png", key, output.toByteArray());
        } catch (Exception ex) {
            return CompletableFuture.failedFuture(ex);
        }
    }

    private CompletableFuture<URL> put(String bucket, String mime, String key, byte[] data) {
        // 时间戳，用于请求HTTP和签名
        final var timestamp = System.currentTimeMillis();

        // 原始HTTP请求
        final var request = HttpRequest.newBuilder()
                .uri(URI.create("https://%s.bj.bcebos.com/%s".formatted(bucket, key)))
                .header("Content-Type", mime)
                .header("x-bce-date", DATE_FORMATTER_ISO8601.format(new Date(timestamp).toInstant()))
                .PUT(HttpRequest.BodyPublishers.ofByteArray(data))
                .build();

        // 签名HTTP请求
        final var signRequest = BceSigner.sign(request, credential, timestamp);

        // 记录请求日志
        logger.debug("{}/put => bucket={};key={};mime={};size={}(bytes)",
                this,
                bucket,
                key,
                mime,
                data.length
        );

        // 发送HTTP请求
        return http.sendAsync(signRequest, HttpResponse.BodyHandlers.ofString())
                .thenApplyAsync(v -> v, executor)
                .thenApply(response -> {

                    final var code = response.statusCode();
                    final var body = response.body();

                    // 记录返回日志
                    logger.debug("{}/put <= bucket={};key={};code={};message={};",
                            this,
                            bucket,
                            key,
                            code,
                            isBlank(body) ? "success" : body
                    );

                    // 检查HTTP响应是否成功
                    if (code != 200) {
                        throw new RuntimeException("BOS[bucket=%s;key=%s;] put object failed! code=%s;message=%s;".formatted(
                                bucket,
                                key,
                                code,
                                body
                        ));
                    }

                    // 转为下载URL返回
                    return request.uri();

                })
                .thenApply(uri -> {
                    try {
                        return uri.toURL();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
