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
    int tool = 0; //0はツールではない, 1は包丁, 2はキャベツボックス, 3は皿ボックス, 4:トマトボックス

    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean hasFood(){
        return food != null;
    }
}
class Food {
    public int cabbage = 0; // 0:未所持 1:カット 2:
    public int tomato = 0;
    public int cucumber = 0;
    public boolean canCut = false;
    public boolean canHeat = false;
    public boolean isOnPlate = false;
    public Food(int cabbage, int tomato, int cucumber, boolean canCut){
        this.cabbage = cabbage;
        this.tomato = tomato;
        this.cucumber = cucumber;
        this.canCut = canCut;
    }
    public void cut(){
        cabbage = 2; //キャベツを切った状態に
    }
    public int getImageId(){ //ステータスから適切な画像idをさくせいする
        if(cabbage==0 && tomato==0 && cucumber==0){
            System.out.println("エラーです。このような状態にはなりません");
            return 0;
        }else if(cabbage==1 && tomato==0 && cucumber==0){
            System.out.println("未加工キャベツのImgIdが取得されました");
            return 1;
        }else if(cabbage==2 && tomato==0 && cucumber==0){
            System.out.println("カットキャベツのImgIdが取得されました");
            return 5;
        }else if(cabbage==0 && tomato==1 && cucumber==0){
            System.out.println("カットキャベツのImgIdが取得されました");
            return 8;
        }else{
            System.err.println("回答になりえない状態になっています");
            return 0;
        }
    }
}

/*
abstract class Food { //継承させる前提のabstractクラス
    public int foodStatus; //食材のステータスの変数、何もしてなければ0になる
    public boolean canCut; //その食材がカット可能ならtrue
    public boolean canHeat; //その食材が加熱可能ならtrue
    public boolean isOnPlate; //皿の上に置かれているか
    public abstract int getImageIdFromStatus();
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
*/