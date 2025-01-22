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
    //private int[][] imageGrid; // 各マスの画像IDを管理する2次元配列
    private int score;
    private static DrawModel instance;
    private Order[] orders; //orderを入れる配列

    public DrawModel() {
        System.out.println("DrawModel instance: " + this);
        orders = new Order[3];
        orders[0] = null;
        orders[1] = null;
        orders[2] = null;
        grid = new Grid[xsize][ysize];
        //imageGrid = new int[xsize][ysize];
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                grid[i][j] = new Grid(i, j);
                //imageGrid[i][j] = '\0';
                if (i == 0 || j == 0 || i == xsize - 1 || j == ysize - 1) {
                    grid[i][j].wall = true; // 外周を壁に設定
                }
            }
        }
        grid[5][5].food = new Kyabetu();  // (5,5)の位置に食材を配置 Yoshida
        //grid[5][5].food = new Food(1,0,0,true);  // (5,5)の位置にキャベツを配置 Kome
        //imageGrid[5][5] = 1;

        grid[5][7].food = new Tomato(); // (5,7)の位置にトマトを配置 Yoshida
        //grid[5][7].food = new Food(0,1,0,true); // (5,7)の位置にトマトを配置 Kome
        //imageGrid[5][7] = 8;

        grid[7][7].foodBox = 1; //(7,7)にキャベツボックスを配置 Yoshida
        //grid[7][7].foodBox = true; //(7,7)にキャベツボックスを配置 Kome
        grid[7][7].obstacle = true;
        grid[7][7].tool = 2;

        grid[8][7].foodBox = 2; //(8,7)にトマトボックスを配置 Yoshida
        //grid[8][7].foodBox = true; //(8,7)にトマトボックスを配置 Kome
        grid[8][7].obstacle = true;
        grid[8][7].tool = 4;
        
        //カウンターを設置 Yoshida
        grid[13][0].wall = false; //元々壁だったところをカウンターにしたい
        grid[13][0].isCounter = true;

        player = new Player(1, 1, this, grid);

        grid[3][3].obstacle = true;
        grid[3][4].obstacle = true;
        grid[3][5].obstacle = true;

        grid[0][3].tool = 1;

        grid[0][6].plateBox = true;
        grid[0][6].tool = 3;
    }

    public static DrawModel getInstance() { //現在使い方がわかっておりません Kome
        if (instance == null) {
            instance = new DrawModel();
        }
        return instance;
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
    /*
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
    */
    public void printInfo(){
        System.out.println("<デバッグ用情報>");
        // デバッグ用
        System.out.println("orders配列の状態:");
        for (int i = 0; i < 3; i++) {
            if (orders[i] != null) {
                System.out.println("orders[" + i + "]: " + orders[i].orderName);
            } else {
                System.out.println("orders[" + i + "]: null");
            }
        }
    }

    /*public void generateOrder(){
        for(int i=0; i<3; i++){
            if(orders[i] == null){// 配列ordersの空いてるところに新しい注文を入れる
                orders[i] = new Order("salad"); //ここの料理名はあとで乱数で決めたりする予定。とりあえずサラダ
                System.out.println("注文" + (i+1) + "：" + orders[i].orderName);
                break;
            }
        }
    } */
    public void generateOrder() {
        for (int i = 0; i < 3; i++) {
            if (orders[i] == null) {
                System.out.println("orders[" + i + "] はnullです 新しいオーダーを生成します");
                orders[i] = new Order("salad", i , this);
                System.out.println("生成されたオーダー: " + orders[i].orderName);
                break;
            } else {
            System.out.println("orders[" + i + "] は存在しています: " + orders[i].orderName);
            }
        }
    }
    public Order matchOrder(Plate plate) {
        for (Order order : orders) {
            if (order != null && plate.matchesOrder(order)) {
                System.out.println(order.orderName + "が完成！");
                return order;
            }
        }
        return null;
    }
    public String[] getOrders() {
        String[] orderDescriptions = new String[3];
        for (int i = 0; i < 3; i++) {
            if (orders[i] != null) {
                orderDescriptions[i] = "Order " + (i + 1) + ": " + orders[i].orderName; // 料理名を表示
            } else {
                orderDescriptions[i] = "Order " + (i + 1) + ": (empty)"; // 注文がない場合
            }
        }
        /*// デバッグ用に追加
        for (String desc : orderDescriptions) {
            System.out.println(desc);
        }*/

        return orderDescriptions;
    }
    public void scoreUp(Order order){
        switch(order.orderName){
            case "salad" : score += 30;
        }

        //これは料理が提供された瞬間の方がいいかも知れない
        for(int i=0; i<3; i++){
            if(orders[i].orderName == order.orderName){
                removeOrder(i);
                return;
            }
        }
    }
    public void scoreDown(Order order){
        if(score == 0) return;
        switch(order.orderName){
            case "salad" : score -= 30;
        }
        if(score < 0) score = 0;

        //これは料理が提供された瞬間の方がいいかも知れない
        for(int i=0; i<3; i++){
            if(orders[i].orderName == order.orderName){
                removeOrder(i);
                return;
            }
        }
    }
    public void removeOrder(int i){
        if (i >= 0 && i < orders.length && orders[i] != null) {
            orders[i].cancelTimer(); // タイマーの停止
            System.out.println("注文 " + orders[i].orderName + " を削除します。");
            orders[i] = null;
        }
    }

}
