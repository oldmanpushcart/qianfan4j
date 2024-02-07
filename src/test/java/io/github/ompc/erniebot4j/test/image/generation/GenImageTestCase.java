package io.github.ompc.erniebot4j.test.image.generation;

import io.github.ompc.erniebot4j.ErnieBotClient;
import io.github.ompc.erniebot4j.image.generation.GenImageModel;
import io.github.ompc.erniebot4j.image.generation.GenImageOptions;
import io.github.ompc.erniebot4j.image.generation.GenImageRequest;
import io.github.ompc.erniebot4j.test.LoadingProperties;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenImageTestCase implements LoadingProperties {

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    @AfterClass
    public static void clean() {
        executor.shutdown();
    }

    @Test
    public void test$image$gen() throws IOException {

        final var client = new ErnieBotClient.Builder()
                .refresher(refresher)
                .executor(executor)
                .build();

        final var request = new GenImageRequest.Builder()
                .model(GenImageModel.STABLE_DIFFUSION_XL)
                .prompt("猫")
                .negative("白色")
                .option(GenImageOptions.NUMBERS, 2)
                .option(GenImageOptions.SIZE, GenImageRequest.Size.S_1024_1024)
                .build();

        final var response = client.image().generation(request)
                .async()
                .join();

        BufferedImage[] images = response.images();
        Assert.assertEquals(2, images.length);

        final var imagesDir = new File("gen-images");

        for (int index = 0; index < images.length; index++) {
            final var image = images[index];
            final var file = new File(imagesDir, "gen-image-%s-%03d.png".formatted(response.id(), index));
            Assert.assertTrue(file.mkdirs());
            ImageIO.write(image, "png", file);
            System.out.println(file);
        }

    }

}
