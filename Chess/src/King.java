import java.awt.*;
import javax.swing.*;


public class King extends Piece {
    public King(int color, char x, int y) {
        super(color,x,y);
    }

    private Boolean hasMoved = false;

    public Boolean isInCheck() {
        return hasMoved;
    }

    @Override
    public Boolean isValidMove(char File, int Rank){
        int newRank = Math.abs(Rank - getRank());
        int newFile = Math.abs(File - getFile());
        return (newRank <=1 && newFile <=2 && (newRank + newFile > 0));
    }
}
