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

        /// L'alfiere si muove solo in diagonale
        if (rankDiff != fileDiff) {
            return false;
        }

        /// Determina la direzione del movimento
        int rowDirection = Integer.compare(endRank, startRank);  //// +1 verso il basso, -1 verso l'alto
        int colDirection = Integer.compare(endFile, startFile);    /// +1 verso destra, -1 verso sinistra

        int row = startRank + rowDirection;
        char col = (char) (startFile + colDirection);

        /// Controlla che il percorso sia libero
        while (row != endRank && col != endFile) {
            if (board.getPiece(row, col) != null) {
                return false; /// Pezzo intermedio trovato
            }
            row += rowDirection;
            col += colDirection;
        }

        /// Controllo se la destinazione è vuota o contiene un pezzo avversario
        Piece destinationPiece = board.getPiece(endRank, endFile);
        return destinationPiece == null || !destinationPiece.color().equals(this.color);
    }
}
