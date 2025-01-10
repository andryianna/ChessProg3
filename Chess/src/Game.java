import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Game extends JPanel {

    public Game() {
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
                JPanel cell = new JPanel();
                if ((row + col) % 2 == 0) {
                    cell.setBackground(lightColor);
                } else {
                    cell.setBackground(darkColor);
                }
                chessBoard.add(cell);
            }
        }

        // Aggiungi la scacchiera al pannello principale
        add(chessBoard, BorderLayout.CENTER);
    }
}