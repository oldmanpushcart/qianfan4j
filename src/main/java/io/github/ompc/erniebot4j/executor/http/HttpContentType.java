package io.github.ompc.erniebot4j.executor.http;

import io.github.ompc.erniebot4j.util.FeatureCodec;

import java.net.http.HttpHeaders;
import java.nio.charset.Charset;
import java.util.Map;

import static java.util.Collections.emptyMap;

public record HttpContentType(String mime, Map<String, String> parameters) {

    public static final String MIME_APPLICATION_JSON = "application/json";
    public static final String MIME_TEXT_EVENT_STREAM = "text/event-stream";

    public Charset charset() {
        return Charset.forName(parameters.getOrDefault("charset", "UTF-8"));
    }

    public static HttpContentType parse(HttpHeaders headers) {
        return headers.firstValue("Content-Type")
                .map(ct -> {
                    final var codec = new FeatureCodec(';', '=');
                    final var mime = ct.substring(ct.indexOf(":") + 1, ct.indexOf(";")).trim();
                    final var parameterString = ct.substring(ct.indexOf(";") + 1).trim();
                    final var parameters = codec.toMap(parameterString);
                    return new HttpContentType(mime, parameters);
                })
                .orElseGet(() -> new HttpContentType(MIME_APPLICATION_JSON, emptyMap()));
    }

}
