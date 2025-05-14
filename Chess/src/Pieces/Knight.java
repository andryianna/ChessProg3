package Pieces;

import GUI.ChessBoard;

public class Knight implements Piece {
    private final String color;

    public Knight(String color,int rank,char file) {
        this.color = color;
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
