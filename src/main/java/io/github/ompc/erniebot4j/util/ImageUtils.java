package io.github.ompc.erniebot4j.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 图片工具类
 */
public class ImageUtils {

    /**
     * base64转图片
     *
     * @param base64 base64
     * @return 图片
     * @throws IOException 转换失败
     */
    public static BufferedImage base64ToImage(String base64) throws IOException {
        final var bytes = Base64.getDecoder().decode(base64);
        try (final var input = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(input);
        }
    }

    /**
     * 图片转base64
     *
     * @param image 图片
     * @return base64
     * @throws IOException 转换失败
     */
    public static String imageToBase64(BufferedImage image) throws IOException {
        try (final var output = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        }
    }

}
