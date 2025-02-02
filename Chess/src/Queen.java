public class Queen extends Piece {
    public Queen(int color, char x, int y) {
        super(color,x,y);
    }

    @Override
    public Boolean isValidMove(char File, int Rank,Piece[][] board) {
        int fileDiff = Math.abs(File - getFile());
        int rankDiff = Math.abs(Rank - getRank());
        if (fileDiff == rankDiff) {
            return new Bishop(getColor(),getFile(),getRank()).isPathClear(File,Rank,board);
        }
        if (File == getFile() || getRank() == getFile()) {
            return new Rook(getColor(),getFile(),getRank()).isPathClear(File,Rank,board);
        }
        return false;
    }
}
