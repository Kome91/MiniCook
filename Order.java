import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;

class Order {
    String orderName;
    double posAnim;
    int subOrderPosY = 110;
    double subOrderPosYAnim = 40;
    boolean hasPlate; //まず皿が必要
    public Food ingredient1;
    public Food ingredient2;
    public Food ingredient3; //材料は多くて3つまで
    public int timeLimit; //制限時間
    public int orderIndex;
    private DrawModel model;
    public int timeAnim = 0;
    
    private long createTime; //注文が作成された時間
    private Timer expirationTimer; // 自動削除用タイマー

    public Order(String orderName, int orderIndex, DrawModel model){
        //コンストラクタでは完成形の値を設定
        this.orderName = orderName;
        this.hasPlate = true;
        this.createTime = System.currentTimeMillis();
        this.posAnim = 1200;
        this.orderIndex = orderIndex;
        this.model = model;
        //オーダーによって必要な食材や状態(切られてる、焼かれてる等)を設定
        if("salad".equals(orderName)){
            System.out.println("Order created: " + this.orderName);
            this.timeLimit = 100;

            this.ingredient1 = new Cabbage();
            this.ingredient1.foodStatus = 2;
            this.ingredient1.isOnPlate = true;

            this.ingredient2 = new Tomato();
            this.ingredient2.foodStatus = 2;
            this.ingredient2.isOnPlate = true;

            this.ingredient3 = new Cucumber();
            this.ingredient3.foodStatus = 2;
            this.ingredient3.isOnPlate = true;
        }
        if("tekkamaki".equals(orderName)){
            System.out.println("Order created: " + this.orderName);
            this.timeLimit = 100;

            this.ingredient1 = new Rice();
            this.ingredient1.foodStatus = 3;
            this.ingredient1.isOnPlate = true;

            this.ingredient2 = new Tuna();
            this.ingredient2.foodStatus = 2;
            this.ingredient2.isOnPlate = true;

            this.ingredient3 = new Seaweed();
            this.ingredient3.foodStatus = 1;
            this.ingredient3.isOnPlate = true;
        }
        if("kappamaki".equals(orderName)){
            System.out.println("Order created: " + this.orderName);
            this.timeLimit = 100;

            this.ingredient1 = new Rice();
            this.ingredient1.foodStatus = 3;
            this.ingredient1.isOnPlate = true;

            this.ingredient2 = new Cucumber();
            this.ingredient2.foodStatus = 2;
            this.ingredient2.isOnPlate = true;

            this.ingredient3 = new Seaweed();
            this.ingredient3.foodStatus = 1;
            this.ingredient3.isOnPlate = true;
        }
        if("tunanigiri".equals(orderName)){
            System.out.println("Order created: " + this.orderName);
            this.timeLimit = 80;

            this.ingredient1 = new Rice();
            this.ingredient1.foodStatus = 3;
            this.ingredient1.isOnPlate = true;

            this.ingredient2 = new Tuna();
            this.ingredient2.foodStatus = 2;
            this.ingredient2.isOnPlate = true;

        }
        if("ikanigiri".equals(orderName)){
            System.out.println("Order created: " + this.orderName);
            this.timeLimit = 80;

            this.ingredient1 = new Rice();
            this.ingredient1.foodStatus = 3;
            this.ingredient1.isOnPlate = true;

            this.ingredient2 = new Squid();
            this.ingredient2.foodStatus = 2;
            this.ingredient2.isOnPlate = true;

        }
        if("kaisendon".equals(orderName)){
            System.out.println("Order created: " + this.orderName);
            this.timeLimit = 100;

            this.ingredient1 = new Rice();
            this.ingredient1.foodStatus = 3;
            this.ingredient1.isOnPlate = true;

            this.ingredient2 = new Tuna();
            this.ingredient2.foodStatus = 2;
            this.ingredient2.isOnPlate = true;

            this.ingredient3 = new Squid();
            this.ingredient3.foodStatus = 2;
            this.ingredient3.isOnPlate = true;
        }
 

        // 制限時間後に削除するタイマーを設定
        expirationTimer = new Timer(timeLimit * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioManager se = new AudioManager();
                se.playSE("./sound/music_timeuporder3.wav");
                model.scoreDown(null);
                removeThisOrder();
                System.out.println(orderIndex+orderName + " の制限時間が切れました！");
            }
        });
        expirationTimer.setRepeats(false); // 一度だけ実行
        expirationTimer.start();
    }
    private void removeThisOrder(){
        model.removeOrder(orderIndex);
    }

    public boolean isCompleted(Plate plate) { //オーダー判定処理 Kome
        System.out.println("isCompleted() called");
        boolean[] matchedIngredients = new boolean[3];
        Food[] orderIngredients = {ingredient1, ingredient2, ingredient3};
    
        for (int i = 0; i < plate.foods.length; i++) {
            for (int j = 0; j < orderIngredients.length; j++) {
                if(orderIngredients[j] == null){
                    matchedIngredients[j] = true;
                    continue;
                }
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
    public double getRemainingTime(){
        long elapsedTimeMill = (System.currentTimeMillis() - createTime);
        double elapsedTime = elapsedTimeMill / 1000.0;
        return (timeLimit - elapsedTime);
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