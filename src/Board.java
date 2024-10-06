import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int DIAMETER = 30;
    private static final int GRID_SIZE = 75; // Size of each square on the board
    private static final int BOARD_SIZE = 600; // Board width and height
   

    private Piece[] whitePieces;
    private Piece[] blackPieces;
    private Piece selectedPiece = null; // Track the selected piece
    private List<Point> possibleMoves = new ArrayList<>(); // Track possible moves

    private Game game; // Declare the Game object
    private boolean gameOver = false; // Add a flag to track game state

    public Board() {
        setLayout(null);
        initializePieces();
        game = new Game(); // Initialize the Game object
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedPiece == null) {
                    deselectAllPieces();
                    Piece clickedPiece = getPieceAt(e.getPoint());
                    if (clickedPiece != null) {
                        System.out.println("Piece selected: " + clickedPiece.getClass().getSimpleName());

                        if (clickedPiece.getColor() == game.getCurrentTurnColor()) {
                            selectedPiece = clickedPiece;
                        
                            Piece[][] boardArray = createBoardArray();
                            if (noValidMovesLeft(selectedPiece, boardArray)) {
                                JOptionPane.showMessageDialog(Board.this, "No valid moves left for this piece.", "No Moves", JOptionPane.INFORMATION_MESSAGE);
                                selectedPiece = null;
                                return;
                            }

                            showPossibleMoves(selectedPiece);
                        } else {
                            System.out.println("Not the current player's turn.");
                        }
                    } else {
                        System.out.println("No piece selected.");
                    }
                } else {
                    // Use isClickOnPossibleMove to check if the move is valid
                    if (isClickOnPossibleMove(e.getPoint())) {
                        movePiece(e.getPoint());
                    } else {
                        deselectAllPieces();
                        selectedPiece = null;
                        possibleMoves.clear();
                    }
                }
            }


        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw all white and black pieces in their current positions
        drawPieces(g2d, whitePieces);
        drawPieces(g2d, blackPieces);

        // Draw possible move highlights
        g.setColor(Color.RED);
        for (Point move : possibleMoves) {
            g.drawOval(move.x - DIAMETER / 2, move.y - DIAMETER / 2, DIAMETER, DIAMETER);
        }
    }

    private void drawBoard(Graphics g) {
        Color darkGray = new Color(50, 50, 50); // Dark gray color for the board
        for (int i = 0; i < BOARD_SIZE / GRID_SIZE; i++) {
            for (int m = 0; m < BOARD_SIZE / GRID_SIZE; m++) {
                g.setColor((i + m) % 2 != 0 ? darkGray : Color.WHITE);
                g.fillRect(i * GRID_SIZE, m * GRID_SIZE, GRID_SIZE, GRID_SIZE);
            }
        }
    }

    private void drawPieces(Graphics2D g2d, Piece[] pieces) {
        for (Piece piece : pieces) {
            if (piece != null && !piece.isCaptured()) {
                piece.draw(g2d, DIAMETER);
            }
        }
    }

    
// Initialize white and black pieces
private void initializePieces() {
    int offset = GRID_SIZE / 2;

    // Initialize white pieces
    whitePieces = new Piece[]{
            // new WhitePiece(1 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
            // new WhitePiece(3 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
            // new WhitePiece(5 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
            // new WhitePiece(7 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
            // new WhitePiece(0 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
            // new WhitePiece(2 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
            // new WhitePiece(4 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
            // new WhitePiece(6 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
            new WhitePiece(1 * GRID_SIZE + offset, 2 * GRID_SIZE + offset),
            new WhitePiece(3 * GRID_SIZE + offset, 2 * GRID_SIZE + offset),
            // new WhitePiece(5 * GRID_SIZE + offset, 2 * GRID_SIZE + offset),
            // new WhitePiece(7 * GRID_SIZE + offset, 2 * GRID_SIZE + offset)
    };

    // Initialize black pieces
    blackPieces = new Piece[]{
            new BlackPiece(0 * GRID_SIZE + offset, 5 * GRID_SIZE + offset),
            // new BlackPiece(2 * GRID_SIZE + offset, 5 * GRID_SIZE + offset),
            // new BlackPiece(4 * GRID_SIZE + offset, 5 * GRID_SIZE + offset),
            // new BlackPiece(6 * GRID_SIZE + offset, 5 * GRID_SIZE + offset),
            // new BlackPiece(1 * GRID_SIZE + offset, 6 * GRID_SIZE + offset),
            // new BlackPiece(3 * GRID_SIZE + offset, 6 * GRID_SIZE + offset),
            // new BlackPiece(5 * GRID_SIZE + offset, 6 * GRID_SIZE + offset),
            // new BlackPiece(7 * GRID_SIZE + offset, 6 * GRID_SIZE + offset),
            // new BlackPiece(0 * GRID_SIZE + offset, 7 * GRID_SIZE + offset),
            // new BlackPiece(2 * GRID_SIZE + offset, 7 * GRID_SIZE + offset),
            // new BlackPiece(4 * GRID_SIZE + offset, 7 * GRID_SIZE + offset),
            // new BlackPiece(6 * GRID_SIZE + offset, 7 * GRID_SIZE + offset)
    };
}


    private Piece getPieceAt(Point point) {
        int x = point.x / GRID_SIZE;
        int y = point.y / GRID_SIZE;
        Piece[][] boardArray = createBoardArray();

        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            return boardArray[y][x];
        }
        return null;
    }

    private Piece[][] createBoardArray() {
        Piece[][] board = new Piece[8][8];
        populateBoardArray(board, whitePieces);
        populateBoardArray(board, blackPieces);
        return board;
    }

    private void populateBoardArray(Piece[][] board, Piece[] pieces) {
        for (Piece piece : pieces) {
            if (piece != null) {
                int x = piece.getPosition().x / GRID_SIZE;
                int y = piece.getPosition().y / GRID_SIZE;
                board[y][x] = piece;
            }
        }
    }

    private void deselectAllPieces() {
        for (Piece piece : whitePieces) {
            if (piece != null) {
                piece.setSelected(false);
            }
        }
        for (Piece piece : blackPieces) {
            if (piece != null) {
                piece.setSelected(false);
            }
        }
    }

    private boolean isClickOnPossibleMove(Point point) {
        return possibleMoves.stream().anyMatch(move -> isWithinRange(point, move));
    }

    private boolean isWithinRange(Point clickPoint, Point movePoint) {
        int radius = DIAMETER / 2;
        return clickPoint.distance(movePoint) <= radius;
    }

    private void movePiece(Point point) {
        int offset = GRID_SIZE / 2;
        try {
            int gridX = point.x / GRID_SIZE;
            int gridY = point.y / GRID_SIZE;
            Point newPosition = new Point(gridX * GRID_SIZE + offset, gridY * GRID_SIZE + offset);
            Piece[][] boardArray = createBoardArray();
            
            if (possibleMoves.contains(newPosition) && canCapture(selectedPiece, boardArray, newPosition)) {
                handleCapture(selectedPiece, newPosition, boardArray);
    
                // Check for additional capture opportunities from the new position
                possibleMoves = getCaptureMoves(selectedPiece, boardArray);
                if (!possibleMoves.isEmpty()) {
                    // Show dialog only once
                    JOptionPane.showMessageDialog(Board.this, "You can perform another capture!", "Capture Opportunity", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Let the player perform another capture; don't switch turn yet
                    return;
                }
    
                checkForWinner();
                if (!gameOver) {
                    game.switchTurn();
                }
            } else if (selectedPiece.isDestinationEmpty(boardArray, newPosition)) {
                selectedPiece.setPosition(newPosition);
                promotePieceIfNecessary(selectedPiece, gridY);
                deselectAllPieces();
                selectedPiece = null;
                possibleMoves.clear();
                checkForWinner();
                if (!gameOver) {
                    game.switchTurn();
                }
            } else {
                throw new IllegalStateException("Invalid move: Destination is not empty or capture not possible!");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            deselectAllPieces();
            selectedPiece = null;
            possibleMoves.clear();
        }
        repaint();
    }
    

    private void promotePieceIfNecessary(Piece piece, int gridY) {
        if (piece.getColor() == Color.WHITE && gridY == 7 || piece.getColor() == Color.BLACK && gridY == 0) {
            piece.makeKing();
        }
    }

    private boolean canCapture(Piece piece, Piece[][] boardArray, Point newPosition) {
        return calculateCapturePosition(piece, newPosition, boardArray) != null;
    }

    private Point calculateCapturePosition(Piece piece, Point newPosition, Piece[][] boardArray) {
        int oldX = piece.getPosition().x / GRID_SIZE;
        int oldY = piece.getPosition().y / GRID_SIZE;
        int newX = newPosition.x / GRID_SIZE;
        int newY = newPosition.y / GRID_SIZE;

        int deltaX = newX - oldX;
        int deltaY = newY - oldY;

        if (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 2) {
            int captureX = oldX + deltaX / 2;
            int captureY = oldY + deltaY / 2;

            Piece capturedPiece = boardArray[captureY][captureX];
            if (capturedPiece != null && capturedPiece.getColor() != piece.getColor()) {
                return new Point(captureX, captureY);
            }
        }
        return null;
    }

    private void handleCapture(Piece movedPiece, Point newPosition, Piece[][] boardArray) {
        // Calculate the position of the captured piece
        Point capturePosition = calculateCapturePosition(movedPiece, newPosition, boardArray);
    
        if (capturePosition != null) {
            // Capture the opponent's piece
            Piece capturedPiece = boardArray[capturePosition.y][capturePosition.x];
            capturedPiece.setCaptured(true);
            removePieceFromBoard(capturedPiece);
    
            // Move the capturing piece to the new position
            movedPiece.setPosition(newPosition);
    
            // Check for further capture opportunities from the new position for both black and white pieces
            possibleMoves = getCaptureMoves(movedPiece, createBoardArray());
    
            if (!possibleMoves.isEmpty()) {
                // Double capture is possible, let the player capture again
                JOptionPane.showMessageDialog(Board.this, "You can perform another capture!", "Capture Opportunity", JOptionPane.INFORMATION_MESSAGE);
                return; // Allow the player to continue their turn
            } else {
                // No further capture moves are possible
                JOptionPane.showMessageDialog(Board.this, "No possible moves for this piece.", "No Moves", JOptionPane.INFORMATION_MESSAGE);
            }
    
            // Deselect all pieces and switch the turn if no more captures are possible
            deselectAllPieces();
            // game.switchTurn();
        }
    }
    
    

    private void removePieceFromBoard(Piece piece) {
        if (piece.getColor() == Color.WHITE) {
            whitePieces = removePieceFromArray(whitePieces, piece);
        } else {
            blackPieces = removePieceFromArray(blackPieces, piece);
        }
        repaint();
    }

    private Piece[] removePieceFromArray(Piece[] pieces, Piece pieceToRemove) {
        return Arrays.stream(pieces).filter(piece -> piece != null && piece != pieceToRemove).toArray(Piece[]::new);
    }

    private List<Point> getCaptureMoves(Piece piece, Piece[][] boardArray) {
        List<Point> captureMoves = new ArrayList<>();
        
        int[] dx = {-2, 2, -2, 2};  // Two square diagonal jumps
        int[] dy = {-2, -2, 2, 2};  
        
        int oldX = piece.getPosition().x / GRID_SIZE;
        int oldY = piece.getPosition().y / GRID_SIZE;
        
        for (int i = 0; i < dx.length; i++) {
            int newX = oldX + dx[i];
            int newY = oldY + dy[i];
            
            // Mid-point coordinates (opponent's piece position)
            int midX = oldX + dx[i] / 2;
            int midY = oldY + dy[i] / 2;
            
            // Check that both the new position and the mid-point (opponent's piece) are within board bounds (0-7)
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8 && midX >= 0 && midY < 8 && midX >= 0 && midY < 8) {
                Piece opponentPiece = boardArray[midY][midX];
                Piece landingSpot = boardArray[newY][newX];
    
                // Capture is possible if:
                // 1. The mid-point (opponent's piece) is not null and is an opponent's piece.
                // 2. The landing spot (new position) is empty.
                if (opponentPiece != null && opponentPiece.getColor() != piece.getColor() && landingSpot == null) {
                    Point newPosition = new Point(newX * GRID_SIZE + GRID_SIZE / 2, newY * GRID_SIZE + GRID_SIZE / 2);
                    captureMoves.add(newPosition);
                }
            }
        }
        
        return filterOutOfBoundsMoves(captureMoves);
    }
    
    
    

    private void showPossibleMoves(Piece piece) {
        Piece[][] boardArray = createBoardArray();
    
        // Get possible capture moves
        possibleMoves = getCaptureMoves(piece, boardArray);
    
        // If no capture moves are available, get the regular possible moves
        if (possibleMoves.isEmpty()) {
            possibleMoves = filterOutOfBoundsMoves(piece.getPossibleMoves(boardArray));
        }
    
        // Mark the piece as selected
        piece.setSelected(true);
    
        // Repaint the board to show possible moves
        repaint();
    }
    
    
    
    private List<Point> filterOutOfBoundsMoves(List<Point> moves) {
        List<Point> validMoves = new ArrayList<>();
    
        for (Point move : moves) {
            int gridX = move.x / GRID_SIZE;
            int gridY = move.y / GRID_SIZE;
    
            // Ensure that the move stays within the bounds of the board (0 to 7 for both x and y)
            if (gridX >= 0 && gridX < 8 && gridY >= 0 && gridY < 8) {
                validMoves.add(move);
            }
        }
    
        return validMoves;
    }
    
    

    private void checkForWinner() {
        Piece[][] boardArray = createBoardArray();
        if (noPiecesLeft(whitePieces)) {
            showWinnerDialog(Color.BLACK);
        } else if (noPiecesLeft(blackPieces)) {
            showWinnerDialog(Color.WHITE);
        } else if (Arrays.stream(whitePieces).noneMatch(piece -> !piece.isCaptured() && !noValidMovesLeft(piece, boardArray)) && game.getCurrentTurnColor() == Color.WHITE) {
            showWinnerDialog(Color.BLACK);
        } else if (Arrays.stream(blackPieces).noneMatch(piece -> !piece.isCaptured() && !noValidMovesLeft(piece, boardArray)) && game.getCurrentTurnColor() == Color.BLACK) {
            showWinnerDialog(Color.WHITE);
        }
    }
    
    
    

    private boolean noPiecesLeft(Piece[] pieces) {
        return Arrays.stream(pieces).allMatch(piece -> piece == null || piece.isCaptured());
    }
    

    private boolean noValidMovesLeft(Piece piece, Piece[][] boardArray) {
        int currentX = piece.getPosition().x / GRID_SIZE;
        int currentY = piece.getPosition().y / GRID_SIZE;
    
        // Check if the piece is on the left or right edge of the board
        boolean isAtLeftEdge = currentX == 0;
        boolean isAtRightEdge = currentX == 7;
    
        // Check if the piece is blocked on the left or right side by another piece
        boolean isBlockedOnLeft = currentX > 0 && boardArray[currentY][currentX - 1] != null;
        boolean isBlockedOnRight = currentX < 7 && boardArray[currentY][currentX + 1] != null;
    
        // Check if the piece has no valid moves left
        boolean hasNoValidMoves = (isAtLeftEdge || isBlockedOnLeft) && (isAtRightEdge || isBlockedOnRight);
    
        return hasNoValidMoves;
    }
    
    

    private void showWinnerDialog(Color winningColor) {
        gameOver = true; // Set game over to true to prevent further moves
        String winner = winningColor == Color.WHITE ? "White" : "Black";
        JOptionPane.showMessageDialog(this, winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
    
        // Reset the game after the winner is displayed
        resetGame();
    }

    private void resetGame() {
        initializePieces();
        game.resetGame();
        selectedPiece = null;
        possibleMoves.clear();
        gameOver = false; // Reset the game over flag
        repaint();
    }
}




















