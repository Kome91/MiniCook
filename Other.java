import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;

class Grid {
    int x, y;
    boolean wall = false;
    boolean obstacle = false;
    Food food = null;
    Plate plate = null; //各グリッドはPlateという食材をいくつか持つクラスを持つ
    public boolean isPlatePlaced = false; //そのマスにさらがおかれているか
    public int foodBox = 0; //フードボックスがキャベツなら1、トマトなら2...みたいな感じ(ボックスが無ければ0) Yoshida
    public boolean plateBox = false; //皿ボックスだった場合trueになる
    /*
    0はツールではない,
    1は包丁,
    2はキャベツボックス,
    3は皿ボックス,
    4:トマトボックス,
    5:キュウリ,
    6:米,
    7:マグロ,
    8:イカ,
    9:のり,
    10:なべ,
    11:なべ(米),
    12:フライパン
    13:ゴミ箱
    14:キャンドル(特に効果はない)
    */
    public int tool = 0; 
    boolean isCounter; //そのマスがカウンターではないか
    public float cookingGauge = 0; //ご飯を炊いてる時のゲージ用　Yoshida

    public Grid(int x, int y) { this.x = x; this.y = y; }

    public boolean hasFood() { return food != null; }
}


class Waiter{
    int waitY = 1000; //ウェイタースタンバイ位置
    int receiveY = 710; //ウェイターが料理を受け取る場所
    boolean active = true;
    private Image imgMeal;
    private Image imgWaiterUp;
    private Image imgWaiterDown;
    DrawModel model;
    static final int xBefore = 470;
    static final int xAfter = 470;
    static final int counterX = 7;
    static final int counterY = 8;
    final int headerBlank;
    final int rightBlank;
    final int cellsize;
    int playerX;
    int flame = 0;
    static final int comeFlame = 90; //ウェイターが来るときの片道のフレーム数;
    public Waiter(DrawModel model, Image imgMeal, Image imgWaiterDown, Image imgWaiterUp, int headerBlank, int rightBlank, int playerX){
        this.model = model;
        this.imgMeal = imgMeal;
        this.cellsize = model.getCellSize();
        this.headerBlank = headerBlank;
        this.rightBlank = rightBlank;
        this.imgWaiterDown = imgWaiterDown;
        this.imgWaiterUp = imgWaiterUp;
        this.playerX = playerX;
    }
    public void drawMe(Graphics g, ImageObserver io){
        final int cS = cellsize;
        if(0 <= flame && flame < comeFlame){
            g.drawImage(imgMeal, playerX*cellsize + rightBlank, counterY*cellsize + headerBlank, cS, cS, io);
            //仮で正方形を描画してるよ
            g.setColor(Color.pink);
            g.drawImage(imgWaiterUp,xBefore-10, (int)((waitY*(comeFlame-flame) + receiveY*flame)/comeFlame) + rightBlank, cS+20, cS+20, io);
            flame++;
        }else if(comeFlame <= flame && flame < 2*comeFlame){
            g.drawImage(imgWaiterUp,xBefore-10, receiveY + rightBlank, cS+20, cS+20, io);
            flame++;
        }else if(2*comeFlame <= flame && flame < 3*comeFlame){
            g.drawImage(imgWaiterDown,xAfter-10, (int)((waitY*(flame-2*comeFlame) + receiveY*(3*comeFlame-flame))/comeFlame) + rightBlank, cS+20, cS+20, io);
            flame++;
        }else if(flame == 3*comeFlame){ active = false; flame++;}
    }
}
