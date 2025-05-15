package GUI;
import Pieces.*;
import GameState.*;
import TurnObserver.TurnManager;

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
        state.handleClick(this, -1, 'a');
    }

    public GameState getState() {
        return this.state;
    }

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


    public TurnManager getTurnManager() {
        return turnManager;
    }

    public boolean hasPiece(int x, int y) {
        return board.hasPiece(x, y);
    }

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

        if (kingX == -1 || kingY == -1) return false; // Re non trovato

        String opponentColor = color.equals("white") ? "black" : "white";

        // Verifica se il Re è sotto attacco
        return board.isSquareAttacked(kingX, kingY, opponentColor);
    }



    public boolean hasLegalMoves(String color) {
        if (color == null) return false;

        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Piece piece = board.getPiece(fromRow, fromCol);

                if (!(piece instanceof Null) && piece.color().equals(color)) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            if (fromRow == toRow && fromCol == toCol) continue;

                            if (piece.isValidMove(fromRow, (char)(fromCol + 'a'), toRow, (char)(toCol + 'a'), board)) {
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

        Piece piece = board.getPiece(fromX, fromYIndex);
        Piece dest = board.getPiece(toX, toYIndex);


        // Controllo se si tratta di arrocco
        if (piece instanceof King && Math.abs(fromYIndex - toYIndex) == 2) {

            // Arrocco corto (da una torre)
            if (toYIndex > fromYIndex) {
                if (isCastlingPossible(fromX, fromYIndex, toX, toYIndex, piece.color())) {
                    // Esegui arrocco corto
                    Piece rook = board.getPiece(fromX, 7); // Torre destra
                    board.setPiece(toX, toYIndex, piece); // Sposta il re
                    board.setPiece(fromX, fromYIndex, new Null(turnManager)); // Elimina il re dalla posizione iniziale
                    board.setPiece(fromX, 5, rook); // Sposta la torre nella posizione corretta
                    board.setPiece(fromX, 7, new Null(turnManager)); // Elimina la torre dalla posizione iniziale

                    turnManager.nextTurn();
                    return true;
                } else {
                }
            }
            // Arrocco lungo (da una torre)
            else {
                if (isCastlingPossible(fromX, fromYIndex, toX, toYIndex, piece.color())) {
                    // Esegui arrocco lungo
                    Piece rook = board.getPiece(fromX, 0); // Torre sinistra
                    board.setPiece(toX, toYIndex, piece); // Sposta il re
                    board.setPiece(fromX, fromYIndex, new Null(turnManager)); // Elimina il re dalla posizione iniziale
                    board.setPiece(fromX, 3, rook); // Sposta la torre nella posizione corretta
                    board.setPiece(fromX, 0, new Null(turnManager)); // Elimina la torre dalla posizione iniziale

                    turnManager.nextTurn();
                    return true;
                } else {
                }
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
                Piece promoted = ChessBoardUI.showPromotionDialog(piece.color(), toX, toYIndex);
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

        turnManager.nextTurn();
        System.out.println("Mossa valida!" + (isEnPassant ? " (en passant)" : ""));
        evaluateStateAfterMove(piece.color());
        return true;
    }


    private boolean isCastlingPossible(int fromX, int fromY, int toX, int toY, String color) {
        System.out.println("Verifica arrocco per il colore: " + color + " dal " + fromY + fromX + " a " + toY + toX);

        // Verifica se il re o la torre sono già stati mossi
        if (hasMoved(fromX, fromY) || hasMoved(fromX, toY)) {
            System.out.println("Il re o la torre sono già stati mossi, arrocco non possibile.");
            return false;
        }

        // Verifica che non ci siano pezzi tra il re e la torre
        int direction = (toY > fromY) ? 1 : -1; // Determina la direzione (corto o lungo)
        for (int i = fromY + direction; i != toY; i += direction) {
            if (!(board.getPiece(fromX, i) instanceof Null)) {
                System.out.println("Ci sono pezzi tra il re e la torre.");
                return false; // Ci sono pezzi tra il re e la torre
            }
        }

        // Verifica che il re non sia sotto scacco, non passi sopra una casa sotto scacco, e non finisca in scacco
        for (int i = 0; i < 3; i++) {
            int checkRow = fromX;
            int checkCol = (direction == 1) ? fromY + i : fromY - i;
            if (isInCheck(color)) {
                System.out.println("Il re è sotto scacco durante l'arrocco.");
                return false;
            }
        }

        System.out.println("Arrocco possibile.");
        return true;
    }


    private boolean hasMoved(int row, int col) {
        Piece piece = board.getPiece(row, col);
        boolean moved = piece instanceof King ? ((King) piece).hasMoved() : piece instanceof Rook && ((Rook) piece).hasMoved();
        System.out.println("Verifica se il pezzo (" + piece.getClass().getSimpleName() + ") è stato mosso: " + moved);
        return moved;
    }



}
