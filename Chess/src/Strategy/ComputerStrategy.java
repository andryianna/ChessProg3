package Strategy;

import GUI.ChessBoard;
import GUI.Game;
import Pieces.Move;
import TurnObserver.TurnManager;

public interface ComputerStrategy {
    Move chooseMove(Game game, ChessBoard board, String color, TurnManager turnManager);
}
