package Pieces;

import GUI.ChessBoard;

public interface Piece {
    String color();
    /**
     * @param board - La scacchiera in uso
     * @param startRank - Riga di partenza
     * @param startFile - Colonna di partenza
     * @param endRank - Riga di arrivo
     * @param endFile - Colonna di arrivo
     *
     * @return -Vero se la mossa é valida, falso se non é valida a seconda del tipo di pezzo
     *
     * */
    boolean isValidMove(int startRank, char startFile, int endRank, char endFile, ChessBoard board);
}
