import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Grid {
    int x, y;
    boolean wall = false;
    boolean obstacle = false;
    Food food = null;
    String tool = "";

    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean hasFood(){
        return food != null;
    }

    public boolean isTool(String tool){
        return this.tool.equals(tool);
    }
}

class Food {
    int cooking_method; 

    public Food(){
        this.cooking_method = 0; //0は調理されてない状態
    }

    public void cut(){
        this.cooking_method = 1;
    }

    public void stir_fry(){
        this.cooking_method = 2;
    }

    public void grill(){
        this.cooking_method = 3;
    }

    public void cooked(int cooking_method) {
        switch (cooking_method) {
            case 1:
                cut();
                break;
            case 2:
                stir_fry();
                break;
            case 3:
                grill();
                break;
            default:
                System.out.println("無効な調理方法です。");
        }
    }
}

class Player {
    protected int x;
    protected int y;
    private int directionX = 0;
    private int directionY = 1;
    private Food food;
    private DrawModel model;

    public Player(int x, int y, DrawModel model) {
        this.x = x;
        this.y = y;
        this.food = null;
        this.model = model;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Food getFood() {
        return food;
    }

    public void move(int dx, int dy, Grid[][] grid) {
        int newX = x + dx;
        int newY = y + dy;

        if (!grid[newX][newY].wall && !grid[newX][newY].obstacle && !grid[newX][newY].hasFood() && grid[newX][newY].tool.isEmpty()) {
            x = newX;
            y = newY;
        }
        
        if(dx != 0 || dy != 0){
            directionX = dx;
            directionY = dy;
        }

        updatePlayerImage();
    }

    public Grid get_front(Grid[][] grid){
        int frontX = x + directionX;
        int frontY = y + directionY;

        if(frontX >= 0 && frontX < grid.length && frontY >= 0 && frontY < grid[0].length){
            return grid[frontX][frontY];
        }

        return null;
    }

    public boolean exist_food(){
        Grid front_grid = get_front(model.getGrid());
        return front_grid != null && front_grid.hasFood();
    }    

    // プレイヤーの正面に指定したツールがあるかを判定する関数
    private boolean in_front_of_tool(String tool, Grid[][] grid) {
        int frontX = x + directionX;
        int frontY = y + directionY;

        if (frontX >= 0 && frontX < grid.length && frontY >= 0 && frontY < grid[0].length) {
            return grid[frontX][frontY].isTool(tool);
        }
        return false;
    }


    public void cook() {
        Grid[][] grid = model.getGrid();
        
        if (food == null) {
            System.out.println("食材を持っていません！");
            return;
        }

        if (food.cooking_method == 0 && in_front_of_tool("cutting_board", grid)  && exist_food()) {
            food.cut();
            System.out.println("食材を切りました！");
        } else if (food.cooking_method == 1 && in_front_of_tool("pan", grid) && exist_food()) {
            food.stir_fry();
            System.out.println("食材を炒めました！");
        } else if (in_front_of_tool("grill", grid) && exist_food()) {
            food.grill();
            System.out.println("食材を焼きました！");
        }
    }

    public void PickUp_Put(Grid[][] grid) {
        Grid front_grid = get_front(model.getGrid());
        Grid currentGrid = model.getGrid()[x][y];

        if (food == null) {  // 何も持っていない場合
            if (exist_food()) {  // 現在のマスに食材がある場合
                food = front_grid.food;  // 食材を拾う
                front_grid.food = null;  // マスから食材を消す
                model.deleteImageAtPosition(front_grid.x, front_grid.y); // 画像を削除
                System.out.println("食材を持ち上げました！");
            } else {
                System.out.println("ここには食材がありません。");
            }
        } else {  // 既に食材を持っている場合
            if (!currentGrid.hasFood() && in_front_of_tool("cutting_board", grid) && food.cooking_method == 0) {  // 現在のマスが空いている場合
                front_grid.food = food;  // 食材を置く
                food = null;  // 手持ちを空にする
                model.setImageAtPosition(front_grid.x, front_grid.y, 1); // 新しい位置に食材画像を設定
                System.out.println("食材を置きました！");
            }
            else if(!currentGrid.hasFood() && in_front_of_tool("pan", grid) && food.cooking_method == 1){
                front_grid.food = food;  // 食材を置く
                food = null;  // 手持ちを空にする
                model.setImageAtPosition(x, y, 1); // 新しい位置に食材画像を設定
                System.out.println("食材を置きました！");
            } 
            else if(!currentGrid.hasFood() && in_front_of_tool("grill", grid)){
                front_grid.food = food;  // 食材を置く
                food = null;  // 手持ちを空にする
                model.setImageAtPosition(x, y, 1); // 新しい位置に食材画像を設定
                System.out.println("食材を置きました！");
            }
        }
    }

    // プレイヤーの画像を進行方向に合わせて変更するメソッド
    public void updatePlayerImage() {
        int imageID = 10;
        
        if (directionX == 0 && directionY == 1) {
            imageID = 10;  // 下向き
        } else if (directionX == 0 && directionY == -1) {
            imageID = 11;    // 上向き
        } else if (directionX == -1 && directionY == 0) {
            imageID = 12;  // 左向き
        } else if (directionX == 1 && directionY == 0) {
            imageID = 13; // 右向き
        }
    
        // 現在の位置に画像をセット
        model.setImageAtPosition(x, y, imageID);
    }
    
    public String getDirection() {
        if (directionX == 0 && directionY == 1) return "down";
        if (directionX == 0 && directionY == -1) return "up";
        if (directionX == -1 && directionY == 0) return "left";
        if (directionX == 1 && directionY == 0) return "right";
        return "down"; // デフォルト
    }
    
}

class DrawModel extends JPanel {
    private final int xsize = 16; // グリッドの幅
    private final int ysize = 9; // グリッドの高さ
    private final int cellSize = 60; // 1マスの大きさ
    protected Grid[][] grid;
    private Player player;
    private Food food;
    private int[][] imageGrid; // 各マスの画像IDを管理する2次元配列

    public DrawModel() {
        grid = new Grid[xsize][ysize];
        imageGrid = new int[xsize][ysize];
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                grid[i][j] = new Grid(i, j);
                imageGrid[i][j] = '\0';
                if (i == 0 || j == 0 || i == xsize - 1 || j == ysize - 1) {
                    grid[i][j].wall = true; // 外周を壁に設定
                }
            }
        }
        grid[5][5].food = new Food();  // (5,5)の位置に食材を配置
        imageGrid[5][5] = 1;
        player = new Player(1, 1, this);
        grid[3][3].tool = "cutting_board";
        grid[3][4].tool = "pan";
        grid[3][5].tool = "grill";
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

    public Food getFood() {
        return food;
    }

    public void movePlayer(int dx, int dy) {
        int prevX = player.getX();
        int prevY = player.getY();
        player.move(dx, dy, grid);

        deleteImageAtPosition(prevX, prevY);

        player.updatePlayerImage();
    }

    //特定の位置に画像IDを設定
    public void setImageAtPosition(int x, int y, int id) {
        if (x >= 0 && x < xsize && y >= 0 && y < ysize) {
            imageGrid[x][y] = id;
        }
    }

    //画像ID配列を取得
    public int[][] getImageGrid() {
        return imageGrid;
    }

    public void deleteImageAtPosition(int x, int y) {
        // プレイヤーのみの画像を削除
        if (grid[x][y].food == null) { // 食材がない場合のみ画像を削除
            imageGrid[x][y] = 0;
        }
    }
    
}

class DrawView extends JPanel {
    protected DrawModel model;
    private Image Image1;
    private Image Image2;
    private Image Image3;
    private Image ImagePlayer_down;
    private Image ImagePlayer_up;
    private Image ImagePlayer_left;
    private Image ImagePlayer_right;

    public DrawView(DrawModel m) {
        model = m;
        this.setFocusable(true);

        //画像読み込み
        Image1=new ImageIcon("kyabetu.png").getImage();
        Image2=new ImageIcon("houtyou.png").getImage();
        Image3=new ImageIcon("kansei.png").getImage();
        ImagePlayer_down=new ImageIcon("player_down.png").getImage();
        ImagePlayer_up=new ImageIcon("player_up.png").getImage();
        ImagePlayer_left=new ImageIcon("player_left.png").getImage();
        ImagePlayer_right=new ImageIcon("player_right.png").getImage();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[] size = model.getFieldSize();
        Grid[][] grid = model.getGrid();
        int[][] imageGrid = model.getImageGrid();
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

                //画像を描画
                Image selectedImage = null;
                switch (imageGrid[i][j]) {
                    case 1:
                        selectedImage = Image1;
                        break;
                    case 2:
                        selectedImage = Image2;
                        break;
                    case 3:
                        selectedImage = Image3;
                        break;
                    case 10:
                        selectedImage = ImagePlayer_down;
                        break;
                    case 11:
                        selectedImage = ImagePlayer_up;
                        break;
                    case 12:
                        selectedImage = ImagePlayer_left;
                        break;
                    case 13:
                        selectedImage = ImagePlayer_right;
                        break;
                    
                }
                if (selectedImage != null) {
                    g.drawImage(selectedImage, i * cellSize, j * cellSize, cellSize, cellSize, this);
                }

                // 食材の描画
                if (grid[i][j].food != null) {
                    switch (grid[i][j].food.cooking_method) {
                    }
                }
            }
        }
        // プレイヤーを描画
        Image playerImage = null;
        switch (model.getPlayer().getDirection()) {
            case "down":
                playerImage = ImagePlayer_down;
                break;
            case "up":
                playerImage = ImagePlayer_up;
                break;
            case "left":
                playerImage = ImagePlayer_left;
                break;
            case "right":
                playerImage = ImagePlayer_right;
                break;
        }
        
        if (playerImage != null) {
            g.drawImage(playerImage, player.x * cellSize, player.y * cellSize, cellSize, cellSize, this);
        }
        //プレイヤーが色ver
        //g.setColor(Color.GREEN);g.fillRect(player.x * cellSize, player.y * cellSize, cellSize - 1, cellSize - 1);

        // プレイヤーが食材を持っている場合、プレイヤーの上に食材の画像を重ねて描画
        if (player.getFood() != null) {
            Image heldFoodImage = null;
            switch (player.getFood().cooking_method) {
                case 0:
                    heldFoodImage = Image1; // 未調理の食材
                    break;
                case 1:
                    heldFoodImage = Image2; // 切った状態の食材
                    break;
                case 2:
                    heldFoodImage = Image3; // 炒めた状態の食材（仮）
                    break;
            }

            if (heldFoodImage != null) {
                // 少し小さめにしてプレイヤーの上に描画
                int offset = cellSize / 4;
                int foodSize = cellSize / 2;
                g.drawImage(heldFoodImage, player.x * cellSize + offset, player.y * cellSize + offset, foodSize, foodSize, this);
            }
        }

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

        model.setImageAtPosition(5, 5, 1);
        model.getGrid()[5][5].food = new Food();

        model.getPlayer().move(1, 1, model.getGrid());

        this.setBackground(Color.white);
        this.setTitle("Draw Editor");
        this.setSize(960, 720);
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

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                dy = -1;
                model.movePlayer(dx, dy);
                break;
            case KeyEvent.VK_S:
                dy = 1;
                model.movePlayer(dx, dy);
                break;
            case KeyEvent.VK_A:
                dx = -1;
                model.movePlayer(dx, dy);
                break;
            case KeyEvent.VK_D:
                dx = 1;
                model.movePlayer(dx, dy);
                break;
            case KeyEvent.VK_J: //Jキーで調理（切る→炒める→焼く）
                model.getPlayer().cook(); 
                break;
            case KeyEvent.VK_SPACE: //スペースキーで持ち上げる/置く
                model.getPlayer().PickUp_Put(model.getGrid());
                break;
        }
        
        // 再描画
        view.repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
