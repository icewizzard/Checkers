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

    public Board() {
        setLayout(null);
        initializePieces();
        game = new Game(); // Initialize the Game object
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (selectedPiece == null) {
                    // Deselect all pieces
                    deselectAllPieces();

                    // Check if a piece is clicked
                    Piece clickedPiece = getPieceAt(e.getPoint());
                    if (clickedPiece != null && clickedPiece.getColor() == game.getCurrentTurnColor()) {
                        selectedPiece = clickedPiece;
                        showPossibleMoves(selectedPiece);
                    }
                } else {
                    if (isClickOnPossibleMove(e.getPoint())) {
                        movePiece(e.getPoint());
                    } else {
                        deselectAllPieces();
                        selectedPiece = null;
                        possibleMoves.clear();
                    }
                }
                repaint(); // Repaint the board to show the changes
            }
        });
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

    private void initializePieces() {
        int offset = GRID_SIZE / 2;

        // Initialize white pieces
        whitePieces = new Piece[]{
                new WhitePiece(1 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
                // new WhitePiece(3 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
                // new WhitePiece(5 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
                // new WhitePiece(7 * GRID_SIZE + offset, 0 * GRID_SIZE + offset),
                // new WhitePiece(0 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
                // new WhitePiece(2 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
                // new WhitePiece(4 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
                // new WhitePiece(6 * GRID_SIZE + offset, 1 * GRID_SIZE + offset),
                // new WhitePiece(1 * GRID_SIZE + offset, 2 * GRID_SIZE + offset),
                // new WhitePiece(3 * GRID_SIZE + offset, 2 * GRID_SIZE + offset),
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

    private Piece[][] createBoardArray() {
        Piece[][] board = new Piece[8][8];

        for (Piece piece : whitePieces) {
            if (piece != null) {
                int x = piece.getPosition().x / GRID_SIZE;
                int y = piece.getPosition().y / GRID_SIZE;
                board[y][x] = piece;
            }
        }
        for (Piece piece : blackPieces) {
            if (piece != null) {
                int x = piece.getPosition().x / GRID_SIZE;
                int y = piece.getPosition().y / GRID_SIZE;
                board[y][x] = piece;
            }
        }

        return board;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw all white pieces in their current positions
        for (Piece piece : whitePieces) {
            if (piece != null && !piece.isCaptured()) { // Ensure the piece is not captured
                piece.draw(g2d, DIAMETER);
            }
        }

        // Draw all black pieces in their current positions
        for (Piece piece : blackPieces) {
            if (piece != null && !piece.isCaptured()) { // Ensure the piece is not captured
                piece.draw(g2d, DIAMETER);
            }
        }

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

    private void showPossibleMoves(Piece piece) {
        Piece[][] boardArray = createBoardArray();
        possibleMoves = getCaptureMoves(piece, boardArray);

        // If no capture moves are available, show regular moves
        if (possibleMoves.isEmpty()) {
            possibleMoves = piece.getPossibleMoves(boardArray);
        }

        piece.setSelected(true);
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
    
            // Check if the move clicked is one of the possible capture moves
            if (possibleMoves.contains(newPosition) && canCapture(selectedPiece, boardArray, newPosition)) {
                // Handle the capture
                handleCapture(selectedPiece, newPosition, boardArray);
                // If there are more captures possible for the same piece after this one, allow further captures
                List<Point> additionalCaptures = getCaptureMoves(selectedPiece, createBoardArray());
                if (!additionalCaptures.isEmpty()) {
                    showPossibleMoves(selectedPiece); // Show additional possible capture moves
                    return; // Allow the player to continue capturing
                }
            }
    
            // Check if the destination is empty or if it's a valid move (non-capture)
            if (selectedPiece.isDestinationEmpty(boardArray, newPosition)) {
                selectedPiece.setPosition(newPosition);
    
                // Promote piece to king if it reaches the opposite end
                if (selectedPiece.getColor() == Color.WHITE && gridY == 7) {
                    selectedPiece.makeKing();
                } else if (selectedPiece.getColor() == Color.BLACK && gridY == 0) {
                    selectedPiece.makeKing();
                }
                
    
                deselectAllPieces();
                selectedPiece = null;
                possibleMoves.clear();

                checkForWinner();
                game.switchTurn(); // Switch turn after a move
            } else {
                throw new IllegalStateException("Invalid move: Destination is not empty or capture not possible!");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            deselectAllPieces();
            selectedPiece = null;
            possibleMoves.clear();
        }
        repaint(); // Ensure repaint is called after every move
    }
    

    private boolean canCapture(Piece piece, Piece[][] boardArray, Point newPosition) {
        if (newPosition == null) {
            return false; // Invalid move if newPosition is null
        }
    
        int oldX = piece.getPosition().x / GRID_SIZE;
        int oldY = piece.getPosition().y / GRID_SIZE;
        int newX = newPosition.x / GRID_SIZE;
        int newY = newPosition.y / GRID_SIZE;
    
        int deltaX = newX - oldX;
        int deltaY = newY - oldY;
    
        System.out.println("Checking capture: oldX=" + oldX + " oldY=" + oldY + " newX=" + newX + " newY=" + newY);
    
        // Check if the move is a capture move (two squares away diagonally)
        if (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 2) {
            int captureX = oldX + deltaX / 2;
            int captureY = oldY + deltaY / 2;
    
            Piece capturedPiece = boardArray[captureY][captureX];
            
            System.out.println("Piece at (" + captureX + ", " + captureY + ") is " + (capturedPiece == null ? "null" : capturedPiece.getColor()));
    
            // Check if the captured piece exists and is of the opposite color
            return capturedPiece != null && capturedPiece.getColor() != piece.getColor();
        }
    
        return false; // Not a capture move
    }
    

    private List<Point> getCaptureMoves(Piece piece, Piece[][] boardArray) {
        List<Point> captureMoves = new ArrayList<>();
        int oldX = piece.getPosition().x / GRID_SIZE;
        int oldY = piece.getPosition().y / GRID_SIZE;

        // Define possible capture move directions (two squares diagonally)
        int[] dx = {-2, 2, -2, 2};
        int[] dy = {-2, -2, 2, 2};

        for (int i = 0; i < dx.length; i++) {
            int newX = oldX + dx[i];
            int newY = oldY + dy[i];

            // Ensure the new position is within bounds
            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                // Convert grid position back to pixel position
                Point newPosition = new Point(newX * GRID_SIZE + GRID_SIZE / 2, newY * GRID_SIZE + GRID_SIZE / 2);

                // Check if it's a valid capture move
                if (canCapture(piece, boardArray, newPosition)) {
                    int captureX = oldX + dx[i] / 2;
                    int captureY = oldY + dy[i] / 2;
                    Piece capturedPiece = boardArray[captureY][captureX];

                    // Print captured piece property if it's a valid capture move
                    if (capturedPiece != null) {
                        System.out.println("Selected piece at (" + oldX + ", " + oldY + ") can capture piece at (" + captureX + ", " + captureY + ")");
                        System.out.println("Captured piece isCaptured before move: " + capturedPiece.isCaptured());
                    }

                    // Check if the destination is empty
                    if (piece.isDestinationEmpty(boardArray, newPosition)) {
                        captureMoves.add(newPosition);
                        System.out.println("Valid capture move: (" + newX + ", " + newY + ")");
                    }
                }
            }
        }

        // Print all capture moves for the selected piece
        if (!captureMoves.isEmpty()) {
            System.out.println("Capture moves available for the selected piece: " + captureMoves);

        } else {
            System.out.println("No capture moves available for the selected piece.");
        }

        return captureMoves;
    }

    private void handleCapture(Piece movedPiece, Point newPosition, Piece[][] boardArray) {
        int oldX = movedPiece.getPosition().x / GRID_SIZE;
        int oldY = movedPiece.getPosition().y / GRID_SIZE;
        int newX = newPosition.x / GRID_SIZE;
        int newY = newPosition.y / GRID_SIZE;
    
        int deltaX = newX - oldX;
        int deltaY = newY - oldY;
    
        // Check if the move was two squares diagonally, indicating a capture
        if (Math.abs(deltaX) == 2 && Math.abs(deltaY) == 2) {
            int captureX = oldX + deltaX / 2;
            int captureY = oldY + deltaY / 2;
    
            Piece capturedPiece = boardArray[captureY][captureX];
    
            // Ensure there's a captured piece of the opposite color
            if (capturedPiece != null && capturedPiece.getColor() != movedPiece.getColor()) {
    
                // Simulate player decision (yes or no)
                boolean playerWantsToCapture = getPlayerCaptureDecision(); // Placeholder for actual decision mechanism
    
                if (playerWantsToCapture) {
                    // Player decided to perform the capture
                    System.out.println("Player chose to capture!");
    
                    // Mark the captured piece as captured and remove it from the board
                    capturedPiece.setCaptured(true);
    
                    // Remove the captured piece from the piece array and board array
                    removePieceFromBoard(capturedPiece, captureX, captureY, boardArray);
    
                    // Force a full repaint after the piece is removed
                    repaint();
    
                    // Log the remaining pieces
                    System.out.println("Captured piece removed at: " + captureX + ", " + captureY);
                } else {
                    // Player decided not to perform the capture
                    System.out.println("Player chose not to capture!");
    
                    // Remove the moved piece from the board
                    movedPiece.setCaptured(true);
                    removePieceFromBoard(movedPiece, newX, newY, boardArray);
    
                    // Force a full repaint after the piece is removed
                    repaint();
    
                    System.out.println("Moved piece removed from board.");
                }
    
                // Log the remaining pieces
                System.out.println("Total white pieces: " + whitePieces.length);
                System.out.println("Total black pieces: " + blackPieces.length);
            }
        }
    
        // Special logic for kings to allow backward captures
        if (movedPiece.isKing()) {
            handleKingCapture(movedPiece, newPosition, boardArray);
        }
    }
    
    private void handleKingCapture(Piece movedPiece, Point newPosition, Piece[][] boardArray) {
        int newX = newPosition.x / GRID_SIZE;
        int newY = newPosition.y / GRID_SIZE;
    
        // Kings can move and capture in any direction
        int[] deltaXOptions = {-2, 2};
        int[] deltaYOptions = {-2, 2};
    
        for (int dx : deltaXOptions) {
            for (int dy : deltaYOptions) {
                int targetX = newX + dx;
                int targetY = newY + dy;
                
                if (targetX >= 0 && targetX < 8 && targetY >= 0 && targetY < 8) {
                    int captureX = newX + dx / 2;
                    int captureY = newY + dy / 2;
    
                    Piece capturedPiece = boardArray[captureY][captureX];
    
                    if (capturedPiece != null && capturedPiece.getColor() != movedPiece.getColor()) {
                        // Simulate player decision (yes or no)
                        boolean playerWantsToCapture = getPlayerCaptureDecision(); // Placeholder for actual decision mechanism
    
                        if (playerWantsToCapture) {
                            // Player decided to perform the capture
                            System.out.println("King chose to capture!");
    
                            // Mark the captured piece as captured and remove it from the board
                            capturedPiece.setCaptured(true);
    
                            // Remove the captured piece from the piece array and board array
                            removePieceFromBoard(capturedPiece, captureX, captureY, boardArray);
    
                            // Force a full repaint after the piece is removed
                            repaint();
    
                            // Log the remaining pieces
                            System.out.println("Captured piece removed by king at: " + captureX + ", " + captureY);
                        }
                    }
                }
            }
        }
    }
        
    private void removePieceFromBoard(Piece piece, int x, int y, Piece[][] boardArray) {
        // Remove the piece from the board array
        boardArray[y][x] = null;
    
        // Remove the piece from the piece arrays
        if (piece.getColor() == Color.WHITE) {
            whitePieces = removePieceFromArray(whitePieces, piece);
            System.out.println("White piece removed.");
        } else {
            blackPieces = removePieceFromArray(blackPieces, piece);
            System.out.println("Black piece removed.");
        }
    
        // Repaint the board to reflect the changes
        repaint(x * GRID_SIZE, y * GRID_SIZE, GRID_SIZE, GRID_SIZE);
    }
    
    private Piece[] removePieceFromArray(Piece[] pieces, Piece pieceToRemove) {
        // Filter out the captured piece from the array
        return Arrays.stream(pieces)
                .filter(piece -> piece != null && piece != pieceToRemove) // Remove the specific piece
                .toArray(Piece[]::new); // Convert the stream back to an array
    }
    
    private boolean getPlayerCaptureDecision() {
        // Simulate a player's decision to capture or not
        return true; // Default to yes
    }
    private void checkForWinner() {
        if (noPiecesLeft(whitePieces)) {
            showWinnerDialog(Color.BLACK);
        } else if (noPiecesLeft(blackPieces)) {
            showWinnerDialog(Color.WHITE);
        } else if (noValidMovesLeft(whitePieces) && game.getCurrentTurnColor() == Color.WHITE) {
            showWinnerDialog(Color.BLACK);
        } else if (noValidMovesLeft(blackPieces) && game.getCurrentTurnColor() == Color.BLACK) {
            showWinnerDialog(Color.WHITE);
        }
    }

    private boolean noPiecesLeft(Piece[] pieces) {
        return Arrays.stream(pieces).allMatch(piece -> piece == null || piece.isCaptured());
    }

    private boolean noValidMovesLeft(Piece[] pieces) {
        Piece[][] boardArray = createBoardArray();
        return Arrays.stream(pieces)
                     .filter(piece -> piece != null && !piece.isCaptured())
                     .noneMatch(piece -> !piece.getPossibleMoves(boardArray).isEmpty());
    }

    private void showWinnerDialog(Color winningColor) {
        String winner = winningColor == Color.WHITE ? "White" : "Black";
        JOptionPane.showMessageDialog(this, winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);

        resetGame();
    }

    private void resetGame() {
        initializePieces();
        game.resetGame();
        repaint();
    }
}

    

