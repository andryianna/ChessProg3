package GUI;
import Pieces.*;
import GameState.*;

public class Game {
    private final ChessBoard board;
    private GameState state;
    private final TurnManager turnManager;

    public Game() {
        this.state = new NoSelectionState();
        this.turnManager = new TurnManager();
        this.board = new ChessBoard();
    }

    public void setState(GameState state) {
        this.state = state;
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

    public boolean isCorrectTurn(int x, int y) {
        Piece piece = board.getPiece(x, y);
        if (piece == null) return false; // Nessun pezzo sulla casella

        // Controlla se il pezzo appartiene al giocatore di turno
        boolean isWhitePiece = piece.getColor().equals("white");
        return isWhitePiece == turnManager.isWhiteTurn();
    }

    public boolean isValidMove(int fromX, char fromY, int toX, char toY) {
        Piece piece = board.getPiece(fromX, fromY);
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

    public void movePiece(int fromX, char fromY, int toX, char toY) {
        if (!isCorrectTurn(fromX, fromY)) {
            System.out.println("Non è il turno corretto!");
            return;
        }

        if (!isValidMove(fromX, fromY, toX, toY)) {
            System.out.println("Mossa non valida!");
            return;
        }

        // Sposta il pezzo sulla scacchiera
        Piece piece = board.getPiece(fromX, fromY);
        board.setPiece(toX, toY, piece);
        board.setPiece(fromX, fromY, null); // Rimuove il pezzo dalla vecchia posizione

        turnManager.nextTurn(); // Passa il turno all'altro giocatore e notifica gli osservatori
    }
}
