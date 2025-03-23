package GUI;

import javax.swing.*;
import java.awt.*;
import TurnObserver.*;

public class ChessBoardUI extends JFrame implements TurnObserver {
    private JLabel turnLabel;
    private JButton[][] boardButtons;
    private TurnManager turnManager;

    public ChessBoardUI(TurnManager turnManager) {
        this.turnManager = turnManager;
        this.turnManager.attach(this);

        setTitle("Scacchi");
        setSize(600, 600);
        setLayout(new BorderLayout());

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

    public static void main(String[] args){
        ChessBoardUI chessBoardUI = new ChessBoardUI(new TurnManager());
    }
}


