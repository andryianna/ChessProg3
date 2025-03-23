package GameState;

import GUI.Game;

public class NoSelectionState implements GameState {
    @Override
    public void handleClick(Game game, int x, int y) {
        if (game.hasPiece(x, y) && game.isCorrectTurn(x, y)) {
            game.setState(new PieceSelectedState(x, y));
        }
    }
}
