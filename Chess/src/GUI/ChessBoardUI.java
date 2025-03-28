package GUI;

import javax.swing.*;
import java.awt.*;
import TurnObserver.*;
import Pieces.Piece;

public class ChessBoardUI extends JFrame implements TurnObserver {
    private final JLabel turnLabel;
    private final JButton[][] boardButtons;
    private final TurnManager turnManager;
    static ChessBoard chessBoard = new ChessBoard();

    public ChessBoardUI(TurnManager turnManager) {
        this.turnManager = turnManager;
        this.turnManager.attach(this);

        setTitle("Scacchi");
        setSize(600, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label per il turno
        turnLabel = new JLabel("Turno del Bianco", SwingConstants.CENTER);
        add(turnLabel, BorderLayout.NORTH);

        // Scacchiera
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        boardButtons = new JButton[8][8];
        setupBoard();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                boardButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    @Override
    public void onTurnChanged(boolean isWhiteTurn) {
        turnLabel.setText(isWhiteTurn ? "Turno del Bianco" : "Turno del Nero");
    }

    private void setupBoard() {
        chessBoard.setupBoard(); // Posiziona i pezzi sulla scacchiera
        updateBoardUI(); // Aggiorna la UI con i pezzi
    }

    private void updateBoardUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, (char) ('a' + col));
                if (piece != null) {
                    boardButtons[row][col].setIcon(new ImageIcon(piece.getImage())); // Imposta l'icona del pezzo
                } else {
                    boardButtons[row][col].setIcon(null); // Pulisce il bottone se vuoto
                }
            }
        }
    }
    public static void main(String[] args){
        ChessBoardUI chessBoardUI = new ChessBoardUI(new TurnManager());
        chessBoardUI.setVisible(true);
        System.out.println(chessBoard.isPathValid());
    }
}


