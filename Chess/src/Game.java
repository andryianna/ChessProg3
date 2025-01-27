import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

class Game extends JPanel {
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

    public Game() {
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

        initPieces();
        renderPieces();

        //Aggiungi lista mosse
        moveLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveLog);
        add(scrollPane, BorderLayout.EAST);
    }

    private void initPieces() {
        //Pezzi Bianchi
        addPiece(new Rook(0, 'a', 1));
        addPiece(new Knight(0, 'b', 1));
        addPiece(new Bishop(0, 'c', 1));
        addPiece(new Queen(0, 'd', 1));
        addPiece(new King(0, 'e', 1));
        addPiece(new Bishop(0, 'f', 1));
        addPiece(new Knight(0, 'g', 1));
        addPiece(new Rook(0, 'h', 1));
        for (char i = 'a'; i < 'i'; i++) {
            addPiece(new Pawn(0, i, 2));
        }
        //Pezzi Neri
        addPiece(new Rook(1, 'a', 8));
        addPiece(new Knight(1, 'b', 8));
        addPiece(new Bishop(1, 'c', 8));
        addPiece(new Queen(1, 'd', 8));
        addPiece(new King(1, 'e', 8));
        addPiece(new Bishop(1, 'f', 8));
        addPiece(new Knight(1, 'g', 8));
        addPiece(new Rook(1, 'h', 8));
        for (char i = 'a'; i < 'i'; i++) {
            addPiece(new Pawn(1, i, 7));
        }
    }

    public void addPiece(Piece piece) {
        int row = 8-piece.getRank();
        int col = piece.getFile() - 'a';
        board[row][col] = piece;
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
        if (clickedPiece != null && clickedPiece.getColor() != (isWhiteTurn ? 0 : 1)) {
            JOptionPane.showMessageDialog(this, "Non é il tuo turno!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
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
        System.out.println(clickedPiece);
        String move = getMove(clickedPiece);
        board[selectedRow][selectedCol] = null;
        board[row][col] = clickedPiece;
        selectedPiece.setFile((char) ('a' + col));
        selectedPiece.setRank(8 - row);
        renderPieces();

        moveHistory.add(move);
        updateMoveLog();

        if (clickedPiece.getColor() == 0)
            notifyWhite(move);
        else
            notifyBlack(move);

        isWhiteTurn = !isWhiteTurn;
        renderPieces();
    }

    private String getMove(Piece clickedPiece) {
        String move = "";
        if (clickedPiece == null ) {
            if (selectedPiece instanceof Knight)
                move += "N";
            if (!(selectedPiece instanceof Pawn) && !(selectedPiece instanceof Knight)) {
                move = selectedPiece.getClass().getSimpleName().charAt(0) + "";
            }
            move += selectedPiece.getSquare();
        }
        else{
            if (selectedPiece instanceof Knight) {
                move += "N";
            }else if(!(selectedPiece instanceof Pawn) && !(selectedPiece instanceof Knight)){
                move += selectedPiece.getClass().getSimpleName().charAt(0) + "";
            }
            move += "x" + clickedPiece.getSquare();
        }
        return move;
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
        Image scaled = icon.getImage().getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }
}