package io.github.ompc.erniebot4j.executor;

/**
 * 句子
 *
 * @param index   编号
 * @param isLast  是否最后一句
 * @param content 内容
 */
public record Sentence(int index, boolean isLast, String content) {

    public static Sentence last(String content) {
        return new Sentence(0, true, content);
    }

}
