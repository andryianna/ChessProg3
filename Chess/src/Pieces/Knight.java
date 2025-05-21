package Pieces;

import GUI.ChessBoard;

public class Knight implements Piece {
    private final String color;

    public Knight(String color,int rank,char file) {
        this.color = color;
    }

    @Override
    public boolean canAttack(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        if (Math.abs(startRank - endRank) != Math.abs(startFile - endFile)) return false;

        int dx = Integer.compare(endRank, startRank);
        int dy = Integer.compare(endFile, startFile);

        int x = startRank + dx, y = startFile + dy;
        while (x != endRank && y != endFile) {
            if (!(board.getPiece(x, y) instanceof Null)) return false;
            x += dx;
            y += dy;
        }
        return true;
    }


    public String color() {
        return color;
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endFile - startFile);
        boolean result = (rankDiff == 2 && fileDiff == 1) || (rankDiff == 1 && fileDiff == 2);

        /// Controlla se il movimento è valido (L-shape)
        if (!result) {
            return false;
        }
        Piece destinationPiece = board.getPiece(endRank, endFile - 'a');


        /// Se la casella di destinazione è vuota o ha un pezzo avversario, la mossa è valida
        return (destinationPiece instanceof Null || !destinationPiece.color().equals(this.color()));
    }
}
