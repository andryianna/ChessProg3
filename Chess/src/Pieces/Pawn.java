package Pieces;

import GUI.ChessBoard;

public record Pawn(String color, int rank, char file) implements Piece {

    public boolean promoted(){
        return (this.rank == 0 && this.color.equals("white")) || (this.rank == 7 && this.color.equals("black"));
    }

    @Override
    public boolean isValidMove(int startRank, char startCol, int endRank, char endCol, ChessBoard board) {
        int startColIndex = startCol - 'a';
        int endColIndex = endCol - 'a';
        int direction = color.equals("white") ? -1 : 1;
        int rankDiff = endRank - startRank;
        int fileDiff = Math.abs(endColIndex - startColIndex);

        Piece dest = board.getPiece(endRank, endColIndex);

        boolean normalMove = rankDiff == direction && fileDiff == 0 && dest instanceof Null;

        boolean startingMove = (startRank == (color.equals("white") ? 6 : 1)) &&
                rankDiff == 2 * direction && fileDiff == 0 &&
                board.getPiece(startRank + direction, startColIndex) instanceof Null &&
                dest instanceof Null;

        boolean captureMove = rankDiff == direction && fileDiff == 1 &&
                !(dest instanceof Null) && !dest.color().equals(color);

        boolean enPassant = rankDiff == direction && fileDiff == 1 &&
                dest instanceof Null &&
                board.isEnPassantTarget(endRank, endColIndex);

        return normalMove || startingMove || captureMove || enPassant;
    }

}
