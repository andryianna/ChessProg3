package Pieces;

import GUI.ChessBoard;

public class Rook implements Piece {
    private final String color;
    private final int rank;
    private final char file;
    private boolean hasMoved = false;

    public Rook(String color, int rank, char file) {
        this.color = color;
        this.rank = rank;
        this.file = file;
    }
    @Override
    public boolean canAttack(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        if (startRank != endRank && startFile != endFile) return false;

        int dx = Integer.compare(endRank, startRank);
        int dy = Integer.compare(endFile, startFile);

        int x = startRank + dx, y = startFile + dy;
        while (x != endRank || y != endFile) {
            if (!(board.getPiece(x, y) instanceof Null)) return false;
            x += dx;
            y += dy;
        }
        return true;
    }

    @Override
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
    public boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board) {
        boolean sameRank = startRank == endRank;
        boolean sameFile = startFile == endFile;

        if (!sameFile && !sameRank) {
            return false;
        }

        int rankDir = Integer.compare(endRank, startRank);
        int fileDir = Integer.compare(endFile, startFile);

        if (!isPathClear(board, startRank, startFile, rankDir, fileDir, endRank, endFile)) return false;

        Piece destinationPiece = board.getPiece(endRank, endFile - 'a');
        return destinationPiece instanceof Null || !destinationPiece.color().equals(this.color());
    }

    private boolean isPathClear(ChessBoard board, int startRank, char startFile, int rankDir, int fileDir, int endRank, char endFile) {
        int rank = startRank + rankDir;
        int file = startFile - 'a' + fileDir;
        int targetFile = endFile - 'a';

        while (rank != endRank || file != targetFile) {
            if (!(board.getPiece(rank, file) instanceof Null)) {
                return false;
            }
            rank += rankDir;
            file += fileDir;
        }

        return true;
    }
}
