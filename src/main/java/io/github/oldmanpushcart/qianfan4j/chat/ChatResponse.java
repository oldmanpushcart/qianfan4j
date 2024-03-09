package io.github.oldmanpushcart.qianfan4j.chat;

import io.github.oldmanpushcart.qianfan4j.base.algo.AlgoResponse;
import io.github.oldmanpushcart.qianfan4j.util.Aggregatable;

public interface ChatResponse extends AlgoResponse, Aggregatable<ChatResponse> {

    boolean isLast();

    boolean isSafe();

    boolean isFunctionCall();

    String result();

    Search search();

    FunctionCall call();

}
