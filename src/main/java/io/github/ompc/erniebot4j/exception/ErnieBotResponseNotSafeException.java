package io.github.ompc.erniebot4j.exception;

/**
 * ErnieBot响应不安全异常
 * <p>
 * 用户输入是否存在安全风险，比如涉及政治、色情、暴力等内容
 * </p>
 */
public class ErnieBotResponseNotSafeException extends RuntimeException {

    private final int ban;
    private final String content;

    /**
     * 响应不安全异常
     *
     * @param ban     危险对话轮数
     * @param content 模型应答
     */
    public ErnieBotResponseNotSafeException(int ban, String content) {
        super("response not safe, ban=%s;content=%s;".formatted(ban, content));
        this.ban = ban;
        this.content = content;
    }

    public ErnieBotResponseNotSafeException(String content) {
        this(-1, content);
    }

    /**
     * 获取危险对话轮数（-1表示当前轮）
     *
     * @return 危险对话轮数
     */
    public int getBan() {
        return ban;
    }

    /**
     * 获取模型应答
     *
     * @return 模型应答
     */
    public String getContent() {
        return content;
    }

}
