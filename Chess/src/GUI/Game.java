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

    public TurnManager getTurnManager() {
        return turnManager;
    }

    public boolean hasPiece(int x, int y) {
        return board.hasPiece(x, y);
    }

    public boolean isInCheck(String color) {
        int kingRow = -1;
        int kingCol = -1;

        // Trova il re
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getClass().getSimpleName().equals("King") && piece.color().equals(color)) {
                    kingRow = row;
                    kingCol = col;
                    break;
                }
            }
        }

        if (kingRow == -1) return false; // Nessun re trovato, fallback

        // Controlla se qualche pezzo avversario può raggiungere il re
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece attacker = board.getPiece(row, col);
                if (attacker != null && !attacker.color().equals(color)) {
                    if (attacker.isValidMove(row, (char)(col + 'a'), kingRow, (char)(kingCol + 'a'), board)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean hasLegalMoves(String color) {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece piece = board.getPiece(fromRow, fromCol);
                if (piece != null && piece.color().equals(color)) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (piece.isValidMove(fromRow, (char)(fromCol + 'a'), toRow, (char)(toCol + 'a'), board)) {
                                // Simula la mossa
                                Piece captured = board.getPiece(toRow, toCol);
                                board.setPiece(toRow, toCol, piece);
                                board.setPiece(fromRow, fromCol, null);

                                boolean stillInCheck = isInCheck(color);

                                // Ripristina
                                board.setPiece(fromRow, fromCol, piece);
                                board.setPiece(toRow, toCol, captured);

                                if (!stillInCheck) return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(String color) {
        return isInCheck(color) && !hasLegalMoves(color);
    }

    public boolean isStalemate(String color) {
        return !isInCheck(color) && !hasLegalMoves(color);
    }

    public boolean isValidMove(int fromX, char fromY, int toX, char toY,ChessBoard chessBoard) {
        int indexFromY = fromY - 'a';
        Piece piece = chessBoard.getPiece(fromX, indexFromY);
        if (piece == null) return false; // Nessun pezzo da muovere
        return piece.isValidMove(fromX, fromY, toX, toY, board);
    }

    public boolean movePiece(int fromX, char fromY, int toX, char toY) {
        int fromYIndex = fromY - 'a';
        int toYIndex = toY - 'a';

        if (!isValidMove(fromX, fromY, toX, toY, board)) {
            System.out.println("Mossa non valida!");
            return false;
        }

        Piece piece = board.getPiece(fromX, fromYIndex);

        board.setPiece(toX, toYIndex, piece);
        board.setPiece(fromX, fromYIndex, null);


        System.out.println("Mossa valida!");
        turnManager.nextTurn();
        return true;
    }

}
