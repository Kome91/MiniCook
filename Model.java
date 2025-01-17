import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        grid[5][5].food = new Food(1,0,0,true);  // (5,5)の位置にキャベツを配置
        //imageGrid[5][5] = 1;

        grid[5][7].food = new Food(0,1,0,true); // (5,7)の位置にトマトを配置
        //imageGrid[5][7] = 8;

        grid[7][7].foodBox = true; //(7,7)にキャベツボックスを配置
        grid[7][7].obstacle = true;
        grid[7][7].tool = 2;

        grid[9][7].foodBox = true; //(9,7)にトマトボックスを配置
        grid[9][7].obstacle = true;
        grid[9][7].tool = 4;
        
        player = new Player(1, 1, this, grid);

        grid[3][3].obstacle = true;
        grid[3][4].obstacle = true;
        grid[3][5].obstacle = true;

        grid[0][3].tool = 1;

        grid[0][6].plateBox = true;
        grid[0][6].tool = 3;
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
        //player.move(dx, dy, grid, player);
        player.move(dx, dy, grid);
    }
    // 特定の位置に障害物を設定
    public void setObstacleAtPosition(int x, int y) {
        if (x >= 0 && x < xsize && y >= 0 && y < ysize) {
            //grid[x][y].obstacle = true;
        }
    }
    //特定の位置に画像IDを設定
    public void setImageAtPosition(int x, int y, int id) {
        if (x >= 0 && x < xsize && y >= 0 && y < ysize) {
            imageGrid[x][y] = id;
            setObstacleAtPosition(x, y); //<-これはなくていいかもしれない
        }
    }

    //画像ID配列を取得
    public int[][] getImageGrid() {
        return imageGrid;
    }

    public void deleteImageAtPosition(int x, int y){
        if (x >= 0 && x < xsize && y >= 0 && y < ysize) {
            imageGrid[x][y] = 0;
        }
    }
    public void printInfo(){
        System.out.println("<デバッグ用情報>");
    }
}
