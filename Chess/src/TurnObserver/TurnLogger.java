package TurnObserver;

public class TurnLogger implements TurnObserver {

    @Override
    public void onTurnChanged(boolean isWhiteTurn) {
        System.out.println("Turn changed to " + (isWhiteTurn ? "white" : "black"));
    }
}
