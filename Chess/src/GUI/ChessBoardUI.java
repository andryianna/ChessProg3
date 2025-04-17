package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


import GameState.*;
import TurnObserver.*;
import Pieces.Piece;

public class ChessBoardUI extends JFrame implements TurnObserver {
    private final ChessBoard chessBoard = new ChessBoard();
    private final TurnManager turnManager = new TurnManager();
    private final Game game = new Game(chessBoard,turnManager);
    private final JLabel turnLabel;
    private final JButton[][] boardButtons;
    private String currentTurn = "white";

    private int selectedPieceX;
    private char selectedPieceY;

    public ChessBoardUI() {
        turnManager.attach(this);

        setTitle("Scacchi");
        try {
            setIconImage(ImageIO.read(new File("src/images/white/king.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSize(650, 650);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Label per il turno
        turnLabel = new JLabel("Turno del Bianco", SwingConstants.CENTER);
        add(turnLabel, BorderLayout.NORTH);

        // Pannello principale con le coordinate
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Pannello per la scacchiera con le coordinate
        JPanel boardPanel = new JPanel(new GridBagLayout());
        boardButtons = new JButton[8][8];

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        // Aggiungi numeri sulla sinistra e sulla destra
        for (int row = 0; row < 8; row++) {
            JLabel leftLabel = new JLabel(String.valueOf(8 - row), SwingConstants.CENTER);
            leftLabel.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = row + 1;
            boardPanel.add(leftLabel, gbc);

            JLabel rightLabel = new JLabel(String.valueOf(8 - row), SwingConstants.CENTER);
            rightLabel.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = 9;
            gbc.gridy = row + 1;
            boardPanel.add(rightLabel, gbc);
        }

        // Aggiungi le lettere sopra e sotto
        for (int col = 0; col < 8; col++) {
            JLabel topLabel = new JLabel(String.valueOf((char) ('a' + col)), SwingConstants.CENTER);
            topLabel.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = col + 1;
            gbc.gridy = 0;
            boardPanel.add(topLabel, gbc);

            JLabel bottomLabel = new JLabel(String.valueOf((char) ('a' + col)), SwingConstants.CENTER);
            bottomLabel.setFont(new Font("Arial", Font.BOLD, 14));
            gbc.gridx = col + 1;
            gbc.gridy = 9;
            boardPanel.add(bottomLabel, gbc);
        }

        // Aggiungi bottoni della scacchiera
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(60, 60));
                button.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);

                button.addMouseListener(listener(row, col,button));

                boardButtons[row][col] = button;
                gbc.gridx = col + 1;
                gbc.gridy = row + 1;
                boardPanel.add(button, gbc);
            }
        }

        mainPanel.add(boardPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        setupBoard();
    }

    public void renderPieces() {
        System.out.println("Aggiornamento grafico della scacchiera...");
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = chessBoard.getPiece(row, col);
                if (piece != null) {
                    System.out.printf("Posizione %c%d: %s\n", (char)('a' + col), row, piece.getClass().getSimpleName());
                    boardButtons[row][col].setIcon(getPieceIcon(piece));
                } else {
                    boardButtons[row][col].setIcon(null);
                }
            }
        }
    }

    private boolean checkCurrentTurn(Piece selected){
        if (selected == null) {
            return false;
        }

        return selected.color().equals(currentTurn);
    }

    private MouseListener listener(int finalRow, int finalCol, JButton button) {
        return new MouseAdapter() {
            final Color originalColor = (finalRow + finalCol) % 2 == 0 ? Color.WHITE : Color.GRAY;

            @Override
            public void mousePressed(MouseEvent e) {
                /// azioni con il tasto destro del mouse
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (button.getBackground().equals(Color.RED))
                        button.setBackground(originalColor);
                    else
                        button.setBackground(Color.RED);
                    return;
                }

                Piece selectedPiece =  chessBoard.getPiece(finalRow, finalCol);

                /// azioni con il tasto sinistro del mouse
                if (SwingUtilities.isLeftMouseButton(e)) {
                        if (game.getState() instanceof NoSelectionState && checkCurrentTurn(selectedPiece)) {
                            game.setState(new PieceSelectedState(finalRow, (char) finalCol, chessBoard));
                            selectedPieceX = finalRow;
                            selectedPieceY = (char) (finalCol + 'a');
                            button.setBackground(Color.YELLOW);
                            System.out.println("Pezzo selezionato a " + selectedPieceY + selectedPieceX);
                        } else if (game.getState() instanceof PieceSelectedState) {
                        if (selectedPieceX == finalRow && selectedPieceY == (char) (finalCol + 'a')) {
                            game.setState(new NoSelectionState(chessBoard));
                            button.setBackground(originalColor);
                            System.out.println("Deselezionato pezzo.");
                        } else if (game.isValidMove(selectedPieceX, selectedPieceY, finalRow, (char) (finalCol + 'a'),chessBoard)) {
                            if (game.movePiece(selectedPieceX, selectedPieceY, finalRow, (char) (finalCol + 'a'))) {
                                System.out.println("Chiamo renderPieces dopo mossa valida");
                                game.setState(new NoSelectionState(chessBoard));
                                renderPieces();
                            }

                        } else {
                            System.out.println("Mossa non valida!");
                        }
                    }
                }
            }
        };
    }

    @Override
    public void onTurnChanged(boolean isWhiteTurn) {
        turnLabel.setText(isWhiteTurn ? "Turno del Bianco" : "Turno del Nero");
        currentTurn = isWhiteTurn ? "white" : "black";
    }

    private void setupBoard() {
        chessBoard.setupBoard();
        renderPieces();
    }

    private ImageIcon getPieceIcon(Piece piece) {
        String color = piece.color().equals("white") ? "white" : "black";
        String type = piece.getClass().getSimpleName().toLowerCase();
        String path = "src/images/" + color + "/" + type + ".png";

        ImageIcon icon = new ImageIcon(path);
        Image scaled = icon.getImage().getScaledInstance(33, 33,Image.SCALE_DEFAULT);
        return new ImageIcon(scaled);
    }

    public static void main(String[] args) {
        ChessBoardUI chessBoardUI = new ChessBoardUI();
        chessBoardUI.setVisible(true);
    }
}
