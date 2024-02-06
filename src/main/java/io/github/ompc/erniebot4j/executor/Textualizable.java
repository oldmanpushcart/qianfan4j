package io.github.ompc.erniebot4j.executor;

/**
 * 文本化
 * <p>
 * 当一个对象实现文本化接口时，表示该对象可以在序列化过程中被转换为文本。
 * 可以参考：
 * <ul>
 *     <li>{@link io.github.ompc.erniebot4j.chat.message.Message.Role}</li>
 *     <li>{@link io.github.ompc.erniebot4j.chat.ChatResponse.Format}</li>
 *     <li>{@code ...}</li>
 * </ul>
 * </p>
 */
public interface Textualizable {

    /**
     * 文本内容
     *
     * @return 文本内容
     */
    String getText();

}
