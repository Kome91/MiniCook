import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Grid {
    int x, y;
    boolean wall = false;
    boolean obstacle = false;
    Food food = null;
    Plate plate = null; //各グリッドはPlateという食材をいくつか持つクラスを持つ
    public boolean isPlatePlaced = false; //そのマスにさらがおかれているか
    public int foodBox = 0; //フードボックスがキャベツなら1、トマトなら2...みたいな感じ(ボックスが無ければ0) Yoshida
    public boolean plateBox = false; //皿ボックスだった場合trueになる
    public int tool = 0; //0はツールではない, 1は包丁, 2はキャベツボックス, 3は皿ボックス, 4:トマトボックス
    boolean isCounter; //そのマスがカウンターではないか

    public Grid(int x, int y) { this.x = x; this.y = y; }

    public boolean hasFood() { return food != null; }
}

abstract class Food { //継承させる前提のabstractクラス
    public int foodStatus; //食材のステータスの変数、何もしてなければ0になる カットしてたら1
    public boolean canCut; //その食材がカット可能ならtrue
    public boolean canHeat; //その食材が加熱可能ならtrue
    public boolean isOnPlate; //皿の上に置かれているか
    public String foodName;
    public abstract int getFoodStatus();

    public Food(int foodStatus, boolean canCut, boolean canHeat, boolean isOnPlate, String foodName){
        //ここにFoodのスペルが正しいか調べる処理を入れないと、エグいバグを生む気がします Kome
        //↑いや、そもそもコンストラクタでしか、代入しないから別に問題ないわ Kome
        this.foodStatus = foodStatus;
        this.canCut = canCut;
        this.canHeat = canHeat;
        this.isOnPlate = isOnPlate;
        this.foodName = foodName;
    }
}

//Foodクラスを継承したKyabetuクラスです
class Kyabetu extends Food{
    public Kyabetu(){
        super(1, true, false, false, "Kyabetu");
    }

    public int getFoodStatus(){ //そのフードの状態を返す
        return foodStatus;
    }
}

//Foodクラスを継承したTomatoクラスです
class Tomato extends Food{
    public Tomato(){
        super(1, true, false, false, "Tomato");
    }
    public int getFoodStatus(){ //そのフードの状態を返す
        return foodStatus;
    }
}

class Plate {
    Food[] foods;
    public Plate(){
        foods = new Food[3];
        foods[0] = null;
        foods[1] = null;
        foods[2] = null;
    }

    public boolean hasAnyFood(){ //plateになにかしら乗っているかのboolean
        if(foods[0]==null && foods[1]==null && foods[2]==null) return false;
        else return true;
    }

    public void add(Food food) {
        for (int i = 0; i < foods.length; i++) {
            if(foods[i] != null && foods[i].foodName == food.foodName) { continue; }
            if (foods[i] == null) {
                foods[i] = food;
                System.out.println(food.foodName + " を皿に追加しました。");
                return;  // 追加が完了したら終了
            }
        }
        System.out.println("これ以上皿に食材を追加できません。");
    }

    public Food get(int i){
        if(i<0 || i>=foods.length){return null;}
        else return foods[i];
    }

    //これっている?
    /*
    public int getImageByFood(Order currentOrder) {
        if (currentOrder == null) {
            System.out.println("Order が null です。");
            return 7; // 空の皿の画像IDを返す
        }

        boolean[] matchedIngredients = new boolean[3];
    
        Food[] requiredIngredients = {currentOrder.ingredient1, currentOrder.ingredient2, currentOrder.ingredient3};
    
        // Plateの食材とOrderの食材を比較
        for (int i = 0; i < foods.length; i++) {
            for (int j = 0; j < requiredIngredients.length; j++) {
                if (requiredIngredients[j] == null){
                    matchedIngredients[j] = true;
                    break;
                }
                if (!matchedIngredients[j] && foods[i] != null && requiredIngredients[j] != null) {
                    if (foods[i].getClass() == requiredIngredients[j].getClass() &&
                        foods[i].foodStatus == requiredIngredients[j].foodStatus) {
                        matchedIngredients[j] = true;
                        break;
                    }
                }
            }
        }
    
        // 全ての食材が揃っていれば完成画像IDを返す
        boolean isComplete = true;
        for (boolean matched : matchedIngredients) {
            if (requiredIngredients != null && !matched) {
                System.out.println(currentOrder.orderName+"は未完成です！");
                isComplete = false;
                break;
            }
        }
    
        if (isComplete) {
            if("salad".equals(currentOrder.orderName))return 10; // 完成品の画像ID（例）
            else return 10;
        } else if (foods[0] == null && foods[1] == null && foods[2] == null) {
            return 7; // 空の皿の画像ID
        } else {
            return 7; // 未完成の画像ID
        }
    }
    */

    public void printPlate(){
        String state = "";
        System.out.print("現在、皿の上には：");
        for(int i=0; i<3; i++){
            if(foods[i] != null) {
                switch(foods[i].foodStatus){
                    case 1: state = "raw"; break;
                    case 2: state = "cut"; break;
                    case 3: state = "grilled"; break;
                }
                System.out.print(foods[i].foodName+"("+ state + ")" + "　");
            }
        }
        System.out.print("\n");
        return ;
    }

    public boolean matchesOrder(Order order) {
        boolean[] matchedIngredients = new boolean[3];
        Food[] orderIngredients = {order.ingredient1, order.ingredient2, order.ingredient3};

        for (int i = 0; i < foods.length; i++) {
            for (int j = 0; j < orderIngredients.length; j++) {
                if(orderIngredients[j] == null){
                    matchedIngredients[j] = true;
                    continue;
                }
                if (!matchedIngredients[j] && foods[i] != null) {
                    if (foods[i].getClass() == orderIngredients[j].getClass() &&
                        foods[i].foodStatus == orderIngredients[j].foodStatus) {
                        System.out.println(foods[i].foodName + "は満たされました。");
                        matchedIngredients[j] = true;
                        break;
                    }
                }
            }
        }

        for(int i=0; i<matchedIngredients.length; i++){
            if(matchedIngredients[i]){
                System.out.println("材料"+(i+1)+"は満たされいます。");
            }
            else System.out.println("材料"+(i+1)+"は満たされいません。");
        }

        for (boolean matched : matchedIngredients) {
            if (!matched){
                System.out.println("料理は未完成です。");
                return false;
            }
        }
        System.out.println("料理は完成しています。");
        return true;
    }

}

class Order {
    String orderName;
    boolean hasPlate; //まず皿が必要
    Food ingredient1;
    Food ingredient2;
    Food ingredient3; //材料は多くて3つまで？
    private int timeLimit; //制限時間
    private long createTime; //注文が作成された時間
    private Timer expirationTimer; // 自動削除用タイマー

    public Order(String orderName, int orderIndex, DrawModel model){
        //コンストラクタでは完成形の値を設定
        this.orderName = orderName;
        this.hasPlate = true;
        this.createTime = System.currentTimeMillis();

        //オーダーによって必要な食材や状態(切られてる、焼かれてる等)を設定
        if("salad".equals(orderName)){
            System.out.println("Order created: " + this.orderName);
            this.timeLimit = 45;

            this.ingredient1 = new Kyabetu();
            this.ingredient1.foodStatus = 2;
            this.ingredient1.isOnPlate = true;

            this.ingredient2 = new Tomato();
            this.ingredient2.foodStatus = 2;
            this.ingredient2.isOnPlate = true;

            this.ingredient3 = null;
        }

        // 制限時間後に削除するタイマーを設定
        expirationTimer = new Timer(timeLimit * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.removeOrder(orderIndex);
                System.out.println(orderName + " の制限時間が切れました！");
            }
        });
        expirationTimer.setRepeats(false); // 一度だけ実行
        expirationTimer.start();
    }

    public boolean isCompleted(Plate plate) { //オーダー判定処理 Kome
        boolean[] matchedIngredients = new boolean[3];
        Food[] orderIngredients = {ingredient1, ingredient2, ingredient3};
    
        for (int i = 0; i < plate.foods.length; i++) {
            for (int j = 0; j < orderIngredients.length; j++) {
                if (!matchedIngredients[j] && plate.foods[i] != null && orderIngredients[j] != null) {
                    if (plate.foods[i].getClass() == orderIngredients[j].getClass() &&
                        plate.foods[i].foodStatus == orderIngredients[j].foodStatus) {
                        matchedIngredients[j] = true;
                        break;
                    }
                }
            }
        }
    
        for (boolean matched : matchedIngredients) {
            if (matched == false){
                return false;
            }
        }
        return true;
    }

    // 残り時間を計算
    public int getRemainingTime(){
        long elapsedTime = (System.currentTimeMillis() - createTime)/1000;
        return (int) (timeLimit - elapsedTime);
    }

    // 注文の期限切れ確認
    public boolean isExpired(){
        return getRemainingTime() <= 0;
    }
    
    // タイマーの停止（手動で注文を削除するとき用）
    public void cancelTimer() {
        expirationTimer.stop();
    }

    public String getOrderName() {
        return orderName;
    }
}