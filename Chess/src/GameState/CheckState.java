package GameState;

import GUI.Game;

public class CheckState implements GameState {
    @Override
    public void handleClick(Game game, int x, char y) {
        System.out.println("Sei sotto scacco! Fai una mossa valida.");
        game.setState(new NoSelectionState());
    }
}
