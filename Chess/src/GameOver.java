public class GameOver implements GameState{
    @Override
    public void handleCellClick(Game game, int row, int col) {
        System.out.println("Partita terminata. Nessuna azione consentita.");
    }

    @Override
    public void enterState(Game game) {
        System.out.println("Partita terminata. Nessuna azione consentita.");
    }
    public String getState(){
        return "GameOver";
    }
}
