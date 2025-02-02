import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewGame{
    private final Piece[][] board;
    private final Game game;

    public NewGame(Piece[][] board, Game game){
        this.board = board;
        this.game = game;
        initPieces();
    }

    public void initPieces() {
        //Pezzi Bianchi
        addPiece(new Rook(0, 'a', 1));
        addPiece(new Knight(0, 'b', 1));
        addPiece(new Bishop(0, 'c', 1));
        addPiece(new Queen(0, 'd', 1));
        addPiece(new King(0, 'e', 1));
        addPiece(new Bishop(0, 'f', 1));
        addPiece(new Knight(0, 'g', 1));
        addPiece(new Rook(0, 'h', 1));
        for (char i = 'a'; i < 'i'; i++) {
            addPiece(new Pawn(0, i, 2));
        }
        //Pezzi Neri
        addPiece(new Rook(1, 'a', 8));
        addPiece(new Knight(1, 'b', 8));
        addPiece(new Bishop(1, 'c', 8));
        addPiece(new Queen(1, 'd', 8));
        addPiece(new King(1, 'e', 8));
        addPiece(new Bishop(1, 'f', 8));
        addPiece(new Knight(1, 'g', 8));
        addPiece(new Rook(1, 'h', 8));
        for (char i = 'a'; i < 'i'; i++) {
            addPiece(new Pawn(1, i, 7));
        }
    }

    private void addPiece(Piece piece) {
        int row = 8-piece.getRank();
        int col = piece.getFile() - 'a';
        board[row][col] = piece;
    }

    public void resetBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
        initPieces();
    }
}
