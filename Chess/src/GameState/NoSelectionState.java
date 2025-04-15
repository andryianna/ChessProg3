package GameState;

import GUI.ChessBoard;
import GUI.Game;

public class NoSelectionState implements GameState {
    private final ChessBoard chessBoard;
    public NoSelectionState(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    @Override
    public void handleClick(Game game, int x, char y) {
        if (game.hasPiece(x, y) /*&& game.isCorrectTurn(x, y)*/) {
            game.setState(new PieceSelectedState(x, y,chessBoard));
        }
    }
}
