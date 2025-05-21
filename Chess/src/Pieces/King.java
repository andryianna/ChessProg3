package Pieces;

import GUI.ChessBoard;

public class King implements Piece {
    private final String color;
    private final int rank;
    private final char file;
    private boolean hasMoved = false;

    public King(String color, int rank, char file) {
        this.color = color;
        this.rank = rank;
        this.file = file;
    }

    public String color() {
        return color;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved(boolean moved) {
        this.hasMoved = moved;
    }

    @Override
    public boolean canAttack(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        return Math.abs(startRank - endRank) <= 1 && Math.abs(startFile - endFile) <= 1;
    }

    @Override
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endCol - startCol);

        Piece destinationPiece = board.getPiece(endRank, endCol - 'a');
        if (rankDiff > 1 || fileDiff > 1) return false;

        return destinationPiece instanceof Null || !destinationPiece.color().equals(this.color());
    }

    public int getRank() {
        return this.rank;
    }

    public char getFile() {
        return this.file;
    }
}
