import java.util.Random;

/**
 * Represents a Tetris piece. Each Tetris piece has a shape, as there are different
 * kinds of pieces. The shape is selected randomly when constructed.
 */
class Piece {

    private Shape shape; // The shape of the piece.
    private int[][] array; // Stores the shape.
    private int x, y;
    private int color; // Color is an int as I want to pick the colors outside this class.

    /**
     * Construct a random piece at the specified coordinate.
     * @param x     the x coordinate
     * @param y     the y coordinate
     */
    Piece (int x, int y) {
        shape = getRandomShape();
        this.x = x;
        this.y = y;
        createPiece();
    }

    /**
     * Get a random type of piece.
     * @return      a random shape
     */
    private Shape getRandomShape () {
        int random = new Random().nextInt(Shape.values().length);
        return Shape.values()[random];
    }

    /**
     * Create the piece in the array.
     */
    private void createPiece () {

        if (shape == Shape.Tower)                 createTower();
        else if (shape == Shape.Square)           createSquare();
        else if (shape == Shape.LightningRight)   createLightningRight();
        else if (shape == Shape.LightningLeft)    createLightningLeft();
        else if (shape == Shape.HammerLeft)       createHammerLeft();
        else if (shape == Shape.HammerRight)      createHammerRight();
        else if (shape == Shape.Hat)              createHat();

    }

    private void createTower() {

        array = new int[][]{
                { 0, 1, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 1, 0, 0 }
        };

        color = -1;

    }

    private void createSquare() {

        array = new int[][]{
                { 1, 1 },
                { 1, 1 }
        };

        color = -2;

    }

    private void createLightningLeft() {

        array = new int[][]{
                { 0, 0, 1 },
                { 0, 1, 1 },
                { 0, 1, 0 }
        };

        color = -3;

    }

    private void createLightningRight() {

        array = new int[][]{
                { 1, 0, 0 },
                { 1, 1, 0 },
                { 0, 1, 0 }
        };

        color = -4;

    }

    private void createHammerLeft() {

        array = new int[][]{
                { 1, 1, 0 },
                { 0, 1, 0 },
                { 0, 1, 0 }
        };

        color = -5;

    }

    private void createHammerRight() {

        array = new int[][]{
                { 0, 1, 1 },
                { 0, 1, 0 },
                { 0, 1, 0 }
        };

        color = -6;

    }

    private void createHat() {

        array = new int[][]{
                { 1, 0, 0 },
                { 1, 1, 0 },
                { 1, 0, 0 }
        };

        color = -7;

    }

    /**
     * Decides which method to use to rotate the piece counter-clockwise.
     */
    void rotateCounterClockwise () {
        if (shape == Shape.Square) { // Rotation does not affect Square.
            return;
        } else if (shape == Shape.Tower) { // Tower rotates in its own special way.
            doRotateTower();
        } else {
            doRotateCounterClockwise();
        }
    }

    /**
     * Decides which method to use to rotate the piece clockwise.
     */
    void rotateClockwise () {
        if (shape == Shape.Square) { // Rotation does not affect Square.
            return;
        } else if (shape == Shape.Tower) { // Tower rotates in its own special way.
            doRotateTower();
        } else {
            doRotateClockwise();
        }
    }

    /**
     * Rotates the piece counter-clockwise.
     */
    private void doRotateCounterClockwise () {
        int[][] newArray = new int[3][3];

        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[0].length; y++) {
                if (array[y][x] == 1) {
                    newArray[array.length - 1 - x][y] = 1;
                }
            }
        }

        array = newArray;
    }

    /**
     * Rotates the piece clockwise.
     */
    private void doRotateClockwise () {

        int[][] newArray = new int[3][3];

        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array[0].length; y++) {
                if (array[y][x] == 1) {
                    newArray[array.length - 1 - x][y] = 1;
                }
            }
        }

        array = newArray;
    }

    /**
     * The tower is rotated differently than all the other pieces.
     * It only has two position. If it is rotated like the other pieces
     * then it may switch between column 2 and 3 when it is rotated to
     * the vertical. This looks strange.
     */
    private void doRotateTower () {

        if (array[0][1] == 1) {

            array = new int[][]{
                    { 0, 0, 0, 0 },
                    { 1, 1, 1, 1 },
                    { 0, 0, 0, 0 },
                    { 0, 0, 0, 0 }
            };

        }
        else createTower();
    }

    int getX () {
        return x;
    }

    int getY () {
        return y;
    }

    int getColor () {
        return color;
    }

    void moveDown () {
        y++;
    }

    void moveLeft () {
        x--;
    }

    void moveRight () {
        x++;
    }

    public Piece clone () {
        Piece piece = new Piece(x, y);
        piece.shape = this.shape;
        piece.array = this.array.clone();
        piece.x = this.x;
        piece.y = this.y;
        piece.color = this.color;
        return piece;
    }

    int[][] getArray () {
        return array.clone();
    }

    public String toString () {
        StringBuilder sb = new StringBuilder();

        for (int x = 0; x < array.length; x++) {
            for (int y = 0; y < array.length; y++) {
                sb.append(array[x][y] + " ");
            }
            sb.append("\n");
        }

        return new String(sb);
    }

    private enum Shape {
        Tower,
        Square,
        LightningLeft,
        LightningRight,
        HammerLeft,
        HammerRight,
        Hat
    }

}
