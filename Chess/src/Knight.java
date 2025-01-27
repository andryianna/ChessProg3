public class Knight extends Piece {
    public Knight(int color, char x, int y) {
        super(color,x,y);
    }

    @Override
    public Boolean isValidMove(char File, int Rank, Piece[][] board) {
        int fileDiff = Math.abs(File - getFile());
        int rankDiff = Math.abs(Rank - getRank());

        if ((fileDiff == 2 && rankDiff == 1) || (fileDiff == 1 && rankDiff == 2)){
            Piece piece = board[Rank - 1][File - 'a'];
            return piece == null || piece.getColor() != this.getColor();
        }
        return false;
    }


}
