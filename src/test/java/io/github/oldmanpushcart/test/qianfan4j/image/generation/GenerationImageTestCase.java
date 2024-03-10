package io.github.oldmanpushcart.test.qianfan4j.image.generation;

import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageModel;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageOptions;
import io.github.oldmanpushcart.qianfan4j.image.generation.GenerationImageRequest;
import io.github.oldmanpushcart.test.qianfan4j.LoadingEnv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GenerationImageTestCase implements LoadingEnv {

    @Test
    public void test$image$generation() throws IOException {

        final var request = GenerationImageRequest.newBuilder()
                .model(GenerationImageModel.STABLE_DIFFUSION_XL)
                .prompt("猫")
                .negative("白色")
                .option(GenerationImageOptions.NUMBERS, 2)
                .option(GenerationImageOptions.SIZE, GenerationImageRequest.Size.S_1024_1024)
                .build();

        final var response = client.generationImage(request)
                .async()
                .join();

        final var images = response.images();
        Assertions.assertEquals(2, images.size());

        final var imagesDir = new File("gen-images");
        int index = 0;
        for(final var image : images) {
            final var file = new File(imagesDir, "gen-image-%s-%03d.png".formatted(response.uuid(), index++));
            Assertions.assertTrue(file.mkdirs());
            ImageIO.write(image, "png", file);
        }

    }

}
