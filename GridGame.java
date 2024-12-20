import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Grid {
    int x, y;
    boolean wall = false;
    boolean obstacle = false;

    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Player {
    protected int x;
    protected int y;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move(int dx, int dy, Grid[][] grid) {
        int newX = x + dx;
        int newY = y + dy;

        if (!grid[newX][newY].wall && !grid[newX][newY].obstacle) {
            x = newX;
            y = newY;
        }
    }
}

class DrawModel extends JPanel {
    private final int xsize = 21; // グリッドの幅
    private final int ysize = 21; // グリッドの高さ
    private final int cellSize = 20; // 1マスの大きさ
    protected Grid[][] grid;
    private Player player;

    public DrawModel() {
        grid = new Grid[xsize][ysize];
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                grid[i][j] = new Grid(i, j);
                if (i == 0 || j == 0 || i == xsize - 1 || j == ysize - 1) {
                    grid[i][j].wall = true; // 外周を壁に設定
                }
            }
        }
        player = new Player(1, 1);
    }

    public Grid[][] getGrid() {
        return grid;
    }

    public int[] getFieldSize() {
        return new int[]{xsize, ysize};
    }

    public int getCellSize() {
        return cellSize;
    }

    public Player getPlayer() {
        return player;
    }

    public void movePlayer(int dx, int dy) {
        player.move(dx, dy, grid);
    }
}

class DrawView extends JPanel {
    protected DrawModel model;

    public DrawView(DrawModel m) {
        model = m;
        this.setFocusable(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[] size = model.getFieldSize();
        Grid[][] grid = model.getGrid();
        int cellSize = model.getCellSize();
        Player player = model.getPlayer();
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                if (grid[i][j].wall) {
                    g.setColor(Color.GRAY);
                } else if (grid[i][j].obstacle) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(i * cellSize, j * cellSize, cellSize - 1, cellSize - 1);
            }
        }
        // プレイヤーを描画
        g.setColor(Color.GREEN);
        g.fillRect(player.x * cellSize, player.y * cellSize, cellSize - 1, cellSize - 1);
    }
}

class GridGame extends JFrame {
    DrawModel model;
    DrawView view;
    DrawController cont;

    public GridGame() {
        model = new DrawModel();
        view = new DrawView(model);
        cont = new DrawController(model, view);
        view.addKeyListener(cont); // キーリスナーを設定
        this.setBackground(Color.white);
        this.setTitle("Draw Editor");
        this.setSize(500, 500);
        this.add(view);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new GridGame();
    }
}

class DrawController implements KeyListener {
    protected DrawModel model;
    protected DrawView view;

    public DrawController(DrawModel m, DrawView v) {
        model = m;
        view = v;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int dx = 0, dy = 0;

        // キーコードに応じた移動量を設定
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                dy = -1;
                break;
            case KeyEvent.VK_DOWN:
                dy = 1;
                break;
            case KeyEvent.VK_LEFT:
                dx = -1;
                break;
            case KeyEvent.VK_RIGHT:
                dx = 1;
                break;
        }

        // プレイヤーを移動
        model.movePlayer(dx, dy);

        // 再描画
        view.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
