package GUI;
import Pieces.*;
import GameState.*;

public class Game {
    private final ChessBoard board;
    private GameState state;
    private final TurnManager turnManager;


    public Game(ChessBoard board, TurnManager turnmanager) {
        this.board = board;
        this.state = new NoSelectionState(board);
        this.turnManager = turnmanager;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return this.state;
    }

    public void handleClick(int x, char y) {
        state.handleClick(this, x, y);
    }

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public boolean hasPiece(int x, int y) {
        return board.hasPiece(x, y);
    }

    /*public boolean isCorrectTurn(int x, int y) {
        Piece piece = board.getPiece(x, y);
        if (piece == null) return false; // Nessun pezzo sulla casella

        // Controlla se il pezzo appartiene al giocatore di turno
        boolean isWhitePiece = piece.getColor().equals("white");
        return isWhitePiece == turnManager.isWhiteTurn();
    }*/

    public boolean isValidMove(int fromX, char fromY, int toX, char toY,ChessBoard chessBoard) {
        int indexFromY = fromY - 'a';
        Piece piece = chessBoard.getPiece(fromX, indexFromY);
        if (piece == null) return false; // Nessun pezzo da muovere
        return piece.isValidMove(fromX, fromY, toX, toY, board);
    }

    public boolean isCheck() {
        King king = board.getKing(turnManager.getCurrentTurn());
        return king != null && king.isKingInCheck(turnManager.getCurrentTurn(), board);
    }

    public boolean isCheckmate() {
        King king = board.getKing(turnManager.getCurrentTurn());
        return king != null && king.isCheckmate(turnManager.getCurrentTurn(),board);
    }

    public boolean movePiece(int fromX, char fromY, int toX, char toY) {
        int fromYIndex = fromY - 'a';
        int toYIndex = toY - 'a';

        System.out.printf("Tentativo di muovere da %c%d a %c%d\n", fromY, fromX, toY, toX);

        if (!isValidMove(fromX, fromY, toX, toY, board)) {
            System.out.println("Mossa non valida!");
            return false;
        }

        Piece piece = board.getPiece(fromX, fromYIndex);
        System.out.println("Pezzo selezionato: " + (piece != null ? piece.getClass().getSimpleName() : "null"));

        board.setPiece(toX, toYIndex, piece);
        board.setPiece(fromX, fromYIndex, null);

        // Stampa lo stato della board dopo la mossa
        System.out.println("Stato della board dopo la mossa:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece p = board.getPiece(i, j);
                System.out.print((p != null ? p.getClass().getSimpleName().charAt(0) : '.') + " ");
            }
            System.out.println();
        }

        System.out.println("Mossa valida!");
        turnManager.nextTurn();
        return true;
    }

}
