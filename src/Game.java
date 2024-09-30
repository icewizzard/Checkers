import java.awt.Color;
import java.util.Random;

public class Game {
    private Color currentTurn;

    public Game() {
        // Randomly select the starting color
        currentTurn = new Random().nextBoolean() ? Color.WHITE : Color.BLACK;
    }

    public Color getCurrentTurnColor() {
        return currentTurn;
    }

    public void switchTurn() {
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public void resetGame() {
        // Reset the game to the initial state
        currentTurn = new Random().nextBoolean() ? Color.WHITE : Color.BLACK;
    }
}
