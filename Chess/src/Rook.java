public class Rook extends Piece {
    public Rook(int color, char x, int y) {
        super(color,x,y);
    }

    private Boolean hasMoved = false;

    protected Boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public Boolean isValidMove(char File, int Rank) {
        return null;
    }


}
