package Pieces;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Rook implements Piece {
    private final String color;
    private final BufferedImage image;

    public Rook(String color,String imagePath){
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
    public boolean isValidMove(int startRank, char startCol, int endRank, char endFile, Piece[][] board) {
        return true;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
