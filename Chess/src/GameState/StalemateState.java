package GameState;
import GUI.Game;

import javax.swing.*;

public class StalemateState extends JOptionPane implements GameState {
    @Override
    public void handleClick(Game game, int x, char y){
        JOptionPane.showMessageDialog(null, "Stallo!");
        System.out.println("Stallo!");
    }
}
