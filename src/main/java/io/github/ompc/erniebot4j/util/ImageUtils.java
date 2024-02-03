package io.github.ompc.erniebot4j.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    public static BufferedImage base64ToImage(String base64) throws IOException {
        final var bytes = Base64.getDecoder().decode(base64);
        try (final var input = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(input);
        }
    }

    public static String imageToBase64(BufferedImage image) throws IOException {
        try (final var output = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", output);
            return Base64.getEncoder().encodeToString(output.toByteArray());
        }
    }

}
