package TurnObserver;

import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private boolean isWhiteTurn = true;
    private final List<TurnObserver> observers = new ArrayList<>();

    public void attach(TurnObserver observer) {
        observers.add(observer);
    }

    public void nextTurn() {
        isWhiteTurn = !isWhiteTurn;
        notifyObservers();
    }

    private void notifyObservers() {
        for (TurnObserver observer : observers) {
            observer.onTurnChanged(isWhiteTurn);
        }
    }

    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    public String getCurrentTurn() {
        return isWhiteTurn() ? "white" : "black";
    }

}
