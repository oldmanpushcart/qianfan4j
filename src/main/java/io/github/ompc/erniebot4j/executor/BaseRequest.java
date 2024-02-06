package io.github.ompc.erniebot4j.executor;

import java.time.Duration;

/**
 * 基础请求
 */
public abstract class BaseRequest implements Request {

    private final Model model;
    private final Option option;
    private final Duration timeout;
    private final String user;

    /**
     * 基础请求
     *
     * @param model   模型
     * @param option  选项
     * @param timeout 超时
     * @param user    用户标识
     */
    protected BaseRequest(Model model, Option option, Duration timeout, String user) {
        this.model = model;
        this.option = option;
        this.timeout = timeout;
        this.user = user;
    }

    @Override
    public Model model() {
        return model;
    }

    @Override
    public Option option() {
        return option;
    }

    @Override
    public Duration timeout() {
        return timeout;
    }

    @Override
    public String user() {
        return user;
    }

}
