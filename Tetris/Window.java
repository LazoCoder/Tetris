import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.lang.Thread.sleep;

/**
 * Runs a game of Tetris.
 */
public class Window extends JFrame {

    private Engine engine;
    private volatile GameBoard gameBoard;
    private static final int ROWS = 18;
    private static final int COLUMNS = 10;
    private static int sizeOfPieces = 35;

    private Window() {
        engine = createEngine();
        setWindowProperties();
        startGame();
    }

    private Engine createEngine () {
        Engine engine = new Engine();
        gameBoard = new GameBoard(ROWS, COLUMNS, sizeOfPieces);

        engine.setPreferredSize(new Dimension(COLUMNS * sizeOfPieces, ROWS * sizeOfPieces));
        Container cp = getContentPane();
        cp.add(engine);
        addKeyListener(new MyKeyAdapter());

        return engine;
    }

    private void setWindowProperties () {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Lazo's Tetris");
        setResizable(false);
        pack();
        setVisible(true);
    }

    private void startGame () {
        Thread th = new Thread(engine);
        th.start();
    }

    /**
     * Contains the game loop.
     */
    private class Engine extends JPanel implements Runnable {

        public void run () {

            long before = System.nanoTime();
            double elapsedTime = 0.0;
            double FPS = 2.0;

            while (true) {

                long now = System.nanoTime();
                elapsedTime += ((now-before)/1_000_000_000.0) * FPS;
                before = System.nanoTime();

                if (elapsedTime >= 1) {
                    gameBoard.moveDown();
                    elapsedTime--;
                }

                try {
                    sleep(10);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                repaint();
            }

        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            // Ensures that it will run smoothly on Linux.
            if (System.getProperty("os.name").equals("Linux")) {
                Toolkit.getDefaultToolkit().sync();
            }

            gameBoard.paint(graphics);
        }

    }

    private class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent keyEvent) {
            super.keyPressed(keyEvent);

            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                gameBoard.moveLeft();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                gameBoard.moveRight();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                gameBoard.rotateClockwise();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                gameBoard.moveDown();
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
                while (gameBoard.moveDown()) ;
            }

            engine.repaint();
        }

    }

    public static void main (String[] args) {
        if (args.length == 1 && args[0].equals("-p")) {
            sizeOfPieces = 10;
        }

        SwingUtilities.invokeLater(() -> new Window());
    }

}
