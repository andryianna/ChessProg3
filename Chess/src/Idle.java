public class Idle implements GameState {
    @Override
    public void handleCellClick(Game game, int x, int y) {
        Piece clickedPiece = game.getPieceAt(x,y);
        if (clickedPiece != null) {
            game.setSelectedPiece(clickedPiece,x,y);
            game.setState(new PieceSelected());
        }
    }

    @Override
    public void enterState(Game game) {
        System.out.println("Nessun pezzo selezionato");
    }
}
