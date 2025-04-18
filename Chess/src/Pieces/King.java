package Pieces;

import GUI.ChessBoard;

public record King(String color, int rank, char file) implements Piece {

    @Override
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endCol - startCol);

        Piece destinationPiece = board.getPiece(endRank, endCol - 'a');

        /// si muove di una casella in tutte le direzioni
        return (rankDiff == 1 || fileDiff == 1) && (destinationPiece == null || !destinationPiece.color().equals(this.color()));

    }

}
