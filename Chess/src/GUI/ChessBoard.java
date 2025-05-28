package GUI;
import Pieces.*;
import TurnObserver.TurnManager;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {
    private final Piece[][] board;
    private final TurnManager turnManager;
    private int enPassantTargetRow = -1;
    private int enPassantTargetCol = -1;
    private boolean castledShort = false;
    private boolean castledLong = false;
    public void setCastledShort() {
        this.castledShort = true;
    }
    public void setCastledLong() {
        this.castledLong = true;
    }
    public void resetCastled() {
        this.castledShort = false;
        this.castledLong = false;
    }
    public boolean hasCastledShort() {
        return castledShort;
    }
    public boolean hasCastledLong() {
        return castledLong;
    }
    private Piece promoted;
    public Piece getPromoted() {
        return promoted;
    }

    public void setPromoted(Piece promoted) {
        this.promoted = promoted;
    }

    public ChessBoard(TurnManager turnManager) {
        this.turnManager = turnManager;
        this.promoted = new Null(turnManager);
        board = new Piece[8][8];
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setBoard(Piece[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }

    public void setEnPassantTarget(int row, int col) {
        this.enPassantTargetRow = row;
        this.enPassantTargetCol = col;
    }

    public boolean isEnPassantTarget(int row, int col) {
        return this.enPassantTargetRow == row && this.enPassantTargetCol == col;
    }

    public void resetEnPassantTarget() {
        this.enPassantTargetRow = -1;
        this.enPassantTargetCol = -1;
    }


    public boolean isSquareAttacked(int row, int col, String byColor) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece attacker = board[r][c];
                if (!(attacker instanceof Null) && attacker.color().equals(byColor)) {
                    if (attacker.canAttack(r, (char)(c + 'a'), row, (char)(col + 'a'), this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }




    public int piecesCount() {
        int count = 0;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (!(board[rank][file] instanceof Null)) count++;
            }
        }
        return count;
    }

    public boolean foundOtherPieceinSameRank(int x, char y, Piece piece) {
        if (piece instanceof Pawn || piece instanceof King) return false;
        for (char file = 'a'; file < 'h'; file++) {
            if (file != x && !(board[y][file] instanceof Null) && board[y][file].color().equals(piece.color())) {
                return true;
            }
        }
        return false;
    }

    public Piece findKing(String color){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] instanceof King && board[i][j].color().equals(color)) {
                    return board[i][j];
                }
            }
        }
        return new Null(turnManager);
    }

    public Piece findQueen(String color){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] instanceof Queen && board[i][j].color().equals(color)) {
                    return board[i][j];
                }
            }
        }
        return new Null(turnManager);
    }
    public boolean isUnderAttack( int targetRow, int targetCol,String color){
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece attacker = getPiece(row, col);
                if (!(attacker instanceof Null) && !attacker.color().equals(color)) {
                    if (attacker.isValidMove(row, (char) (col + 'a'), targetRow, (char) (targetCol + 'a'), this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean foundOtherPieceinSameFile(int x,char y, Piece piece) {
        if (piece instanceof Pawn || piece instanceof King) return false;
        for (int rank = 0; rank < 8; rank++) {
            if (rank != y && !(board[rank][x] instanceof Null) && board[rank][x].color().equals(piece.color())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPiece(int x, int y) {
        if (x < 0 || x >= 8 || y < 0 || y >= 8) return false;
        return !(board[x][y] instanceof Null);
    }

    public Piece getPiece(int x, int y) {
        if (x + 1 < 0 || x > 8 || y + 1 < 0 || y > 8) return new Null(turnManager);
        return board[x][y];
    }
    public void setPiece(int x, int y, Piece piece) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            board[x][y] = piece;
        }
    }

    public void setupBoard() {
        // pezzi neri
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

        // pezzi bianchi
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

        // riempi il resto con pezzi Null
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) {
                    board[i][j] = new Null(turnManager);
                }
            }

        }
    }
}
