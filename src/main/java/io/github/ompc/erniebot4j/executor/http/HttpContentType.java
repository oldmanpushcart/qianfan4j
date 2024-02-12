package io.github.ompc.erniebot4j.executor.http;

import io.github.ompc.erniebot4j.util.FeatureCodec;

import java.net.http.HttpHeaders;
import java.nio.charset.Charset;
import java.util.Map;

import static java.util.Collections.emptyMap;

/**
 * HTTP Content-Type
 *
 * @param mime       MIME
 * @param parameters 参数
 */
public record HttpContentType(String mime, Map<String, String> parameters) {

    public static final String MIME_APPLICATION_JSON = "application/json";
    public static final String MIME_TEXT_EVENT_STREAM = "text/event-stream";

    /**
     * 获取字符集
     *
     * @return 字符集
     */
    public Charset charset() {
        return Charset.forName(parameters.getOrDefault("charset", "UTF-8"));
    }

    /**
     * 解析Content-Type
     *
     * @param headers HTTP头
     * @return Content-Type
     */
    public static HttpContentType parse(HttpHeaders headers) {
        return headers.firstValue("content-type")
                .map(ct -> {

                    // mime only
                    if (!ct.contains(";")) {
                        return new HttpContentType(ct, emptyMap());
                    }

                    // mime with parameters
                    final var mime = ct.substring(0, ct.indexOf(";")).trim();
                    final var parameterString = ct.substring(ct.indexOf(";") + 1).trim();
                    final var codec = new FeatureCodec(';', '=');
                    final var parameters = codec.toMap(parameterString);
                    return new HttpContentType(mime, parameters);
                })
                .orElseGet(() -> new HttpContentType(MIME_APPLICATION_JSON, emptyMap()));
    }

}
