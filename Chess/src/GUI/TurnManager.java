package GUI;

import TurnObserver.TurnObserver;
import java.util.ArrayList;
import java.util.List;

public class TurnManager {
    private boolean isWhiteTurn = true;
    private List<TurnObserver> observers = new ArrayList<>();

    public void attach(TurnObserver observer) {
        observers.add(observer);
    }

    public void detach(TurnObserver observer) {
        observers.remove(observer);
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
}
