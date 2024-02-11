package io.github.ompc.erniebot4j.cloud.baidu.util;

import io.github.ompc.erniebot4j.cloud.baidu.BceCredential;
import io.github.ompc.erniebot4j.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * BCE签名工具
 * <p>
 * 参考资料：
 *  <ul>
 *      <li><a href="https://cloud.baidu.com/doc/Reference/s/njwvz1yfu">生成认证字符串</a></li>
 *      <li><a href="https://cloud.baidu.com/signature/index.html">签名计算工具</a></li>
 * </p>
 */
public class BceSigner {

    private static final String HEADER_HOST = "Host";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_CONTENT_MD5 = "Content-MD5";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_CONTENT_LENGTH = "Content-Length";
    private static final String BCE_AUTH_VERSION = "bce-auth-v1";

    /**
     * 签名需要的日期格式化
     */
    public static final DateTimeFormatter DATE_FORMATTER_ISO8601 = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
            .withZone(ZoneId.of("UTC"));

    /**
     * 签名超时时间（单位：秒）；
     * 从签名时间开始，多少秒内有效
     */
    private static final int DEFAULT_EXPIRATION_IN_SECONDS = 1800;

    /**
     * 需要参加签名的HEADER名称；
     * 如果请求中包含以下HEADER，则需要参加签名
     */
    private static final Set<String> REQUIRED_HEADER_SET = Set.of(
                    HEADER_HOST,
                    HEADER_CONTENT_LENGTH,
                    HEADER_CONTENT_TYPE,
                    HEADER_CONTENT_MD5
            )
            .stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());

    /**
     * 格式化时间戳为BCE日期格式
     *
     * @param timestamp 时间戳
     * @return BCE日期格式
     */
    public static String formatToBceDate(long timestamp) {
        return DATE_FORMATTER_ISO8601.format(new Date(timestamp).toInstant());
    }

    /**
     * 对请求进行签名
     *
     * @param request    请求
     * @param credential BCE凭证
     * @return 签名后的请求
     */
    public static HttpRequest sign(HttpRequest request, BceCredential credential) {
        return sign(request, credential, System.currentTimeMillis());
    }

    /**
     * 对请求进行签名
     *
     * @param request    请求
     * @param credential BCE凭证
     * @param timestamp  签名时间戳
     * @return 签名后的请求
     */
    public static HttpRequest sign(HttpRequest request, BceCredential credential, long timestamp) {
        final var headers = new HashMap<String, String>();
        final var time = new Date(timestamp);

        // 添加授权
        headers.put(HEADER_AUTHORIZATION, toAuthorization(request, credential, time));

        // 重新构建请求
        final var builder = HttpRequest.newBuilder(request, (k, v) -> true);
        headers.forEach(builder::header);
        return builder.build();
    }

    private static String toAuthorization(HttpRequest request, BceCredential credential, Date timestamp) {

        // 签名前缀字符串
        final var auth = "%s/%s/%s/%s".formatted(
                BCE_AUTH_VERSION,
                credential.ak(),
                DATE_FORMATTER_ISO8601.format(timestamp.toInstant()),
                DEFAULT_EXPIRATION_IN_SECONDS
        );

        // 派生签名密钥
        final var signKey = sha256Hex(credential.sk(), auth);

        // 最终参与签名的HEADER集合
        final var signingHeaders = new TreeSet<String>();

        // 规范化请求
        final var canonicalRequest = "%s\n%s\n%s\n%s".formatted(
                request.method(),
                toCanonicalUriPathString(request),
                toCanonicalQueryString(request),
                toCanonicalHeaderMapString(request, signingHeaders)
        );

        // 签名摘要
        final var signature = sha256Hex(signKey, canonicalRequest);

        // 返回最终认证字符串
        return "%s/%s/%s".formatted(auth, String.join(";", signingHeaders), signature);
    }

    // 规范化URI路径
    private static String toCanonicalUriPathString(HttpRequest request) {
        return Optional.ofNullable(request.uri().getPath())
                .map(path -> path.startsWith("/")
                        ? path
                        : "/%s".formatted(path)
                )
                .orElse("/");
    }

    // 规范化查询字符串
    private static String toCanonicalQueryString(HttpRequest request) {
        return Optional.ofNullable(request.uri().getQuery())
                .map(query -> Arrays.stream(query.split("&"))
                        .map(kv -> kv.split("=", 2))
                        .map(kv -> "%s=%s".formatted(
                                URLEncoder.encode(kv[0], UTF_8),
                                URLEncoder.encode(kv[1], UTF_8)
                        ))
                        .sorted()
                        .collect(Collectors.joining("&"))
                )
                .orElse("");
    }

    // 规范化HEADER
    private static String toCanonicalHeaderMapString(HttpRequest request, Set<String> singed) {
        final var clone = new HashMap<>(request.headers().map());
        clone.put(HEADER_HOST, List.of(request.uri().getHost()));
        return clone.entrySet().stream()

                // 多值转换为逗号分隔
                .map(e -> Map.entry(
                        e.getKey().trim().toLowerCase(),
                        String.join(",", e.getValue()).trim()
                ))

                // 过滤调不需要参与签名KV
                .filter(e -> {
                    final var key = e.getKey();
                    if (key.startsWith("x-bce-") || (REQUIRED_HEADER_SET.contains(key) && !HEADER_AUTHORIZATION.equalsIgnoreCase(key))) {
                        singed.add(key);
                        return true;
                    }
                    return false;
                })

                // 重新拼接回K:V字符串
                .map(entry -> "%s:%s".formatted(
                        URLEncoder.encode(entry.getKey(), UTF_8),
                        URLEncoder.encode(entry.getValue(), UTF_8)
                ))

                // 排个序
                .sorted()

                // KV对按行拼接
                .collect(Collectors.joining("\n"));
    }

    /**
     * 生成SHA256摘要
     *
     * @param signingKey   签名密钥
     * @param stringToSign 待签名字符串
     * @return SHA256摘要
     */
    private static String sha256Hex(String signingKey, String stringToSign) {
        try {
            final var mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(signingKey.getBytes(UTF_8), "HmacSHA256"));
            return StringUtils.encodeHex(mac.doFinal(stringToSign.getBytes(UTF_8)), true);
        } catch (Exception e) {
            throw new RuntimeException("fail to generate the signature", e);
        }
    }

}
