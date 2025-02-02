public class Pawn extends Piece {
    public Pawn(int color, char x, int y) {
        super(color,x,y);
    }

    @Override
    public Boolean isValidMove(char File, int Rank,Piece[][] board) {
        int fileDiff = File - getFile();
        int rankDiff = Rank - getRank();
        int fileInd = File - 'a';
        int dir = getColor() == 0 ? 1 : -1;

        //Muovi di una casella
        if (Rank < 1 || Rank > 8 || File < 'a' || File > 'h') {
                return false;
        }

        if (fileDiff == 0 && rankDiff == dir) {
            if (board [Rank][fileInd] == null || board [Rank][fileInd] != null) {
                return true;
            }
        }
        //Muovi di due caselle
        if ((fileDiff == 0 && rankDiff == 2 * dir) && (getColor() == 1 && getRank() == 7)
                || (getColor() == 0 && getRank() == 2)) {
            if (board[Rank - 1][fileInd] == null &&
                    board[getRank() - 1 + dir][getFile() - 'a'] == null)
                return true;
        }

        //Cattura
        if (Math.abs(fileDiff) == 1 && rankDiff == dir) {
            Piece piece = board[Rank][fileInd];
            if (piece != null && piece.getColor() != getColor()) {}
                return true;
        }

        return false;
    }
}
