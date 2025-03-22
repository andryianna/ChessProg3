package Pieces;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class Knight implements Piece {
    private final String color;
    private final BufferedImage image;

    public Knight(String color, String imagePath) {
        this.color = color;
        this.image = loadImage(imagePath);
    }

    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String getColor() {
        return color;
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, Piece[][] board) {
        return false;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
