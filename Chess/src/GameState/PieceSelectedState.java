package GameState;

import GUI.ChessBoard;
import GUI.Game;

public class PieceSelectedState implements GameState {
    private final int selectedX;
    private final char selectedY;
    private final ChessBoard chessBoard;


    public PieceSelectedState(int x, char y, ChessBoard chessBoard) {
        this.selectedX = x;
        this.selectedY = y;
        this.chessBoard = chessBoard;
    }

    @Override
    public void handleClick(Game game, int x, char y) {
        if (game.isValidMove(selectedX, selectedY, x, y,chessBoard)) {
            game.movePiece(selectedX, selectedY, x, y);
            game.setState(new NoSelectionState(chessBoard));
            game.getTurnManager().nextTurn();// Deseleziona
        }
    }
}

