package io.github.ompc.erniebot4j.exception;

/**
 * ErnieBot响应错误异常
 * <p>
 * 由服务端返回的错误信息，详细的错误码和错误信息请参考文档
 * <a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/tlmyncueh">错误码</a>
 * </p>
 */
public class ErnieBotResponseErrorException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;

    /**
     * 响应错误异常
     *
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     */
    public ErnieBotResponseErrorException(int errorCode, String errorMessage) {
        super("code=%s;message=%s;".formatted(errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * 获取错误信息
     *
     * @return 错误信息
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
