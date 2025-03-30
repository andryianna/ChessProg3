package GUI;
import Pieces.*;
import java.io.File;

public class ChessBoard {
    private Piece[][] board;
    private final PieceFactory pieceFactory;
    private String path = "images/" + "white" + "/"+ "pawn" + ".png";

    public ChessBoard() {
        board = new Piece[8][8];
        pieceFactory = new PieceFactory();
    }

    public boolean isPathValid(){
        File file = new File(path);
        return file.exists();
    }

    public boolean hasPiece(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) return false;
        return board[x][y] != null;
    }

    public Piece getPiece(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) return null;
        return board[x][y];
    }
    public void setPiece(int x, int y, Piece piece) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            board[x][y] = piece;
        }
    }
    public void removePiece(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            board[x][y] = null;
        }
    }
    public King getKing(String color) {
        for (int rank = 0; rank < 8; rank++) {
            for (char file = 'a'; file <= 'h'; file++) {
                Piece piece = getPiece(rank, file);
                if (piece instanceof King && piece.getColor().equals(color)) {
                    return (King) piece;
                }
            }
        }
        return null; // Se il re non viene trovato (caso anomalo)
    }
    public void setupBoard() {
        /// pezzi bianchi
        for (char file = 97; file <= 104; file++) {
            pieceFactory.createPiece("pawn", "white", 2, file, board);
        }
        pieceFactory.createPiece("rook","white",1,'a',board);
        pieceFactory.createPiece("knight","white",1,'b',board);
        pieceFactory.createPiece("bishop","white",1,'c',board);
        pieceFactory.createPiece("queen","white",1,'d',board);
        pieceFactory.createPiece("king","white",1,'e',board);
        pieceFactory.createPiece("bishop","white",1,'f',board);
        pieceFactory.createPiece("knight","white",1,'g',board);
        pieceFactory.createPiece("rook","white",1,'h',board);

        /// pezzi neri
        for (char file = 97; file <= 104; file++) {
            pieceFactory.createPiece("pawn", "black", 7, file,board);
        }
        pieceFactory.createPiece("rook","black",8,'a',board);
        pieceFactory.createPiece("knight","black",8,'b',board);
        pieceFactory.createPiece("bishop","black",8,'c',board);
        pieceFactory.createPiece("queen","black",8,'d',board);
        pieceFactory.createPiece("king","black",8,'e',board);
        pieceFactory.createPiece("bishop","black",8,'f',board);
        pieceFactory.createPiece("knight","black",8,'g',board);
        pieceFactory.createPiece("rook","black",8,'h',board);
    }
}
