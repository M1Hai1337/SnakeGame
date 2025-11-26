import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.*;

void main() throws IOException {
    SnakeGame game = new SnakeGame();
    game.start();
}

class SnakeGame {
    private static final int WIDTH = 30;
    private static final int HEIGHT = 15;
    private static final int INITIAL_DELAY = 150;

    private Terminal terminal;
    private LinkedList<Point> snake;
    private Point food;
    private char direction;
    private boolean gameOver;
    private int score;

    public SnakeGame() throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        factory.setInitialTerminalSize(new TerminalSize(WIDTH + 2, HEIGHT + 5));
        terminal = factory.createTerminal();
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);

        snake = new LinkedList<>();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        direction = 'R';
        gameOver = false;
        score = 0;
        spawnFood();
    }

    public void start() throws IOException {
        long lastUpdate = System.currentTimeMillis();

        while (!gameOver) {
            // Handle input (non-blocking)
            KeyStroke keyStroke = terminal.pollInput();
            if (keyStroke != null) {
                handleInput(keyStroke);
            }

            // Update game at fixed intervals
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdate >= INITIAL_DELAY) {
                update();
                draw();
                lastUpdate = currentTime;
            }

            // Small sleep to prevent CPU spinning
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                break;
            }
        }

        // Game over screen
        terminal.clearScreen();
        terminal.setCursorPosition(WIDTH / 2 - 5, HEIGHT / 2 - 1);
        terminal.putString("GAME OVER!");
        terminal.setCursorPosition(WIDTH / 2 - 8, HEIGHT / 2);
        terminal.putString("Final Score: " + score);
        terminal.setCursorPosition(WIDTH / 2 - 10, HEIGHT / 2 + 1);
        terminal.putString("Press any key to exit");
        terminal.flush();

        terminal.readInput();
        terminal.exitPrivateMode();
        terminal.close();
    }

    private void handleInput(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Escape ||
            keyStroke.getCharacter() != null && keyStroke.getCharacter() == 'q') {
            gameOver = true;
            return;
        }

        if (keyStroke.getKeyType() == KeyType.ArrowUp && direction != 'D') {
            direction = 'U';
        } else if (keyStroke.getKeyType() == KeyType.ArrowDown && direction != 'U') {
            direction = 'D';
        } else if (keyStroke.getKeyType() == KeyType.ArrowLeft && direction != 'R') {
            direction = 'L';
        } else if (keyStroke.getKeyType() == KeyType.ArrowRight && direction != 'L') {
            direction = 'R';
        }
    }

    private void update() {
        Point head = snake.getFirst();
        Point newHead = switch (direction) {
            case 'U' -> new Point(head.x, head.y - 1);
            case 'D' -> new Point(head.x, head.y + 1);
            case 'L' -> new Point(head.x - 1, head.y);
            case 'R' -> new Point(head.x + 1, head.y);
            default -> head;
        };

        // Check wall collision
        if (newHead.x < 0 || newHead.x >= WIDTH || newHead.y < 0 || newHead.y >= HEIGHT) {
            gameOver = true;
            return;
        }

        // Check self collision
        if (snake.contains(newHead)) {
            gameOver = true;
            return;
        }

        snake.addFirst(newHead);

        // Check food collision
        if (newHead.equals(food)) {
            score += 10;
            spawnFood();
        } else {
            snake.removeLast();
        }
    }

    private void draw() throws IOException {
        terminal.clearScreen();

        // Draw top border
        terminal.setCursorPosition(0, 0);
        terminal.putString("+" + "-".repeat(WIDTH) + "+");

        // Draw game area
        for (int y = 0; y < HEIGHT; y++) {
            terminal.setCursorPosition(0, y + 1);
            terminal.putString("|");

            for (int x = 0; x < WIDTH; x++) {
                Point p = new Point(x, y);
                terminal.setCursorPosition(x + 1, y + 1);

                if (snake.contains(p)) {
                    if (snake.getFirst().equals(p)) {
                        terminal.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
                        terminal.putString("O");  // Head
                    } else {
                        terminal.setForegroundColor(TextColor.ANSI.GREEN);
                        terminal.putString("o");  // Body
                    }
                    terminal.setForegroundColor(TextColor.ANSI.DEFAULT);
                } else if (food.equals(p)) {
                    terminal.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
                    terminal.putString("*");  // Food
                    terminal.setForegroundColor(TextColor.ANSI.DEFAULT);
                } else {
                    terminal.putString(" ");
                }
            }

            terminal.setCursorPosition(WIDTH + 1, y + 1);
            terminal.putString("|");
        }

        // Draw bottom border
        terminal.setCursorPosition(0, HEIGHT + 1);
        terminal.putString("+" + "-".repeat(WIDTH) + "+");

        // Draw score
        terminal.setCursorPosition(0, HEIGHT + 2);
        terminal.putString("Score: " + score + " | Length: " + snake.size());
        terminal.setCursorPosition(0, HEIGHT + 3);
        terminal.putString("Arrow Keys to move | Q or ESC to quit");

        terminal.flush();
    }

    private void spawnFood() {
        Random rand = new Random();
        do {
            food = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
        } while (snake.contains(food));
    }
}

class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Point p)) return false;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
