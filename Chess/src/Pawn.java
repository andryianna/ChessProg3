public class Pawn extends Piece {
    public Pawn(int color, char x, int y) {
        super(color,x,y);
    }

    @Override
    public Boolean isValidMove(char File, int Rank, Piece[][] board) {
        try {
            int fileIndex = File - 'a';
            System.out.println(isIstanceofSomething(fileIndex, Rank, board));
            if (getFile() - File == 0 && Math.abs(getRank() - Rank) == 1 && isIstanceofSomething(fileIndex,Rank,board)) {
                return true;
            }

            if ((getColor() == 0 && getRank() == 2) || (getColor() == 1 && getRank() == 7) && (
                    !(board[fileIndex][Rank] instanceof Pawn) || !(board[fileIndex][Rank] instanceof Knight) || !(board[fileIndex][Rank] instanceof Queen) || !(board[fileIndex][Rank] instanceof King) || !(board[fileIndex][Rank] instanceof Bishop ))) {
                if (Math.abs(getRank() - Rank) == 2 && Math.abs(getFile() - File) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean isIstanceofSomething(int file, int rank, Piece[][] piece) {
        return piece[file][rank] instanceof Pawn || piece[file][rank] instanceof Knight || piece[file][rank] instanceof Queen || piece[file][rank] instanceof King || piece[file][rank] instanceof Bishop || piece[file][rank] instanceof Rook;
    }
}
