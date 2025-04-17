package Pieces;

import GUI.ChessBoard;

public record Pawn(String color, int rank, char file) implements Piece {

    @Override
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endCol - startCol);
        int direction = this.color.equals("white") ? 1 : -1;
        Piece dest = board.getPiece(endRank, endCol - 'a');
        ///movimento standard dei pedoni
        boolean normalBehav = rankDiff <= 2 || (direction == 1 && startRank == 6) || (direction == -1 && startRank == 1);
        ///controllo cattura
        boolean captureBehav = rankDiff == 1 && fileDiff == 1 && !dest.color().equals(this.color());
        return normalBehav || captureBehav;
    }
}
