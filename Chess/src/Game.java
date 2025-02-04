import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

class Game extends JPanel {
    private final NewGame newGame;
    private GameState currentGameState;
    private final JButton[][] square = new JButton[8][8];
    private final Piece[][] board = new Piece[8][8];
    private Piece selectedPiece = null;
    private int selectedCol = -1;
    private int selectedRow = -1;
    private final List<String> moveHistory = new ArrayList<>();
    private final JTextArea moveLog = new JTextArea(10,30);
    private final List<TurnObserver> observers = new ArrayList<>();
    private boolean isWhiteTurn = true;
    private SaveGame saveGame;

    public Game() {
        this.newGame = new NewGame(board,this);
        this.currentGameState = new Idle();
        currentGameState.enterState(this);
        // Layout della scacchiera
        setLayout(new BorderLayout());

        // Pannello della scacchiera
        JPanel chessBoard = new JPanel();
        chessBoard.setLayout(new GridLayout(8, 8));

        // Colori per le caselle
        Color lightColor = new Color(240, 217, 181);
        Color darkColor = new Color(181, 136, 99);

        // Genera la scacchiera
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton cell = new JButton();
                cell.setFocusPainted(false);
                cell.setBorderPainted(false);
                if ((row + col) % 2 == 0) {
                    cell.setBackground(lightColor);
                } else {
                    cell.setBackground(darkColor);
                }
                final int currentRow = row;
                final int currentCol = col;
                cell.addActionListener(e -> handleCellClick(currentRow, currentCol));
                square[row][col] = cell;
                chessBoard.add(cell);
            }
        }

        // Aggiungi la scacchiera al pannello principale
        add(chessBoard, BorderLayout.CENTER);

        newGame.initPieces();
        renderPieces();

        //Aggiungi lista mosse
        moveLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveLog);
        add(scrollPane, BorderLayout.EAST);

        saveGame = new SaveGame("partita.pgn");
    }


    public void renderPieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                square[row][col].removeAll();
                Piece piece = board[row][col];
                if (piece != null) {
                    JLabel pieceLabel = new JLabel(getPieceIcon(piece));
                    square[row][col].add(pieceLabel);
                }
            }
        }
        revalidate();
        repaint();
    }

    private void handleCellClick(int row, int col) {
        Piece clickedPiece = board[row][col];
        if (!currentGameState.getState().equals("PieceSelected")) {
            if (clickedPiece != null && clickedPiece.getColor() != (isWhiteTurn ? 0 : 1)) {
                JOptionPane.showMessageDialog(this, "Non é il tuo turno!", "Errore", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        currentGameState.handleCellClick(this,row,col);
    }

    public void setState(GameState state) {
        this.currentGameState = state;
        currentGameState.enterState(this);
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    public Piece getPieceAt(int row, int col) {
        return board[row][col];
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void setSelectedPiece(Piece piece, int row, int col) {
        this.selectedPiece = piece;
        this.selectedRow = row;
        this.selectedCol = col;
    }

    public void deselectPiece() {
        this.selectedPiece = null;
        this.selectedRow = -1;
        this.selectedCol = -1;
    }

    public void movePiece(Piece clickedPiece, int row, int col) {
        String move = getMove(clickedPiece,row,col);
        board[selectedRow][selectedCol] = null;
        board[row][col] = clickedPiece;
        clickedPiece.setFile((char) ('a' + col));
        clickedPiece.setRank(row);
        renderPieces();

        moveHistory.add(move);
        System.out.println(moveHistory.size());
        updateMoveLog();
        saveGame.addMove(move);

        if (clickedPiece.getColor() == 0)
            notifyWhite(move);
        else
            notifyBlack(move);

        isWhiteTurn = !isWhiteTurn;
        renderPieces();
    }

    private String getMove(Piece piece,int row,int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        String move = "";

        // Se il pezzo non è un pedone, aggiunge l'iniziale del pezzo
        if (!(piece instanceof Pawn) && !(piece instanceof Knight)) {
            move += piece.getClass().getSimpleName().charAt(0);
        }
        if (piece instanceof Knight)
            move += "N";

        // Controlla se la mossa è una cattura
        if (board[row][col] != null) {
            if (piece instanceof Pawn) {
                move += piece.getFile(); // Indica la colonna del pedone in caso di cattura
            }
            move += "x";
        }

        move += file + "" + rank;

        // Controlla se il re avversario è sotto scacco o scacco matto
        King opponentKing = findKing(isWhiteTurn ? 1 : 0);
        if (opponentKing.isInCheck(board)) {
            move += "+";
            if (opponentKing.CheckMate(board)) {
                move += "#";
            }
        }

        return move;
    }

    private King findKing(int color) {
        for (Piece[] row : board) {
            for (Piece p : row) {
                if (p instanceof King && p.getColor() == color) {
                    return (King) p;
                }
            }
        }
        return null;
    }

    private void updateMoveLog(){
        moveLog.setText(String.join("\n", moveHistory));
    }

    public void addObserver(TurnObserver observer) {
        observers.add(observer);
    }

    private void notifyWhite(String move) {
        for (TurnObserver observer : observers) {
            observer.onWhiteTurn(move);
        }
    }

    private void notifyBlack(String move) {
        for (TurnObserver observer : observers) {
            observer.onBlackTurn(move);
        }
    }

    private void resetCellColor(int row, int col) {
        Color light = new Color(240, 217, 181);
        Color dark = new Color(181, 136, 99);
        square[row][col].setBackground((row + col) % 2 == 0 ? light : dark);
    }


    private ImageIcon getPieceIcon(Piece piece) {
        String color = piece.getColor() == 0 ? "white" : "black";
        String type = piece.getClass().getSimpleName().toLowerCase();
        String path = "res/images/" + color + "/" + type + ".png";

        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }
}