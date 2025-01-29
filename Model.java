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
    public int score;
    //private static DrawModel instance;
    public Order[] orders; //orderを入れる配列
    private int gameTime = 5/*3*60 + 30*/; //　ゲーム時間は3分30秒 Yoshida

    public DrawModel() {
        //System.out.println("DrawModel instance: " + this);
        orders = new Order[5];
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
        grid[5][5].food = new Cabbage();  // (5,5)の位置に食材を配置 Yoshida

        grid[5][7].food = new Tomato(); // (5,7)の位置にトマトを配置 Yoshida

        grid[5][8].food = new Cucumber();

        grid[7][7].foodBox = 1; //(7,7)にキャベツボックスを配置 Yoshida
        grid[7][7].obstacle = true;
        grid[7][7].tool = 2;

        grid[8][7].foodBox = 2; //(8,7)にトマトボックスを配置 Yoshida
        grid[8][7].obstacle = true;
        grid[8][7].tool = 4;

        grid[9][7].foodBox = 3; //(9,7)にきゅうりボックスを配置 heiwa
        grid[9][7].obstacle = true;
        grid[9][7].tool = 5;
        
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

    /*
    public static DrawModel getInstance() { //現在使い方がわかっておりません Kome
        if (instance == null) {
            instance = new DrawModel();
        }
        return instance;
    }
    */

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
        player.move(dx, dy, grid);
    }
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
        for (int i = 0; i < orders.length; i++) {
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
            if (order != null && plate.matchesOrder(order)==true) {
                System.out.println(order.orderName + "が完成！");
                return order;
            }
        }
        return null;
    }
    public Order getOrder(int index) {
        if(index < orders.length || index >= 0)return orders[index];
        else return null;
    }
    public void scoreUp(Order order){
        switch(order.orderName){
            case "salad" : score += 30;
        }
        System.out.println("scoreUp()が呼ばれました");
        //これは料理が提供された瞬間の方がいいかも知れない
        for(int i=0; i<orders.length; i++){
            //if(orders[i].orderName == order.orderName) 
            if(orders[i] == order){ //こっちのほうが重複した料理があったときに対応できる
                removeOrder(i);
                return;
            }
        }
    }
    public void scoreDown(Order order){
        System.out.println("socreDown() called");
        if(score == 0) return;
        switch(order.orderName){
            case "salad" : score -= 30;
        }
        if(score < 0) score = 0;

        //これは料理が提供された瞬間の方がいいかも知れない
        //それな てかこれ失敗したときだからtrueにならんくね　Kome
        for(int i=0; i<orders.length; i++){
            if(orders[i].orderName == order.orderName){
                removeOrder(i);
                return;
            }
        }
    }
    public void removeOrder(int i){
        System.out.println("get =" + i);
        if (i >= 0 && i < orders.length && orders[i] != null) {
            orders[i].cancelTimer(); // タイマーの停止
            System.out.println("注文 " + orders[i].orderName + " を削除します。");
            orders[i] = null;
            formatOrder();
        }
    }
    private void formatOrder(){ //orderを前に詰めていくメソッド
        for(int s = 0; s < orders.length - 1; s++){
            for(int t = s; t < orders.length - 1; t++){
                if(orders[t] == null) {
                    orders[t] = orders[t+1];
                    if(orders[t] != null) { orders[t].orderIndex = t; }
                    orders[t+1] = null;
                }
            }
        }
    }

    //　以下時間に関わるメソッド Yoshida
    public int getGameTime(){
        return gameTime;
    }

    public void decreaseTime(){
        if(gameTime > 0){
            gameTime--;
        }
    }
}
