package GameState;

import GUI.Game;

public class PieceSelectedState implements GameState {
    private int selectedX, selectedY;

    public PieceSelectedState(int x, int y) {
        this.selectedX = x;
        this.selectedY = y;
    }

    @Override
    public void handleClick(Game game, int x, int y) {
        if (game.isValidMove(selectedX, selectedY, x, y)) {
            game.movePiece(selectedX, selectedY, x, y);

            if (game.isCheckmate()) {
                game.setState(new CheckmateState());
            } else if (game.isCheck()) {
                game.setState(new CheckState());
            } else {
                game.setState(new NoPieceSelectedState());
                game.getTurnManager().nextTurn(); // Cambia turno
            }
        } else {
            game.setState(new NoPieceSelectedState()); // Deseleziona
        }
    }
}

