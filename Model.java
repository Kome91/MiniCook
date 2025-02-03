import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

class DrawModel extends JPanel {
    private final int xsize = 16; // グリッドの幅
    private final int ysize = 9; // グリッドの高さ
    private final int cellSize = 60; // 1マスの大きさ
    protected Grid[][] grid;
    private Player player;
    private Food food;
    public int score;
    //private static DrawModel instance;
    public Order[] orders; //orderを入れる配列
    private int gameTime;

    public DrawModel() {
        //System.out.println("DrawModel instance: " + this);
        gameTime = 100/*3*60 + 30*/; //　ゲーム時間は3分30秒 Yoshida
        orders = new Order[5];
        for(int i=0; i<5; i++){
            orders[i] = null;
        }
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

        grid[8][4].food = new Tuna();

        grid[7][7].foodBox = 1; //(7,7)にキャベツボックスを配置 Yoshida
        grid[7][7].obstacle = true;
        grid[7][7].tool = 2;

        grid[8][7].foodBox = 2; //(8,7)にトマトボックスを配置 Yoshida
        grid[8][7].obstacle = true;
        grid[8][7].tool = 4;

        grid[9][7].foodBox = 3; //(9,7)にきゅうりボックスを配置 heiwa
        grid[9][7].obstacle = true;
        grid[9][7].tool = 5;

        grid[10][7].foodBox = 4; //(7,7)にキャベツボックスを配置 Yoshida
        grid[10][7].obstacle = true;
        grid[10][7].tool = 6;

        grid[11][7].foodBox = 5; //(8,7)にトマトボックスを配置 Yoshida
        grid[11][7].obstacle = true;
        grid[11][7].tool = 7;

        grid[12][7].foodBox = 6; //(9,7)にきゅうりボックスを配置 heiwa
        grid[12][7].obstacle = true;
        grid[12][7].tool = 8;

        grid[13][7].foodBox = 7; //(9,7)にきゅうりボックスを配置 heiwa
        grid[13][7].obstacle = true;
        grid[13][7].tool = 9;
        
        //カウンターを設置 Yoshida
        grid[13][0].wall = false; //元々壁だったところをカウンターにしたい
        grid[13][0].isCounter = true;

        player = new Player(1, 1, this, grid);

        grid[3][3].obstacle = true;
        grid[3][4].obstacle = true;
        grid[3][5].obstacle = true;

        grid[0][2].tool = 12;//フライパン
        grid[0][3].tool = 1;//ナイフ
        grid[0][4].tool = 10;//なべ

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

    public void generateOrder() {
        String[] menu={"salad","tekkamaki","kappamaki","tunanigiri","ikanigiri","kaisendon"};
        int num_menu=6;
        Random random=new Random();
        for (int i = 0; i < orders.length; i++) {
            if (orders[i] == null) {
                System.out.println("orders[" + i + "] はnullです 新しいオーダーを生成します");
                String randommenu=menu[random.nextInt(num_menu)];     
                orders[i] = new Order(randommenu, i , this);
                //orders[i] = new Order("tekkamaki", i , this);
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
            case "tekkamaki" : score += 30;
            case "kappamaki" : score += 30;
            case "tunanigiri" : score += 10;
            case "ikanigiri" : score += 10;
            case "kaisendon" : score += 30;
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
            case "tekkamaki" : score -= 30;
            case "kappamaki" : score -= 30;
            case "tunanigiri" : score -= 10;
            case "ikanigiri" : score -= 10;
            case "kaisendon" : score -= 30;
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

    public void reset() {
        //System.out.println("DrawModel instance: " + this);
        gameTime = 10/*3*60 + 30*/;
        score = 0;
        for(int i=0; i<5; i++){
            //orders[i].cancelTimer();  
            orders[i] = null;
        }
        //grid = new Grid[xsize][ysize];
        for (int i = 0; i < xsize; i++) {
            for (int j = 0; j < ysize; j++) {
                //grid[i][j] = new Grid(i, j);
                //imageGrid[i][j] = '\0';
                grid[i][j].food = null;
                grid[i][j].plate = null;
                grid[i][j].isPlatePlaced = false;
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

        grid[10][7].foodBox = 4; //
        grid[10][7].obstacle = true;
        grid[10][7].tool = 6;

        grid[11][7].foodBox = 5; //
        grid[11][7].obstacle = true;
        grid[11][7].tool = 7;

        grid[12][7].foodBox = 6; //
        grid[12][7].obstacle = true;
        grid[12][7].tool = 8;

        grid[13][7].foodBox = 7; //
        grid[13][7].obstacle = true;
        grid[13][7].tool = 9;
        
        //カウンターを設置 Yoshida
        grid[13][0].wall = false; //元々壁だったところをカウンターにしたい
        grid[13][0].isCounter = true;

        //player = new Player(1, 1, this, grid);
        player.x = 1;
        player.y = 1;
        player.direction = 1;
        player.food = null;
        player.plate = null;
        player.hasPlate = false;

        grid[3][3].obstacle = true;
        grid[3][4].obstacle = true;
        grid[3][5].obstacle = true;

        grid[0][3].tool = 1;

        grid[0][6].plateBox = true;
        grid[0][6].tool = 3;
    }
}
