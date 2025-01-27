import javax.swing.*;

public class Turn implements TurnObserver{
    private final JTextArea textArea;

    public Turn(JTextArea textArea) {
        this.textArea = textArea;
    }


    @Override
    public void onWhiteTurn(String move) {
        textArea.append("Giocatore: " + move + "\n");
    }

    @Override
    public void onBlackTurn(String move) {
        textArea.append("Giocatore: " + move + "\n");
    }
}
