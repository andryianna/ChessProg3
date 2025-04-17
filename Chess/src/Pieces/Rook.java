package Pieces;

import GUI.ChessBoard;

public record Rook(String color, int rank, char file) implements Piece {

    @Override
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        boolean sameRank = startRank == endRank;
        boolean sameFile = startFile == endFile;

        /// Movimento in orizzontale o in verticale
        if (!sameFile && !sameRank) {
            return false;
        }

        int rankDir = Integer.compare(endRank, startRank);
        int fileDir = Integer.compare(endFile, startFile);

        /// Controlla se il percorso é pulito
        if (!isPathClear(board,startRank,startFile,rankDir,fileDir,endRank,endFile)) return false;

        /// Se la casella di destinazione é vuota o se é del colore avversario restituisce vero
        Piece destinationPiece = board.getPiece(endRank, endFile - 'a');
        if (destinationPiece == null)
            return true;
        if (!destinationPiece.color().equals(this.color()))
            return true;
        return false;
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
