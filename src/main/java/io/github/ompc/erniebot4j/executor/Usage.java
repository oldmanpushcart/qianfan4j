package io.github.ompc.erniebot4j.executor;

/**
 * 用量
 *
 * @param prompt     提示用量
 * @param completion 完成用量
 * @param total      总用量
 */
public record Usage(int prompt, int completion, int total) {

    /**
     * 用量
     */
    public Usage() {
        this(0, 0, 0);
    }

}
