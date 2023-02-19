package org.example.components;

import org.example.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Sprite extends Component {
    public BufferedImage image;
    public String pictureFile;
    public int width, height;

    public int row, column, index;

    public Sprite(String pictureFile) {
        this.pictureFile = pictureFile;

        try {
            this.image = ImageIO.read(Objects.requireNonNull(Main.class.getResourceAsStream("/" + pictureFile)));
            this.width = image.getWidth();
            this.height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Sprite(BufferedImage image, String pictureFile) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pictureFile = pictureFile;
    }

    public Sprite(BufferedImage image, int row, int column, int index, String pictureFile) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.row = row;
        this.column = column;
        this.index = index;
//        this.isSubsprite = true;
        this.pictureFile = pictureFile;
    }


}
