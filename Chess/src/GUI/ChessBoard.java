package GUI;
import Pieces.*;

public class ChessBoard {
    private final Piece[][] board;

    public ChessBoard() {
        board = new Piece[8][8];
    }


    public int piecesCount() {
        int count = 0;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (board[rank][file] != null) count++;
            }
        }
        return count;
    }

    public boolean foundOtherPieceinSameRank(int x, char y, Piece piece) {
        if (piece instanceof Pawn || piece instanceof King) return false;
        for (char file = 'a'; file < 'h'; file++) {
            if (file != x && board[y][file] != null && board[y][file].color().equals(piece.color())) {
                return true;
            }
        }
        return false;
    }

    public boolean foundOtherPieceinSameFile(int x,char y, Piece piece) {
        if (piece instanceof Pawn || piece instanceof King) return false;
        for (int rank = 0; rank < 8; rank++) {
            if (rank != y && board[rank][x] != null && board[rank][x].color().equals(piece.color())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPiece(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) return false;
        return board[x][y] != null;
    }

    public Piece getPiece(int x, int y) {
        if (x + 1 < 0 || x > 8 || y + 1 < 0 || y > 8) return null;
        return board[x][y];
    }
    public void setPiece(int x, int y, Piece piece) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            board[x][y] = piece;
        }
    }
    public void setupBoard() {
        /// pezzi bianchi
        for (char file = 97; file <= 104; file++) {
            PieceFactory.createPiece("pawn", "black", 2, file, board);
        }
        PieceFactory.createPiece("rook","black",1,'a',board);
        PieceFactory.createPiece("knight","black",1,'b',board);
        PieceFactory.createPiece("bishop","black",1,'c',board);
        PieceFactory.createPiece("queen","black",1,'d',board);
        PieceFactory.createPiece("king","black",1,'e',board);
        PieceFactory.createPiece("bishop","black",1,'f',board);
        PieceFactory.createPiece("knight","black",1,'g',board);
        PieceFactory.createPiece("rook","black",1,'h',board);
        PieceFactory.createPiece("pawn", "white", 3, 'g',board);
        PieceFactory.createPiece("pawn", "white", 3, 'f',board);

        /// pezzi neri
        for (char file = 97; file <= 104; file++) {
            PieceFactory.createPiece("pawn", "white", 7, file,board);
        }
        PieceFactory.createPiece("rook","white",8,'a',board);
        PieceFactory.createPiece("knight","white",8,'b',board);
        PieceFactory.createPiece("bishop","white",8,'c',board);
        PieceFactory.createPiece("queen","white",8,'d',board);
        PieceFactory.createPiece("king","white",8,'e',board);
        PieceFactory.createPiece("bishop","white",8,'f',board);
        PieceFactory.createPiece("knight","white",8,'g',board);
        PieceFactory.createPiece("rook","white",8,'h',board);
    }
}
