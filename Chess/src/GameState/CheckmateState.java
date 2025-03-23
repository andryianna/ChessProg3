package GameState;

import GUI.Game;

public class CheckmateState implements GameState {
    @Override
    public void handleClick(Game game, int x, int y) {
        System.out.println("Scacco matto! Il gioco è finito.");
    }
}