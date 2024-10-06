import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Piece {
    protected Point position;
    protected Color color;
    protected boolean isSelected = false;
    protected boolean isKing = false;
    protected boolean isCaptured = false; // Track if the piece is captured
    private Point opponentPiecePosition; // Store the position of the opponent's piece for later capture

    private static final int GRID_SIZE = 75; // Size of each square on the board

    public Piece(int x, int y, Color color) {
        this.position = new Point(x, y);
        this.color = color;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Color getColor() {
        return color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isKing() {
        return isKing;
    }

    public void makeKing() {
        this.isKing = true;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public void setCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }

    public Point getOpponentPiecePosition() {
        return opponentPiecePosition;
    }

    public void setOpponentPiecePosition(Point opponentPiecePosition) {
        this.opponentPiecePosition = opponentPiecePosition;
    }

    public void draw(Graphics g, int diameter) {
        if (!isCaptured) { // Only draw if the piece is not captured
            g.setColor(isSelected ? Color.YELLOW : color);
            g.fillOval(position.x - diameter / 2, position.y - diameter / 2, diameter, diameter);
        }
    }

    public boolean isInsideCircle(Point point, int diameter) {
        return point.distance(position) <= diameter / 2;
    }

    public List<Point> getPossibleMoves(Piece[][] board) {
        List<Point> possibleMoves = new ArrayList<>();
        int x = position.x;
        int y = position.y;

        if (isKing) {
            addAllDirectionalMoves(possibleMoves, board, x, y);
        } else {
            addDiagonalMoves(possibleMoves, board, x, y);
        }

        return possibleMoves;
    }

    protected void addDiagonalMoves(List<Point> possibleMoves, Piece[][] board, int x, int y) {
        if (color == Color.WHITE) {
            // White pieces move "down" the board
            addIfValidMove(possibleMoves, board, new Point(x - GRID_SIZE, y + GRID_SIZE)); // Left diagonal down
            addIfValidMove(possibleMoves, board, new Point(x + GRID_SIZE, y + GRID_SIZE)); // Right diagonal down
        } else if (color == Color.BLACK) {
            // Black pieces move "up" the board
            addIfValidMove(possibleMoves, board, new Point(x - GRID_SIZE, y - GRID_SIZE)); // Left diagonal up
            addIfValidMove(possibleMoves, board, new Point(x + GRID_SIZE, y - GRID_SIZE)); // Right diagonal up
        }
    }

    protected void addAllDirectionalMoves(List<Point> possibleMoves, Piece[][] board, int x, int y) {
        // Add all possible moves for a king piece
        for (int dx = -1; dx <= 1; dx += 2) {
            for (int dy = -1; dy <= 1; dy += 2) {
                int step = 1;
                while (true) {
                    Point move = new Point(x + step * dx * GRID_SIZE, y + step * dy * GRID_SIZE);
                    if (!isWithinBoard(move) || !isDestinationEmpty(board, move)) {
                        break;
                    }
                    possibleMoves.add(move);
                    step++;
                }
            }
        }
    }

    protected boolean isWithinBoard(Point point) {
        return point.x >= 0 && point.x < GRID_SIZE * 8 && point.y >= 0 && point.y < GRID_SIZE * 8;
    }

    protected boolean isDestinationEmpty(Piece[][] board, Point point) {
        int gridX = point.x / GRID_SIZE;
        int gridY = point.y / GRID_SIZE;
        return board[gridY][gridX] == null;
    }

    protected void addIfValidMove(List<Point> possibleMoves, Piece[][] board, Point move) {
        if (isWithinBoard(move) && isDestinationEmpty(board, move)) {
            possibleMoves.add(move);
        }
    }
}
