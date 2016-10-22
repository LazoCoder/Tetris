import java.awt.*;

/**
 * Creates and contains the environment and rules of the game.
 * <br/>
 * Many of the methods work by creating a doppelganger of the piece before moving
 * the actual piece, to check for validity. For example, the method moveLeft() creates
 * a deep copy of the piece that is dropping and moves the clone piece one Square to the
 * left. Then it checks if the clone piece is in a valid position (in bounds and not
 * overlapping another piece). If the position is valid, then the original is moved over
 * to where the doppelganger is and the doppelganger is deleted. Otherwise, the original
 * piece is not moved.
 */
class GameBoard {

    private static int ROWS;
    private static int COLUMNS;

    private int[][] gameBoard;
    private Piece piece;

    private static int sizeOfPiece;

    private static int startPositionX;
    private static int startPositionY;

    private int score = 0;

    /**
     * Construct the game board.
     * @param rows      the number of rows
     * @param columns   the number of columns
     * @param size      the size of each square
     */
    GameBoard (int rows, int columns, int size) {
        ROWS = rows;
        COLUMNS = columns;
        sizeOfPiece = size;

        startPositionX = (COLUMNS / 2) - 2;
        startPositionY = 0;

        gameBoard = new int[ROWS][COLUMNS];
        createNewPiece();
    }

    /**
     * Create a new piece to drop from the top of the screen.
     * Exits the game if it cannot spawn a piece without overlapping
     * another piece.
     */
    private void createNewPiece () {
        piece = new Piece(startPositionX, startPositionY);
        if (isOverlappingAnotherPiece(piece)) {
            System.out.println("Final Score: " + score);
            System.exit(0);
        }
    }

    /**
     * Check to see if a piece is in bounds.
     * @param piece     the piece to check
     * @return          true if it is in bounds
     */
    private boolean isInBounds (Piece piece) {

        int[][] array = piece.getArray(); // Get the piece as an array.
        boolean xLessThanWidth, xMoreThanZero;
        boolean yLessThanHeight, yMoreThanZero;
        boolean xInBounds, yInBounds;

        // For each element in the array, check if it is out of bounds.
        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array[0].length; column++) {
                if (array[row][column] == 1) {

                    xLessThanWidth = (piece.getX() + column) < COLUMNS;
                    xMoreThanZero = (piece.getX() + column) >= 0;
                    yLessThanHeight = (piece.getY() + row) < ROWS;
                    yMoreThanZero = (piece.getY() + row) >= 0;

                    xInBounds = xLessThanWidth && xMoreThanZero;
                    yInBounds = yLessThanHeight && yMoreThanZero;

                    if (!xInBounds || !yInBounds) return false;

                }
            }
        }

        return true;
    }

    /**
     * Check to see if a piece if overlapping another piece.
     * @param piece     the piece to check
     * @return          true if it is overlapping another piece
     */
    private boolean isOverlappingAnotherPiece (Piece piece) {

        int[][] array = piece.getArray(); // Get the piece as an array.

        // For each element in the array, check if it overlaps another piece.
        for (int row = 0; row < array.length; row++)
            for (int column = 0; column < array[0].length; column++)
                if (array[row][column] == 1
                        && piece.getX() + column >= 0
                        && piece.getX() + column < COLUMNS
                        && piece.getY() + row >= 0
                        && piece.getY() + row< ROWS
                        && gameBoard[piece.getY() + row][piece.getX() + column] != 0)
                    return true;

        return false;

    }

    /**
     * When a piece is locked, it has stopped moving, and is not part of the
     * pieces on the floor of the Tetris screen.
     */
    private void lock () {

        int[][] array = piece.getArray();

        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array[0].length; column++) {
                if (array[row][column] == 1) {
                    gameBoard[piece.getY() + row][piece.getX() + column] = piece.getColor();
                }
            }
        }
    }

    /**
     * Moves the piece that is dropping down one Square if possible, otherwise lock it.
     * @return  true if the piece moved down successfully one square
     */
    boolean moveDown () {

        Piece clone = piece.clone();
        clone.moveDown();

        if (isInBounds(clone) && !isOverlappingAnotherPiece(clone)) {
            piece.moveDown();
            return true;
        }
        else {
            lock();
            createNewPiece();
            removeRow();
            return false;
        }

    }

    /**
     * Checks to see if a row has been filled, if so, removeRow it.
     */
    private void removeRow() {

        int identicalColumns;

        for (int row = 0; row < ROWS; row++) {

            identicalColumns = 0;

            for (int column = 0; column < COLUMNS; column++) {
                if (gameBoard[row][column] < 0) {
                    identicalColumns++;
                }
                if (identicalColumns == COLUMNS) {
                    removeRow(row);
                }
            }
        }
    }

    /**
     * Removes a row and moves all the pieces above it one Square down to fill the space.
     * @param rowToDelete   the row to delete
     */
    private void removeRow (int rowToDelete) {
        for (int row = rowToDelete; row > 0; row--) {
            for (int column = 0; column < COLUMNS; column++) {
                gameBoard[row][column] = gameBoard[row - 1][column];
            }
        }
        score += 10;
    }

    /**
     * Move the piece that is dropping left one Square.
     */
    void moveLeft () {
        Piece clone = piece.clone();
        clone.moveLeft();

        if (isInBounds(clone) && !isOverlappingAnotherPiece(clone)) {
            piece.moveLeft();
        }
    }

    /**
     * Move the piece that is dropping right one Square.
     */
    void moveRight () {
        Piece clone = piece.clone();
        clone.moveRight();

        if (isInBounds(clone) && !isOverlappingAnotherPiece(clone)) {
            piece.moveRight();
        }
    }

    /**
     * Rotates the piece that is dropping counter-clockwise.
     */
    void rotateCounterClockwise () {
        Piece clone = piece.clone();
        clone.rotateCounterClockwise();

        if (isInBounds(clone) && !isOverlappingAnotherPiece(clone)) {
            piece.rotateCounterClockwise();
        }
    }

    /**
     * Rotates the piece that is dropping clockwise.
     */
    void rotateClockwise () {
        Piece clone = piece.clone();
        clone.rotateClockwise();

        if (isInBounds(clone) && !isOverlappingAnotherPiece(clone)) {
            piece.rotateClockwise();
        }
    }

    void paint (Graphics graphics) {

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintBackground(g);
        paintAllPieces(g);
        paintActivePiece(g);

    }

    private void paintBackground (Graphics2D graphics) {
        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                if ((column + row) % 2 == 0) graphics.setColor(new Color(20, 20, 20));
                else graphics.setColor(new Color(24, 24, 24));
                graphics.fillRect(column * sizeOfPiece, row * sizeOfPiece, sizeOfPiece, sizeOfPiece);
            }
        }
    }

    private void paintAllPieces (Graphics2D graphics) {

        for (int row = 0; row < ROWS; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                if (gameBoard[row][column] < 0) {
                    paintPiece(graphics, row, column, pickHue(row, column));
                }
            }
        }
    }

    private void paintActivePiece (Graphics2D graphics) {

        graphics.setColor(Color.WHITE);
        int[][] array = piece.getArray();

        for (int row = 0; row < array.length; row++) {
            for (int column = 0; column < array[0].length; column++) {
                if (array[row][column] == 1) {
                    paintPiece(graphics, piece.getY() + row, piece.getX() + column,
                            pickHueForActivePiece());
                }
            }
        }
    }

    private void paintPiece (Graphics2D graphics, int row, int column, float hue) {

        int padding = sizeOfPiece/6;

        // Outer Square.
        int x1 = (row * sizeOfPiece);
        int x2 = (row * sizeOfPiece) + sizeOfPiece;
        int y1 = (column * sizeOfPiece);
        int y2 = (column * sizeOfPiece) + sizeOfPiece;

        // Inner square.
        int x3 = x1 + padding;
        int x4 = x1 + (sizeOfPiece - padding);
        int y3 = y1 + padding;
        int y4 = y1 + (sizeOfPiece - padding);

        // Left (bright)
        graphics.setColor(Color.getHSBColor(hue,0.6f,1));
        graphics.fillPolygon(new int[]{y1, y3, y3, y1}, new int[]{x1, x3, x4, x2}, 4);

        // Right (dark)
        graphics.setColor(Color.getHSBColor(hue,1,0.6f));
        graphics.fillPolygon(new int[]{y2, y4, y4, y2}, new int[]{x1, x3, x4, x2}, 4);

        // Top (brightest)
        graphics.setColor(Color.getHSBColor(hue,0.4f,1));
        graphics.fillPolygon(new int[]{y1, y3, y4, y2}, new int[]{x1, x3, x3, x1}, 4);

        // Bottom (darkest)
        graphics.setColor(Color.getHSBColor(hue,1,0.2f));
        graphics.fillPolygon(new int[]{y1, y3, y4, y2}, new int[]{x2, x4, x4, x2}, 4);

        // Fill center.
        graphics.setColor(Color.getHSBColor(hue,1,1));
        graphics.fillPolygon(new int[]{y3, y3, y4, y4}, new int[]{x3, x4, x4, x3}, 4);

    }

    /**
     * This method can be merge with pickHueForActivePiece().
     * @param row       the row of the piece
     * @param column    the column of the piece
     * @return          the hue based on the piece
     */
    private float pickHue (int row, int column) {
        if      (gameBoard[row][column] == -1) return 0.0f;
        else if (gameBoard[row][column] == -2) return 0.15f;
        else if (gameBoard[row][column] == -3) return 0.30f;
        else if (gameBoard[row][column] == -4) return 0.45f;
        else if (gameBoard[row][column] == -5) return 0.60f;
        else if (gameBoard[row][column] == -6) return 0.75f;
        else if (gameBoard[row][column] == -7) return 0.90f;
        return 0.0f;
    }

    /**
     * This method can be merge with pickHue().
     * @return      the hue based on the piece
     */
    private float pickHueForActivePiece () {
        if      (piece.getColor() == -1) return 0.0f;
        else if (piece.getColor() == -2) return 0.15f;
        else if (piece.getColor() == -3) return 0.30f;
        else if (piece.getColor() == -4) return 0.45f;
        else if (piece.getColor() == -5) return 0.60f;
        else if (piece.getColor() == -6) return 0.75f;
        else if (piece.getColor() == -7) return 0.90f;
        return 0.0f;
    }

}