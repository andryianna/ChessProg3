package GUI;

import GameState.*;
import TurnObserver.*;

public class Game {
    private GameState state;
    private TurnManager turnManager;

    public Game() {
        this.state = new NoSelectionState();
        turnManager = new TurnManager();
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void handleClick(int x, int y) {
        state.handleClick(this, x, y);
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }
    public boolean hasPiece(int x, int y) {
        return
    }

/*    public void makeMove(int fromX, int fromY, int toX, int toY) {
        if (isValidMove(fromX, fromY, toX, toY)) {
            movePiece(fromX, fromY, toX, toY);
            turnManager.nextTurn();  // Cambia turno e notifica
        }
    }
 */
}
