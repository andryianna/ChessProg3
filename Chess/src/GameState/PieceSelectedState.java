package GameState;

import GUI.Game;

public class PieceSelectedState implements GameState {
    private int selectedX;
    private char selectedY;

    public PieceSelectedState(int x, char y) {
        this.selectedX = x;
        this.selectedY = y;
    }

    @Override
    public void handleClick(Game game, int x, char y) {
        if (game.isValidMove(selectedX, selectedY, x, y)) {
            game.movePiece(selectedX, selectedY, x, y);

            if (game.isCheckmate()) {
                game.setState(new CheckmateState());
            } else if (game.isCheck()) {
                game.setState(new CheckState());
            } else {
                game.setState(new NoSelectionState());
                game.getTurnManager().nextTurn(); // Cambia turno
            }
        } else {
            game.setState(new NoSelectionState()); // Deseleziona
        }
    }
}

