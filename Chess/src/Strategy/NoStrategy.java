package Strategy;

import GUI.ChessBoard;
import GUI.Game;
import Pieces.Move;
import TurnObserver.TurnManager;

public class NoStrategy implements ComputerStrategy{
    @Override
    public Move chooseMove(Game game, ChessBoard board, String color, TurnManager turnManager) {
        return null;
    }
}
