package Pieces;

import GUI.ChessBoard;

public record Queen(String color, int rank, char file) implements Piece {

    public boolean canAttack(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        Rook tempRook = new Rook(color(),startRank,startFile);
        Bishop tempBishop = new Bishop(color(),startRank,startFile);
        return tempRook.canAttack(startRank, startFile, endRank, endFile, board) ||
                tempBishop.canAttack(startRank, startFile, endRank, endFile, board);
    }

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        boolean sameRank = startRank == endRank;
        boolean sameFile = startFile == endFile;
        int rankDiff = Math.abs(endRank - startRank);
        int fileDiff = Math.abs(endFile - startFile);
        // Movimento alfiere
        if (rankDiff == fileDiff) {

            return new Bishop(color,rank,file).isValidMove(startRank,startFile,endRank,endFile,board);
        }

        // Movimento torre
        if (sameRank || sameFile){
            return new Rook(color,rank,file).isValidMove(startRank,startFile,endRank,endFile,board);
        }
        return false;
    }
}
