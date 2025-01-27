public class Queen extends Piece {
    public Queen(int color, char x, int y) {
        super(color,x,y);
    }

    @Override
    public Boolean isValidMove(char File, int Rank,Piece[][] board) {
        /*Rook rook;
        Bishop bishop;
        return rook.isValidMove(File,Rank,board) || bishop.isValidMove(File,Rank,board);*/
        return true;
    }
}
