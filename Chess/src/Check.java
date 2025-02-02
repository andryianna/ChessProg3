public class Check implements GameState{
    @Override
    public void enterState(Game game) {
        System.out.println("Scacco");
    }

    @Override
    public void handleCellClick(Game game, int row, int col) {
        game.movePiece(game.getBoard()[row][col],row,col);
    }

    @Override
    public String getState() {
        return "Check";
    }
}
