package io.github.oldmanpushcart.qianfan4j;

import java.io.IOException;
import java.util.Properties;

public class Constants {

    private final static Properties properties = new Properties();

    static {
        try {
            properties.load(Constants.class.getResourceAsStream("/qianfan4j-meta.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日志
     */
    public static final String LOGGER_NAME = "qianfan4j";

    /**
     * 版本
     */
    public static final String VERSION = properties.getProperty("version");

}
