import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class King extends Piece {

    public King(int x, int y, Color color) {
        super(x, y, color);
        makeKing();  // Automatically make this piece a King
    }

    @Override
    public void draw(Graphics g, int diameter) {
        super.draw(g, diameter);
        // Additional drawing logic for the King (e.g., drawing a crown)
        g.setColor(Color.RED);  // Example to differentiate the King
        Point position = getPosition();  // Use the getter method
        g.drawOval(position.x - diameter / 4, position.y - diameter / 4, diameter / 2, diameter / 2);
    }
}
