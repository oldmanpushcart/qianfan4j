package io.github.oldmanpushcart.qianfan4j.base.api;

/**
 * API异常
 */
public class ApiException extends RuntimeException {

    private final Ret ret;

    /**
     * 构造API异常
     *
     * @param response 应答
     */
    public ApiException(ApiResponse response) {
        super("api response error! code=%s;message=%s;".formatted(
                response.ret().code(),
                response.ret().message()
        ));
        this.ret = response.ret();
    }

    /**
     * 获取应答结果
     *
     * @return 应答结果
     */
    public Ret ret() {
        return ret;
    }

}
