package io.github.ompc.erniebot4j.plugin;

public record Plugin(String text) {

    /**
     * 知识库
     */
    public static final Plugin KNOWLEDGE_BASE = new Plugin("uuid-zhishiku");

    /**
     * 智慧图问
     */
    public static final Plugin CHAT_OCR = new Plugin("uuid-chatocr");

    /**
     * 天气预报
     */
    public static final Plugin WEATHER_FORECAST = new Plugin("uuid-weatherforecast");
    
}
