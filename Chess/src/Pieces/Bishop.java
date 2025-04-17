package Pieces;

import GUI.ChessBoard;

public record Bishop(String color, int rank, char file) implements Piece {
    @Override
    public String color() {
        return color;
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endFile - startFile);
        /// Movimento in diagonale
        if (rankDiff != fileDiff) return false;

        int rankDir = Integer.compare(endRank, startRank);
        int fileDir = Integer.compare(endFile, startFile);
        Piece destinationPiece = board.getPiece(endRank, endFile - 'a');
        if (!isPathClear(board,startRank,startFile,rankDir,fileDir,endRank,endFile)) return false;
        if (destinationPiece != null && destinationPiece.color().equals(this.color())) return false;

        return true;
    }


    private boolean isPathClear(ChessBoard board, int startRank, char startFile, int rankDir, int fileDir, int endRank, char endFile) {
        int rank = startRank + rankDir;
        int file = startFile - 'a' + fileDir;
        int targetFile = endFile - 'a';

        while (rank != endRank || file != targetFile) {
            if (board.getPiece(rank, file) != null) {
                return false;
            }
            rank += rankDir;
            file += fileDir;
        }

        return true;
    }

}
