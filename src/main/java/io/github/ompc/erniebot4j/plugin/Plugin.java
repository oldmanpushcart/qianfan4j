package io.github.ompc.erniebot4j.plugin;

public record Plugin(String text) {

    /**
     * 知识库
     * @see <a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/Blmygz6t6">知识库</a>
     */
    public static final Plugin KNOWLEDGE_BASE = new Plugin("uuid-zhishiku");

    /**
     * 智慧图问
     * @see <a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/iln1kvmpw">智慧图问</a>
     */
    public static final Plugin CHAT_OCR = new Plugin("uuid-chatocr");

    /**
     * 天气预报
     * @see <a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/Glmypajea">天气</a>
     */
    public static final Plugin WEATHER_FORECAST = new Plugin("uuid-weatherforecast");
    
}
