package GameState;

import GUI.Game;
import javax.swing.*;

public class CheckmateState extends JOptionPane implements GameState {
    @Override
    public void handleClick(Game game, int x, char y) {
        String winner = game.getTurnManager().isWhiteTurn() ? "nero" : "bianco";
        JOptionPane.showMessageDialog(null, "Vince per scacco matto il giocatore " + winner);
        System.out.println("Scacco matto! Il gioco è finito.");
    }
}