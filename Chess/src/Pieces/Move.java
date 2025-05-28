package Pieces;


import GUI.ChessBoard;
import GUI.Game;
import GameState.*;


public record Move(int fromX, char fromY, int toX, char toY) {
    @Override
    public String toString() {
        return  "" + fromX + fromY + toX + toY;
    }

    public String toAlgebraicNotation(Game game, ChessBoard board, Piece piece, boolean capture, Piece promotion) {
        String move = "";
        if (board.hasCastledLong() || board.hasCastledShort()) {
            if (board.hasCastledShort())
                move += "O-O";
            else
                move += "O-O-O";
        } else {
            if (piece instanceof Pawn)
                move += "";
            else if (piece instanceof Knight)
                move += "N";
            else
                move += piece.toString().charAt(0);
            System.out.println("Mossa: " + move);
            // Se trovo un altro pezzo nella stessa riga o colonna aggiungo riga o colonna di partenza
            if (board.foundOtherPieceinSameFile(fromX, fromY, piece))
                move += fromX;
            if (board.foundOtherPieceinSameRank(fromX, fromY, piece))
                move += fromY;
            // Se la scacchiera ha un pezzo in meno rispetto a prima, la mossa é una cattura
            if (capture)
                move += "x";
                // Altrimenti prendo solo casella di destinazione
            else
                move += toX + toY;
            if (piece instanceof Pawn && ((Pawn) piece).promoted()) move += "=" + promotion.toString().charAt(0);
        }
        // Se il re avversario viene messo sotto scacco aggiungi il +
        if (game.getState() instanceof CheckState)
            move += "+";

        // Se il gioco finisce con scacco matto aggiungi # invece di +
        if (game.getState() instanceof CheckmateState)
            move += "#";

        return move;
    }
}