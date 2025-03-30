package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import TurnObserver.*;
import Pieces.Piece;

public class ChessBoardUI extends JFrame implements TurnObserver {
    private final JLabel turnLabel;
    private final JButton[][] boardButtons;
    private final Piece[][] board = new Piece[8][8];
    private final ChessBoard chessBoard = new ChessBoard();

    public ChessBoardUI(TurnManager turnManager) {
        turnManager.attach(this);

        setTitle("Scacchi");
        try {
            setIconImage(ImageIO.read(new File("src/images/white/king.png")));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        setSize(600, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label per il turno
        turnLabel = new JLabel("Turno del Bianco", SwingConstants.CENTER);
        add(turnLabel, BorderLayout.NORTH);

        // Scacchiera
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        boardButtons = new JButton[8][8];

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                int finalRow = row;
                int finalCol = col;
                button.addMouseListener(new MouseAdapter() {
                    private final Color originalColor = (finalRow + finalCol) % 2 == 0 ? Color.WHITE : Color.GRAY; // Colore originale

                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {
                            if (button.getBackground().equals(Color.RED))
                                button.setBackground(originalColor);
                            else
                                button.setBackground(Color.RED);
                        }
                        if (SwingUtilities.isLeftMouseButton(e) && button.getBackground().equals(Color.RED)) {
                            button.setBackground(originalColor);
                        }
                    }
                });
                boardButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        setupBoard(); // Inizializza la scacchiera dopo aver creato i bottoni
    }

    public void renderPieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardButtons[row][col].removeAll();
                Piece piece = chessBoard.getPiece(row, col);
                if (piece != null) {
                    JLabel pieceLabel = new JLabel(getPieceIcon(piece));
                    boardButtons[row][col].add(pieceLabel);
                }
            }
        }
        revalidate();
        repaint();
    }

    @Override
    public void onTurnChanged(boolean isWhiteTurn) {
        turnLabel.setText(isWhiteTurn ? "Turno del Bianco" : "Turno del Nero");
    }

    private void setupBoard() {
        chessBoard.setupBoard(); // Posiziona i pezzi sulla scacchiera
        renderPieces(); // Aggiorna la UI con i pezzi
    }

    private ImageIcon getPieceIcon(Piece piece) {
        String color = piece.getColor().equals("white") ? "white" : "black";
        String type = piece.getClass().getSimpleName().toLowerCase();
        String path = "src/images/" + color + "/" + type + ".png";

        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(60, 60, Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }

    public static void main(String[] args) {
        ChessBoardUI chessBoardUI = new ChessBoardUI(new TurnManager());
        chessBoardUI.setVisible(true);
    }
}
