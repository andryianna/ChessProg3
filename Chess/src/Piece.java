public abstract class Piece {
    private final int color;
    private int rank;
    private char file;

    public Piece(int color, char x, int y) {
        this.color = color;
        this.file = x;
        this.rank = y;
    }

    public int getColor() {
        return color;
    }

    public String getSquare(){
        return file + Integer.toString(rank);
    }

    public char getFile() {
        return file;
    }

    public int getRank() {
        return rank;
    }

    public abstract Boolean isValidMove(char File, int Rank);
}
