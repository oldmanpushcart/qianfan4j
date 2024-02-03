package io.github.ompc.erniebot4j.test.image;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageDisplay extends JFrame {

    private final BufferedImage image;

    public ImageDisplay(BufferedImage image) {
        super("Image Display");
        this.image = image;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void display() {
        getContentPane().add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, this);
            }

        });
        pack();
        setVisible(true);
    }

}
