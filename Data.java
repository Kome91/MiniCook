import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Grid {
    int x, y;
    boolean wall = false;
    boolean obstacle = false;
    Food food = null;
    public boolean isPlatePlaced = false; //そのマスにさらがおかれているか
    public boolean foodBox = false; //フードボックスだった場合にtrueになる
    public boolean plateBox = false; //皿ボックスだった場合trueになる
    int tool = 0; //0はツールではない, 1は包丁, 2はキャベツボックス, 3は皿ボックス

    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean hasFood(){
        return food != null;
    }
}

abstract class Food { //継承させる前提のabstractクラス
    public int foodStatus; //食材のステータスの変数、何もしてなければ0になる
    public boolean canCut; //その食材がカット可能ならtrue
    public boolean canHeat; //その食材が加熱可能ならtrue
    public boolean isOnPlate; //皿の上に置かれているか
    public abstract int getImageIdFromStatus();
    /*
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
    */
}
class Kyabetu extends Food{
    public Kyabetu(){
        //0は未調理,1はカットされた状態
        this.foodStatus = 0;
        this.canCut = true;
        this.canHeat = false;
        this.isOnPlate = false;
    }
    public int getImageIdFromStatus(){
        if(foodStatus == 0) return 1;
        else if(foodStatus == 1) return 5;
        else{
            System.out.println("エラーです");
            return 0;
        }
    }
}
