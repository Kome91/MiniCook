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
        gameTime = 45; //sec ゲーム時間 Yoshida
        score = 0;
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
        player = new Player(2, 2, this, grid);

        grid[3][3].obstacle = true;
        grid[4][3].obstacle = true;
        grid[5][3].obstacle = true;
        grid[6][3].obstacle = true;
        grid[9][5].obstacle = true;
        grid[10][5].obstacle = true;
        grid[11][5].obstacle = true;
        grid[12][5].obstacle = true;

        grid[4][5].foodBox = 1;
        grid[4][5].obstacle = true;
        grid[4][5].tool = 2;

        grid[5][5].foodBox = 2;
        grid[5][5].obstacle = true;
        grid[5][5].tool = 4;

        grid[6][5].foodBox = 3;
        grid[6][5].obstacle = true;
        grid[6][5].tool = 5;

        grid[9][3].foodBox = 4;
        grid[9][3].obstacle = true;
        grid[9][3].tool = 6;

        grid[10][3].foodBox = 5;
        grid[10][3].obstacle = true;
        grid[10][3].tool = 7;

        grid[11][3].foodBox = 6;
        grid[11][3].obstacle = true;
        grid[11][3].tool = 8;

        grid[12][3].foodBox = 7;
        grid[12][3].obstacle = true;
        grid[12][3].tool = 9;
        
        //カウンターを設置 Yoshida
        grid[7][8].wall = true; //元々壁だったところをカウンターにしたい
        grid[7][8].isCounter = true;
        grid[8][8].wall = true; //元々壁だったところをカウンターにしたい
        grid[8][8].isCounter = true;

        grid[0][3].tool = 1;//ナイフ
        grid[0][4].tool = 1;//ナイフ
        grid[0][5].tool = 1;//ナイフ
        grid[15][3].tool = 1;//ナイフ
        grid[15][4].tool = 1;//ナイフ
        grid[15][5].tool = 1;//ナイフ

        grid[10][0].tool = 10;//なべ
        grid[11][0].tool = 10;//なべ
        grid[12][0].tool = 10;//なべ

        grid[3][0].tool = 12;//フライパン
        grid[4][0].tool = 12;//フライパン
        grid[5][0].tool = 12;//フライパン

        grid[3][5].plateBox = true;
        grid[3][5].obstacle = true;
        grid[3][5].tool = 3;

        grid[7][0].plateBox = true;
        grid[7][0].tool = 3; //皿ボックス
        grid[8][0].plateBox = true;
        grid[8][0].tool = 3; //皿ボックス

        grid[0][1].tool=13;
        grid[0][7].tool=13;
        grid[15][1].tool=13;
        grid[15][7].tool=13;

        grid[6][8].tool = 14;
        grid[9][8].tool = 14;
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
                //System.out.println("orders[" + i + "] はnullです 新しいオーダーを生成します");
                String randommenu=menu[random.nextInt(num_menu)];     
                orders[i] = new Order(randommenu, i , this);
                //orders[i] = new Order("tekkamaki", i , this);
                //System.out.println("生成されたオーダー: " + orders[i].orderName);
                break;
            } else {
            //System.out.println("orders[" + i + "] は存在しています: " + orders[i].orderName);
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
            case "salad" : score += 200; break;
            case "tekkamaki" : score += 160; break;
            case "kappamaki" : score += 160; break;
            case "tunanigiri" : score += 140; break;
            case "ikanigiri" : score += 140; break;
            case "kaisendon" : score += 220; break;
        }
        //System.out.println("scoreUp()が呼ばれました");
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
        //System.out.println("scoreDown() called");
        if(score == 0) return;
        if(order == null){
            score -= 60;
            if(score < 0) score = 0;
            return;
        }
        switch(order.orderName){
            case "salad" : score -= 120; break;
            case "tekkamaki" : score -= 100; break;
            case "kappamaki" : score -= 100; break;
            case "tunanigiri" : score -= 80; break;
            case "ikanigiri" : score -= 80; break;
            case "kaisendon" : score -= 120; break;
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
        //System.out.println("get =" + i);
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
        gameTime = 45; // sec
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
        grid[3][3].obstacle = true;
        grid[4][3].obstacle = true;
        grid[5][3].obstacle = true;
        grid[6][3].obstacle = true;
        grid[9][5].obstacle = true;
        grid[10][5].obstacle = true;
        grid[11][5].obstacle = true;
        grid[12][5].obstacle = true;

        grid[4][5].foodBox = 1;
        grid[4][5].obstacle = true;
        grid[4][5].tool = 2;

        grid[5][5].foodBox = 2;
        grid[5][5].obstacle = true;
        grid[5][5].tool = 4;

        grid[6][5].foodBox = 3;
        grid[6][5].obstacle = true;
        grid[6][5].tool = 5;

        grid[9][3].foodBox = 4;
        grid[9][3].obstacle = true;
        grid[9][3].tool = 6;

        grid[10][3].foodBox = 5;
        grid[10][3].obstacle = true;
        grid[10][3].tool = 7;

        grid[11][3].foodBox = 6;
        grid[11][3].obstacle = true;
        grid[11][3].tool = 8;

        grid[12][3].foodBox = 7;
        grid[12][3].obstacle = true;
        grid[12][3].tool = 9;
        
        //カウンターを設置 Yoshida
        grid[7][8].wall = true; //元々壁だったところをカウンターにしたい
        grid[7][8].isCounter = true;
        grid[8][8].wall = true; //元々壁だったところをカウンターにしたい
        grid[8][8].isCounter = true;

        grid[0][3].tool = 1;//ナイフ
        grid[0][4].tool = 1;//ナイフ
        grid[0][5].tool = 1;//ナイフ
        grid[15][3].tool = 1;//ナイフ
        grid[15][4].tool = 1;//ナイフ
        grid[15][5].tool = 1;//ナイフ

        grid[10][0].tool = 10;//なべ
        grid[11][0].tool = 10;//なべ
        grid[12][0].tool = 10;//なべ

        grid[3][0].tool = 12;//フライパン
        grid[4][0].tool = 12;//フライパン
        grid[5][0].tool = 12;//フライパン

        grid[3][5].plateBox = true;
        grid[3][5].obstacle = true;
        grid[3][5].tool = 3;

        grid[7][0].plateBox = true;
        grid[7][0].tool = 3; //皿ボックス
        grid[8][0].plateBox = true;
        grid[8][0].tool = 3; //皿ボックス

        grid[0][1].tool=13;
        grid[0][7].tool=13;
        grid[15][1].tool=13;
        grid[15][7].tool=13;

        grid[6][8].tool = 14;
        grid[9][8].tool = 14;
    }
}
