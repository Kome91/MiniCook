import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;

abstract class Food { //継承させる前提のabstractクラス
    public int foodStatus; //食材のステータスの変数、何もしてなければ0になる カットしてたら1
    public boolean canCut; //その食材がカット可能ならtrue
    public boolean canHeat; //その食材が加熱可能ならtrue
    public boolean isOnPlate; //皿の上に置かれているか
    public String foodName;
    public abstract int getFoodStatus();

    public Food(int foodStatus, boolean canCut, boolean canHeat, boolean isOnPlate, String foodName){
        this.foodStatus = foodStatus;
        this.canCut = canCut;
        this.canHeat = canHeat;
        this.isOnPlate = isOnPlate;
        this.foodName = foodName;
    }
}

//Foodクラスを継承したCabbageクラスです
class Cabbage extends Food{
    public Cabbage(){
        super(1, true, false, false, "cabbage");
    }

    public int getFoodStatus(){ //そのフードの状態を返す
        return foodStatus;
    }
}
//Foodクラスを継承したTomatoクラスです
class Tomato extends Food{
    public Tomato(){
        super(1, true, false, false, "tomato");
    }
    public int getFoodStatus(){ //そのフードの状態を返す
        return foodStatus;
    }
}
//Foodクラスを継承したcucumberクラスです
class Cucumber extends Food{
    public Cucumber(){
        super(1, true, false, false, "cucumber");
    }
    public int getFoodStatus(){ //そのフードの状態を返す
        return foodStatus;
    }
}
//Foodクラスを継承したriceクラスです
class Rice extends Food{
    public Rice(){
        super(1, false, true, false, "rice");
    }
    public int getFoodStatus(){ //そのフードの状態を返す
        return foodStatus;
    }
}
//Foodクラスを継承したtunaクラスです
class Tuna extends Food{
    public Tuna(){
        super(1, true, false, false, "tuna");
    }
    public int getFoodStatus(){ //そのフードの状態を返す
        return foodStatus;
    }
}
//Foodクラスを継承したsquidクラスです
class Squid extends Food{
    public Squid(){
        super(1, true, false, false, "squid");
    }
    public int getFoodStatus(){ //そのフードの状態を返す
        return foodStatus;
    }
}
//Foodクラスを継承したseaweedクラスです
class Seaweed extends Food{
    public Seaweed(){
        super(1, false, false, true, "seaweed");
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
                //System.out.println(food.foodName + " を皿に追加しました。");
                return;  // 追加が完了したら終了
            }
        }
        System.out.println("これ以上皿に食材を追加できません。");
    }

    public Food get(int i){
        if(i<0 || i>=foods.length){return null;}
        else return foods[i];
    }

    public void printPlate(){
        String state = "";
        //System.out.print("現在、皿の上には：");
        for(int i=0; i<3; i++){
            if(foods[i] != null) {
                switch(foods[i].foodStatus){
                    case 1: state = "raw"; break;
                    case 2: state = "cut"; break;
                    case 3: state = "grilled"; break;
                }
                //System.out.print(foods[i].foodName+"("+ state + ")" + " ");
            }
        }
        //System.out.print("\n");
        return ;
    }

    public boolean matchesOrder(Order order) {
        boolean[] matchedIngredients = new boolean[3];
        Food[] orderIngredients = {order.ingredient1, order.ingredient2, order.ingredient3};

            // 皿にある食材の数をカウント
        int plateFoodCount = 0;
        for (int i=0; i<3; i++) {
            if (foods[i] != null) {
                plateFoodCount++;
            }
        }

        // オーダーの食材リストを作成
        int orderFoodCount = 0;
        for (int i=0; i<3; i++) {
            if (orderIngredients[i] != null) {
                orderFoodCount++;
            }
        }

        // **オーダーの食材数と皿の食材数が違ったら不一致とする**
        if (plateFoodCount != orderFoodCount) {
            //System.out.println("料理の食材数がオーダーと一致しません。");
            return false;
        }

        for (int i = 0; i < foods.length; i++) {
            for (int j = 0; j < orderIngredients.length; j++) {
                if(orderIngredients[j] == null){
                    matchedIngredients[j] = true;
                    continue;
                }
                if (!matchedIngredients[j] && foods[i] != null) {
                    if (foods[i].getClass() == orderIngredients[j].getClass() &&
                        foods[i].foodStatus == orderIngredients[j].foodStatus) {
                        //System.out.println(foods[i].foodName + "は満たされました。");
                        matchedIngredients[j] = true;
                        break;
                    }
                }
            }
        }

        for(int i=0; i<matchedIngredients.length; i++){
            if(matchedIngredients[i]){
                //System.out.println("材料"+(i+1)+"は満たされいます。");
            }
            //else System.out.println("材料"+(i+1)+"は満たされいません。");
        }

        for (boolean matched : matchedIngredients) {
            if (!matched){
                //System.out.println("料理は未完成です。");
                return false;
            }
        }
        //System.out.println("料理は完成しています。");
        return true;
    }

}