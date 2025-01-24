import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Player {
    public int x; //プレイヤーのx座標
    public int y; //プレイヤーのy座標
    private Food food;
    public Plate plate;
    public boolean hasPlate;
    private DrawModel model;
    public int direction; //プレイヤーの向きWASDの順で1(上),2(左),3(下),4(右)
    private Grid[][] grid;

    public Player(int x, int y, DrawModel model, Grid[][] grid) {
        this.x = x;
        this.y = y;
        this.food = null;
        this.plate = null;
        this.model = model;
        this.direction = 1; //初期の向きは上に設定してあるけど、別になんでも
        this.grid = grid;
        this.hasPlate = false;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public Food getFood() { return food; }

    public void move(int dx, int dy, Grid[][] grid) {
        int newX = x + dx;
        int newY = y + dy;
        //障害物と重ならないように(障害物である場合、移動を棄却する)
        if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length) {
            if (!grid[newX][newY].wall && !grid[newX][newY].obstacle && !grid[newX][newY].isCounter/*&& (newX != x || newY != y)*/) {
                x = newX;
                y = newY;
            }else{
                if(grid[newX][newY].wall) System.out.printf("wallに激突しました\n");
                if(grid[newX][newY].obstacle) System.out.printf("obstacleに激突しました\n");
            }
        }
    }

    public Grid getFrontGrid(){ //自分が立っている目の前のGridオブジェクトを返す関数
        if(direction == 1) return grid[x][y-1];
        else if(direction == 2) return grid[x-1][y];
        else if(direction == 3) return grid[x][y+1];
        else if(direction == 4) return grid[x+1][y];
        return null;
    }

    public void action() {
        Grid frontGrid = getFrontGrid();
        if(frontGrid.tool == 0){
            System.out.printf("アクションができる場所ではありません\n");
            return;
        }
        if (this.food == null) {
            System.out.println("食材を持っていません！");
            return;
        }else if(frontGrid.tool == 1 && food.canCut == true){
            food.foodStatus = 2; //これで切ったこととなるのだ Kome
            //food.cut();
            System.out.printf("食材を切りました\n");
            return;
        }
    }

    public void pick_up() {
        Grid currentGrid = grid[x][y]; //自分の足元のグリッド
        Grid frontGrid = getFrontGrid(); //自身の目の前のグリッド
        System.out.printf("frontGrid = (%d,%d)\n", frontGrid.x, frontGrid.y);
        if(hasPlate == false && frontGrid.tool == 3 ){ //playerは皿を持っていない かつ 目の前マスが皿ボックス
            System.out.println("皿を持ちました");
            plate = new Plate(); //ここでお皿をもった
            hasPlate = true; //皿を持つ
        }else if(hasPlate == false && frontGrid.isPlatePlaced == true){ //playerは皿を持っていない かつ 目の前マスに皿がある
            hasPlate = true; //皿を持つ
            plate = frontGrid.plate;
            frontGrid.isPlatePlaced = false; //目の前マスから皿を回収
            frontGrid.plate = null;
            //food = frontGrid.food;
            //frontGrid.food = null;
        }
        else if (food == null) {  // 何も持っていない場合
            if(frontGrid.foodBox == 1){ //目の前のマスがキャベツボックスだったら
                this.food = new Kyabetu();
                System.out.println("キャベツボックスから取得しました！");
            }
            else if(frontGrid.foodBox == 2){ //目の前のマスがトマトボックスだったら
                this.food = new Tomato();
                System.out.println("トマトボックスから取得しました！");
            }
            else if (frontGrid.hasFood()) {  // 現在のマスに食材がある場合
                food = frontGrid.food;  // 食材を拾う
                frontGrid.food = null;  // マスから食材を消す
                System.out.println("食材を持ち上げました！");
            } else {
                System.out.println("ここには食材がありません。");
            }
        }

    }

    public void put(){
        Grid currentGrid = grid[x][y];
        Grid frontGrid = getFrontGrid();
        
         //皿を持っていて 目の前がツールマスではなくカウンターでもない、目の前に食材なし
        if((hasPlate) && frontGrid.tool==0 && frontGrid.isCounter==false && frontGrid.food==null) {
            hasPlate = false; //皿を捨てる(置く)
            frontGrid.isPlatePlaced =true;
            frontGrid.plate = plate; //プレイヤーが持っている皿をグリッドにわたす
            plate = null; //プレイヤーは皿を離す
        }
        //皿を持ってて、目の前はツールマスではなくカウンターでもない、目の前に食材がある
        else if((hasPlate) && frontGrid.tool==0 && frontGrid.isCounter==false && frontGrid.food!=null){
            plate.add(frontGrid.food); //まず最初に自分のplateにfoodを追加する。
            frontGrid.isPlatePlaced = true;
            frontGrid.plate = plate;
            plate = null;
            hasPlate = false;
            frontGrid.food = null;
        }
        /* else */if(hasPlate==true && frontGrid.isCounter==true) { //いま皿を持っていて かつ 目の前がカウンター
            System.out.println("カウンターに提供します。");
            hasPlate = false; //皿を捨てる(置く)
            frontGrid.plate = plate;
            plate = null;
            frontGrid.isPlatePlaced =true;
            Order currentOrder = model.matchOrder(frontGrid.plate);
            if(currentOrder == null){// 料理が未完成の場合
                System.out.println("提供できません。");
                //提供できないから、皿が自分の手に返却される
                hasPlate = true;
                plate = frontGrid.plate;
                frontGrid.plate = null;
                frontGrid.isPlatePlaced = false;
                return;
            }
            System.out.println(currentOrder.orderName + "が提供されました！");
            if(currentOrder.isCompleted(frontGrid.plate) == true){
                model.scoreUp(currentOrder);
            }
            else model.scoreDown(currentOrder);
        }
        if(food != null) {  // 既に食材を持っている場合
            if(frontGrid.isPlatePlaced == true){ //目の前のマスに皿が置いてある場
                System.out.println("皿に食材を追加します！");
                frontGrid.plate.add(food);
                food = null;
                Order currentOrder = model.matchOrder(frontGrid.plate);
                System.out.println("皿に食材を追加しました！");
                frontGrid.plate.printPlate();
            }else if (!frontGrid.hasFood() && frontGrid.tool == 0) {  // 現在のマスが空いている場合 かつ そのマスがツールマスではない 
                frontGrid.food = food;  // 食材を置く
                food = null;  // 手持ちを空にする
                System.out.println("皿がないマスに対して食材を置きました！");
            }
            //こちら統合前コードになります 皿がないときにそもそも食材の結合をしないようにすれば良いから要らんかも Kome
            /*
            else if(frontGrid.hasFood()==true && frontGrid.tool == 0){ //目の間に食材あり かつ 目の前がツールマスではない
                frontGrid.food.addFood(this.food);
                food = null; //手持ちを空にする
                //model.setImageAtPosition(frontGrid.x, frontGrid.y, frontGrid.food.getImageId());
            }*/
            else {
                if(frontGrid.hasFood() == true) System.out.println("ここには既に食材があります！");
                if(frontGrid.tool != 0) System.out.printf("ここはツールなので食材は置けません");
            }
        }
    }
}