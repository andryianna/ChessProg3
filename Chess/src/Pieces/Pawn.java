package Pieces;

import GUI.ChessBoard;

public record Pawn(String color, int rank, char file) implements Piece {

    public boolean promoted(){
        return (this.rank == 0 && this.color.equals("white")) || (this.rank == 7 && this.color.equals("black"));
    };

    @Override
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endCol - startCol);
        int direction = this.color.equals("white") ? 1 : -1;
        Piece dest = board.getPiece(endRank, endCol - 'a') == null ? null : board.getPiece(endRank, endCol - 'a');
        String destColor = dest == null ? this.color() : dest.color();
        boolean normalBehav = rankDiff == 1 && fileDiff == 0 && dest == null;
        ///movimento iniziale dei pedoni
        boolean startingBehav = ((rankDiff <= 2  && ((direction == 1 && startRank == 6) || (direction == -1 && startRank == 1))) && fileDiff == 0) && dest == null;
        ///controllo cattura
        boolean captureBehav = rankDiff == 1 && fileDiff == 1 && !destColor.equals(this.color());
        return startingBehav || captureBehav || normalBehav;
    }
}
