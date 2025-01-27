public interface GameState {
    void handleCellClick(Game game, int row, int col);
    void enterState(Game game);
}
