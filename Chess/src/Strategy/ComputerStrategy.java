package Strategy;

import GUI.ChessBoard;
import GUI.Game;
import TurnObserver.TurnManager;

public interface ComputerStrategy {
    boolean chooseMove(Game game, ChessBoard board, String color, TurnManager turnManager);
}
