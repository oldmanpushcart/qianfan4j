package io.github.ompc.erniebot4j.exception;

public class ErnieBotResponseErrorException extends RuntimeException {

    private final int errorCode;
    private final String errorMessage;

    public ErnieBotResponseErrorException(int errorCode, String errorMessage) {
        super("code=%s;message=%s;".formatted(errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
