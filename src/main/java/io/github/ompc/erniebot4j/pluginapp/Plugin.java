package io.github.ompc.erniebot4j.pluginapp;

/**
 * 插件类型
 *
 * @param id 插件唯一标识
 */
public record Plugin(String id) {

    /**
     * 知识库
     * <p>
     * 知识库是指让开发者（甚至非技术人员）以简单的方式管理数据集，包括分片、清洗、向量计算等能力。
     * 当在应用配置中关联了知识库，该插件自动选定
     * </p>
     *
     * @see <a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/Blmygz6t6">知识库</a>
     */
    public static final Plugin KNOWLEDGE_BASE = new Plugin("uuid-zhishiku");

    /**
     * 智慧图问
     * <p>
     * 图片解析插件，主要功能为图片理解与识别，并对图片内容进行总结概述，输出用户可理解的自然语言文本描述（句子或段落）。
     * 识别能力包含文字OCR解析、人物识别、植物识别、商品识别、车辆识别等等。其中，文字OCR可对网页截图、办公文档、表格、题目公式、海报广告等进行
     * </p>
     *
     * @see <a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/iln1kvmpw">智慧图问</a>
     */
    public static final Plugin CHAT_OCR = new Plugin("uuid-chatocr");

    /**
     * 天气预报
     * <p>
     * 输入地址，给出当前该地址天气；输入地址+时间，给出该地址时间段内的天气
     * </p>
     *
     * @see <a href="https://cloud.baidu.com/doc/WENXINWORKSHOP/s/Glmypajea">天气</a>
     */
    public static final Plugin WEATHER_FORECAST = new Plugin("uuid-weatherforecast");

    /**
     * 百度搜索
     * <p>
     * 百度搜索插件,实时获取新闻、股票信息等
     * </p>
     */
    public static final Plugin BAIDU_SEARCH = new Plugin("uuid-websearch");

    /**
     * 网页解析
     * <p>
     * 从任何网页链接获取所需文本信息
     * </p>
     */
    public static final Plugin WEB_PAGE_READER = new Plugin("uuid-webpagereader");


}
