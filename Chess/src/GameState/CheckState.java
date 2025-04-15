package GameState;

import GUI.ChessBoard;
import GUI.Game;

public class CheckState implements GameState {
    private final ChessBoard chessBoard;
    public CheckState(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }
    @Override
    public void handleClick(Game game, int x, char y) {
        System.out.println("Sei sotto scacco! Fai una mossa valida.");
        game.setState(new NoSelectionState(chessBoard));
    }
}
