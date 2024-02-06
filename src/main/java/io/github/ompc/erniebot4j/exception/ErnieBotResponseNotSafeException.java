package io.github.ompc.erniebot4j.exception;

public class ErnieBotResponseNotSafeException extends RuntimeException {

    private final int ban;
    private final String content;

    public ErnieBotResponseNotSafeException(int ban, String content) {
        super("response not safe, ban=%s;content=%s;".formatted(ban, content));
        this.ban = ban;
        this.content = content;
    }

    public ErnieBotResponseNotSafeException(String content) {
        this(-1, content);
    }

    public int getBan() {
        return ban;
    }

    public String getContent() {
        return content;
    }

}
