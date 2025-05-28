package GUI;

import Pieces.*;
import GameState.*;
import TurnObserver.TurnManager;

import javax.swing.*;

import Strategy.*;

import java.util.Random;

public class Game {
    private final ChessBoard board;
    private GameState state;
    private final TurnManager turnManager;
    private ComputerStrategy strategy;

    public Game(ChessBoard board, TurnManager turnManager,ComputerStrategy computerStrategy) {
        this.board = board;
        this.state = new NoSelectionState(board);
        this.turnManager = turnManager;
        if (!(computerStrategy instanceof NoStrategy))
            this.strategy = computerStrategy;
    }
    /**
     * @param fromX - La riga iniziale della mossa
     * @param fromY - La colonna iniziale della mossa
     * @param toX - La riga finale della mossa
     * @param toY - La colonna finale della mossa
     * @return la validità della mossa*/
    public boolean movePiece(int fromX, char fromY, int toX, char toY) {
        int fromYIndex = fromY - 'a';
        int toYIndex = toY - 'a';

        Piece piece = board.getPiece(fromX, fromYIndex);
        Piece dest = board.getPiece(toX, toYIndex);

        // Controllo se si tratta di arrocco
        if (piece instanceof King && Math.abs(fromYIndex - toYIndex) == 2) {
            if (isCastlingPossible(fromX, fromYIndex, toX, toYIndex, piece.color()))
                // Arrocco corto
                if (toYIndex > fromYIndex)
                {
                    System.out.println("Arrocco corto da una torre");
                    // Esegui arrocco corto
                    Piece rook = board.getPiece(fromX, 7); // Torre destra
                    board.setPiece(toX, toYIndex, piece); // Sposta il re
                    board.setPiece(fromX, fromYIndex, new Pieces.Null(turnManager)); // Elimina il re dalla posizione iniziale
                    board.setPiece(fromX, 5, rook); // Sposta la torre nella posizione corretta
                    board.setPiece(fromX, 7, new Null(turnManager)); // Elimina la torre dalla posizione iniziale
                    board.setCastledShort();
                    return true;
                }
            // Arrocco lungo
                else {
                // Esegui arrocco lungo
                    Piece rook = board.getPiece(fromX, 0); // Torre sinistra
                    board.setPiece(toX, toYIndex, piece); // Sposta il re
                    board.setPiece(fromX, fromYIndex, new Null(turnManager)); // Elimina il re dalla posizione iniziale
                    board.setPiece(fromX, 3, rook); // Sposta la torre nella posizione corretta
                    board.setPiece(fromX, 0, new Null(turnManager)); // Elimina la torre dalla posizione iniziale
                    board.setCastledLong();
                    return true;

                }
        }

        if (!piece.isValidMove(fromX, fromY, toX, toY, board)) {
            System.out.println("Mossa non valida!");
            return false;
        }

        boolean isEnPassant = false;
        Piece captured = dest;

        // Gestione En Passant
        if (piece instanceof Pawn && dest instanceof Null) {
            if (Math.abs(toYIndex - fromYIndex) == 1 && Math.abs(toX - fromX) == 1) {
                if (board.isEnPassantTarget(toX, toYIndex)) {
                    isEnPassant = true;
                    int capturedRow = toX + (piece.color().equals("white") ? 1 : -1);
                    captured = board.getPiece(capturedRow, toYIndex);
                    board.setPiece(capturedRow, toYIndex, new Null(turnManager));
                }
            }
        }

        // Applica temporaneamente la mossa
        board.setPiece(toX, toYIndex, piece);
        board.setPiece(fromX, fromYIndex, new Null(turnManager));

        // Controlla se il re è sotto scacco dopo la mossa
        if (isInCheck(piece.color())) {
            board.setPiece(fromX, fromYIndex, piece);
            board.setPiece(toX, toYIndex, dest);
            if (isEnPassant) {
                int capturedRow = toX + (piece.color().equals("white") ? 1 : -1);
                board.setPiece(capturedRow, toYIndex, captured);
            }
            System.out.println("Mossa illegale: il re sarebbe sotto scacco!");
            return false;
        }

        // Gestione promozione pedone
        if (piece instanceof Pawn) {
            boolean isPromotionRank = (piece.color().equals("white") && toX == 0) || (piece.color().equals("black") && toX == 7);
            if (isPromotionRank) {
                Piece promoted = showPromotionDialog(piece.color(), toX, toYIndex);
                board.setPiece(toX, toYIndex, promoted);
                System.out.println("Pedone promosso a " + promoted.getClass().getSimpleName());
            }
        }

        // Gestione En Passant Target
        if (piece instanceof Pawn && Math.abs(toX - fromX) == 2 && fromYIndex == toYIndex) {
            int epRow = (fromX + toX) / 2;
            board.setEnPassantTarget(epRow, toYIndex);
        } else {
            board.resetEnPassantTarget();
        }

        System.out.println("Mossa valida!" + (isEnPassant ? " (en passant)" : ""));
        evaluateStateAfterMove(piece.color());
        return true;
    }
    /**
     * @param color - Il colore del pedone
     * @param rank - La riga del pedone
     * @param file - La colonna del pedone
     * @return il pedone promosso
     * Mostra una finestra di dialogo per la selezione della promozione del pedone
     * */
    private Piece showPromotionDialog(String color, int rank, int file) {
        String[] options = { "Regina", "Torre", "Alfiere", "Cavallo" };
        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Scegli il pezzo per la promozione:",
                "Promozione del Pedone",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]);
        Piece promoted = switch (choice) {
            case "Torre" -> new Rook(color, rank, (char) (file + 'a'));
            case "Alfiere" -> new Bishop(color, rank, (char) (file + 'a'));
            case "Cavallo" -> new Knight(color, rank, (char) (file + 'a'));
            default -> new Queen(color, rank, (char) (file + 'a'));
        };
        board.setPromoted(promoted);
        return promoted;
    }

    /**
     * Restituisce la validità mossa valida del pezzo selezionato
     */
    public boolean isValidMove(int fromX, char fromY, int toX, char toY,ChessBoard chessBoard) {
        int indexFromY = fromY - 'a';
        Piece piece = chessBoard.getPiece(fromX, indexFromY);
        if (piece instanceof Null) return false;
        return piece.isValidMove(fromX, fromY, toX, toY, board);
    }

    /**
     * Restituisce la validità dell arrocco selezionato*/
    private boolean isCastlingPossible(int fromX, int fromY, int toX, int toY, String color) {
        System.out.println("Verifica arrocco per il colore: " + color + " dal " + fromY + fromX + " a " + toY + toX);

        int direction = (toY > fromY) ? 1 : -1;
        int rookCol = (direction == 1) ? 7 : 0;

        // Verifica se il re o la torre sono già stati mossi
        if (hasMoved(fromX, fromY) || hasMoved(fromX, rookCol)) {
            System.out.println("Il re o la torre sono già stati mossi, arrocco non possibile.");
            return false;
        }

        // Verifica che ci sia effettivamente una torre valida nella posizione prevista
        Piece rook = board.getPiece(fromX, rookCol);
        if (!(rook instanceof Rook) || !rook.color().equals(color)) {
            System.out.println("La torre per l'arrocco non è valida.");
            return false;
        }

        // Verifica che non ci siano pezzi tra il re e la torre
        for (int i = fromY + direction; i != rookCol; i += direction) {
            if (!(board.getPiece(fromX, i) instanceof Null)) {
                System.out.println("Ci sono pezzi tra il re e la torre.");
                return false;
            }
        }

        // Verifica che il re non sia sotto scacco, non attraversi case sotto scacco e non finisca in scacco
        for (int i = 0; i <= 2; i++) {
            int checkCol = fromY + i * direction;
            if (board.isSquareAttacked(fromX, checkCol, color)) {
                System.out.println("Il re è sotto scacco durante l'arrocco.");
                return false;
            }
        }

        System.out.println("Arrocco possibile.");
        return true;
    }

    /**
     * @param currentPlayerColor - Il colore del giocatore corrente
     * Calcola lo stato del gioco dopo la mossa selezionata*/
    public void evaluateStateAfterMove(String currentPlayerColor) {
        String opponentColor = currentPlayerColor.equals("white") ? "black" : "white";

        if (isInCheck(opponentColor)) {
            if (isCheckmate(opponentColor))
                setState(new CheckmateState());
            else
                setState(new CheckState(board));
        }
        else if (isStalemate(opponentColor))
            setState(new StalemateState());
        else
            setState(new NoSelectionState(board));
        }

    /**
     * @param color - Il colore del re da controllare
     * @return vero se il re é sotto scacco, falso altrimenti*/
    public boolean isInCheck(String color) {
        int kingX = -1, kingY = -1;

        // Trova il Re del colore specificato
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece instanceof King && piece.color().equals(color)) {
                    kingX = row;
                    kingY = col;
                    break;
                }
            }
        }

        if (kingX == -1) return false; // Re non trovato

        String opponentColor = color.equals("white") ? "black" : "white";

        // Verifica se il Re è sotto attacco
        return board.isSquareAttacked(kingX, kingY, opponentColor);
    }

    /**
     * @param color Il colore da controllare
     * @return vero se il colore ha almeno una mossa legale, falso altrimenti*/
    public boolean hasLegalMoves(String color) {
        if (color == null) return false;

        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece piece = board.getPiece(fromRow, fromCol);

                if (!(piece instanceof Null) && piece.color().equals(color)) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (fromRow == toRow && fromCol == toCol) continue;

                            if (piece.isValidMove(fromRow, (char) (fromCol + 'a'), toRow, (char) (toCol + 'a'), board)) {
                                // Se il Re si sposta su una casella attaccata, scarta
                                if (piece instanceof King &&
                                        board.isSquareAttacked(toRow, toCol, color.equals("white") ? "black" : "white")) {
                                    continue;
                                }

                                // Simula la mossa
                                Piece captured = board.getPiece(toRow, toCol);
                                board.setPiece(toRow, toCol, piece);
                                board.setPiece(fromRow, fromCol, new Null(turnManager));

                                boolean stillInCheck = isInCheck(color);

                                // Ripristina
                                board.setPiece(fromRow, fromCol, piece);
                                board.setPiece(toRow, toCol, captured);

                                if (!stillInCheck) return true; // mossa legale trovata
                            }
                        }
                    }
                }
            }
        }

        return false; // nessuna mossa legale disponibile
    }

    public void makeComputerMove() {
        Move move = strategy.chooseMove(this,board,turnManager.getCurrentTurn(),turnManager);
        if(movePiece(move.fromX(),move.fromY(),move.toX(),move.toY())) {
            System.out.println("Mossa effettuata dalla strategia: " + move);
        }
    }

    /**
     * @return vero se il re o la torre sono stati mossi, falso altrimenti*/
    private boolean hasMoved(int row, int col) {
        Piece piece = board.getPiece(row, col);
        boolean moved = piece instanceof King ? ((King) piece).hasMoved() : piece instanceof Rook && ((Rook) piece).hasMoved();
        System.out.println("Verifica se il pezzo (" + piece.getClass().getSimpleName() + ") è stato mosso: " + moved);
        return moved;
    }

    /**
     * Imposta lo stato del gioco*/
    public void setState(GameState state) {
        this.state = state;
        state.handleClick(this, -1, 'a');
    }

    /**
     * @param strategy - La strategia da impostare
     * Imposta la strategia del computer*/
    protected void setStrategy(String strategy) {
        switch (strategy) {
            case "Attack" -> this.strategy = new Attack();
            case "Defense" -> this.strategy = new Defense();
            default -> this.strategy = new NoStrategy();
        }
    }
    /**
     * @return lo stato di gioco corrente*/
    public GameState getState() {
        return this.state;
    }
    /**
     * @return il turnManager usato*/
    public TurnManager getTurnManager() {
        return turnManager;
    }

    public boolean hasPiece(int x, int y) {
        return board.hasPiece(x, y);
    }
    /**
     * @return vero se scacco matto, falso altrimenti*/
    public boolean isCheckmate(String color) {
        return isInCheck(color) && !hasLegalMoves(color);
    }

    /**
     * @return vero se stallo(non é in scacco e non ha mosse legali), falso altrimenti*/
    public boolean isStalemate(String color) {
        return !isInCheck(color) && !hasLegalMoves(color);
    }
}
