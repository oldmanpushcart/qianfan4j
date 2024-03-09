package io.github.oldmanpushcart.internal.qianfan4j.base.algo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.oldmanpushcart.internal.qianfan4j.base.api.ApiRequestImpl;
import io.github.oldmanpushcart.internal.qianfan4j.util.JacksonUtils;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoRequest;
import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;
import io.github.oldmanpushcart.qianfan4j.base.algo.Model;
import io.github.oldmanpushcart.qianfan4j.base.api.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.function.Function;

import static io.github.oldmanpushcart.qianfan4j.Constants.LOGGER_NAME;

public abstract class AlgoRequestImpl<M extends Model, R extends AlgoResponse> extends ApiRequestImpl<R> implements AlgoRequest<M, R> {

    private final static ObjectMapper mapper = JacksonUtils.mapper();
    private final static Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

    @JsonIgnore
    private final M model;

    @JsonProperty("user_id")
    private final String user;

    private final Class<? extends R> responseType;

    protected AlgoRequestImpl(Duration timeout, M model, Option option, String user, Class<? extends R> responseType) {
        super(timeout, option);
        this.model = model;
        this.user = user;
        this.responseType = responseType;
    }

    @Override
    public M model() {
        return model;
    }

    @Override
    public String user() {
        return user;
    }

    @Override
    public HttpRequest newHttpRequest(String token) {
        final var body = JacksonUtils.toJson(mapper, this);
        logger.debug("{} => {}", this, body);
        return HttpRequest.newBuilder()
                .uri(URI.create("%s?access_token=%s".formatted(model().remote(), token)))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    }

    @Override
    public Function<String, R> responseDeserializer() {
        return body -> {
            logger.debug("{} <= {}", this, body);
            return JacksonUtils.toObject(mapper, body, responseType);
        };
    }


}
