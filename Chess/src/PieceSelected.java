public class PieceSelected implements GameState {
    @Override
    public void handleCellClick(Game game, int row, int col) {
        Piece clickedCell = game.getSelectedPiece();
        if (clickedCell.isValidMove((char) ('a' + col), 8 - row,game.getBoard())) {
            game.movePiece(clickedCell, row, col);
            game.setState(new Idle());
        } else {
            System.out.println("Mossa non valida!");
            game.deselectPiece();
            game.setState(new Idle());
        }
    }

    @Override
    public void enterState(Game game) {
        System.out.println("Attendi mossa");
    }

    public String getState() {
        return "PieceSelected";
    }
}
