import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class DrawView extends JPanel {
    protected DrawModel model;
    Grid[][] grid;
    private Image Image1;
    private Image Image2;
    private Image Image3;
    private Image Image4;
    private Image Image5;
    private Image Image6;
    private Image Image7;
    private Image Image8;
    private Image Image9;
    private Image Image10;
    // private Image Image11;
    private Image ImagePlayer;
    private Image ImagePlayer_up;
    private Image ImagePlayer_left;
    private Image ImagePlayer_down;
    private Image ImagePlayer_right;
    private Image imgCabTom;
    private Image imgErrorBlock;
    private Image imgCounter;
    Player player;
    public DrawView(DrawModel m) {
        model = m;
        this.setFocusable(true);
        player = model.getPlayer();
        grid = model.getGrid();
        //画像読み込み
        Image1=new ImageIcon("kyabetu.png").getImage();
        Image2=new ImageIcon("houtyou.png").getImage();
        Image3=new ImageIcon("kansei.png").getImage();
        Image4=new ImageIcon("kyabetuBox.png").getImage();
        Image5 = new ImageIcon("CutKyabetu.png").getImage();
        Image6 = new ImageIcon("PlateBox.png").getImage();
        Image7 = new ImageIcon("Plate.png").getImage();
        Image8 = new ImageIcon("Tomato.png").getImage();
        Image9 = new ImageIcon("cut_tomato.png").getImage();
        Image10 = new ImageIcon("TomatoBox.png").getImage();
        ImagePlayer_up = new ImageIcon("player_up.png").getImage();
        ImagePlayer_left = new ImageIcon("player_left.png").getImage();
        ImagePlayer_down = new ImageIcon("player_down.png").getImage();
        ImagePlayer_right = new ImageIcon("player_right.png").getImage();
        imgCabTom = new ImageIcon("cabbage_and_tomato.png").getImage();
        imgErrorBlock = new ImageIcon("error_image.png").getImage();
        imgCounter = new ImageIcon("counter.png").getImage();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[] size = model.getFieldSize();
        //int[][] imageGrid = model.getImageGrid();
        int cellSize = model.getCellSize();

        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                if (grid[i][j].wall) {
                    g.setColor(Color.GRAY);
                } else if (grid[i][j].obstacle) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);

                //カウンターの画像を描画 //Yoshida
                if(grid[i][j].isCounter == true){
                    g.drawImage(imgCounter, i * cellSize, j * cellSize, cellSize, cellSize, this);
                }

                if(grid[i][j].isPlatePlaced == true){ //皿は食材の土台にあるべきなので、皿のみの特殊描画処理
                    System.out.println("皿の描画を試みました");
                    g.drawImage(Image7, i * cellSize, j * cellSize, cellSize, cellSize, this);
                }else{
                    
                }

                
                //食材画像を描画
                Image selectedImage = null;
                if(grid[i][j].plate == null && grid[i][j].food != null){ //そのマスはplateをもっていなく かつ そのマスにはしょくざいがある とき
                    //つまり皿の描画はなくFoodだけの描画の場合です。
                    selectedImage = setFoodImage2(grid[i][j].food);
                }else if(grid[i][j].plate != null && grid[i][j].plate.hasAnyFood() == true){ //皿があって食材がおいてある場合
                    selectedImage = setPlateImage(grid[i][j].plate);
                }

                //ツールマスに関しての描画
                if(grid[i][j].tool != 0){
                    selectedImage = setToolImage(grid[i][j].tool);
                }

                //統合前コードです Kome
                /*
                if(grid[i][j].food != null){ //食材の描画です
                    int[] receivedInfo = grid[i][j].food.getInfo(); //そのマスのFood情報を受け取る;
                    selectedImage = setFoodImage(receivedInfo);
                }else if(grid[i][j].tool != 0){ //そのマスが何かしらのツールだった場合にツールの描画
                    selectedImage = setToolImage(grid[i][j].tool);
                }
                */

                /*
                switch (imageGrid[i][j]) {
                    case 1:
                        selectedImage = Image1; break;
                    case 2:
                        selectedImage = Image2; break;
                    case 3:
                        selectedImage = Image3; break;
                    case 4:
                        selectedImage = Image4; break;
                    case 5:
                        selectedImage = Image5; break;
                    case 6:
                        selectedImage = Image6; break;
                    case 7:
                        selectedImage = Image7; break;
                    case 8:
                        selectedImage = Image8; break;
                    case 9:
                        selectedImage = Image9; break;
                    case 10:
                        selectedImage = Image10; break;
                }
                */
                if (selectedImage != null) {
                    g.drawImage(selectedImage, i * cellSize, j * cellSize, cellSize, cellSize, this);
                }
            }
        }
        // プレイヤーを描画
        switch(player.direction){
            case 1: ImagePlayer = ImagePlayer_up; break;
            case 2: ImagePlayer = ImagePlayer_left; break;
            case 3: ImagePlayer = ImagePlayer_down; break;
            case 4: ImagePlayer = ImagePlayer_right; break;
        }
        g.drawImage(ImagePlayer,player.x * cellSize, player.y * cellSize, cellSize, cellSize, this);

        if(player.hasPlate == true){ //プレイヤーが皿を持っていたら
            //皿と画像の比率を調整
            int foodSize = cellSize * 2/3;
            int offsetX = cellSize /6;
            int offsetY = cellSize /6;
            if(player.direction == 1) offsetY -= cellSize / 2;
            else if(player.direction == 2) offsetX -= cellSize / 2;
            else if(player.direction == 3) offsetY += cellSize / 2;
            else if(player.direction == 4) offsetX += cellSize / 2;
            g.drawImage(Image7, player.x * cellSize + offsetX, player.y * cellSize + offsetY , foodSize, foodSize, this);
        }
        Image heldFoodImage = null;
        if(player.hasPlate == true && player.plate.hasAnyFood() == true){ //食材ありの皿を持ってたら
            heldFoodImage = setPlateImage(player.plate);
        }
        else if(player.getFood() != null){ //単体の食材を持っていたら
            heldFoodImage = setFoodImage2(player.getFood());
        }
        if (heldFoodImage != null) {
            // 少し小さめにしてプレイヤーの上に描画
            int foodSize = cellSize / 2;
            int offsetX = cellSize / 4;
            int offsetY = cellSize / 4;
            if(player.direction == 1) offsetY -= cellSize / 2;
            else if(player.direction == 2) offsetX -= cellSize / 2;
            else if(player.direction == 3) offsetY += cellSize / 2;
            else if(player.direction == 4) offsetX += cellSize / 2;
            g.drawImage(heldFoodImage, player.x * cellSize + offsetX, player.y * cellSize + offsetY , foodSize, foodSize, this);
        }

        //この注文描画処理の部分で問題がある？みたいです。画面が真っ白になってしまうので他の動作確認を行う場合はコメントアウトしておいてください。
        //注文を描画
        Image orderImage = null;
        for(int i=0; i<3; i++){
            Order currentOrder = model.getOrder(i);
            if(currentOrder != null){
                orderImage = setOrderImage(currentOrder);
                g.setColor(Color.BLUE);
                g.fillRect(i*3*cellSize, 9 * cellSize, 3*(cellSize-2), 60);
                g.drawImage(orderImage, cellSize + i*3*cellSize, 9*cellSize, cellSize-2, cellSize-2, this);
                for(int j=0; j<3; j++){
                    g.fillRect((i*3*cellSize)+j*cellSize, 10 * (cellSize)+2, (cellSize-6), 50);
                }
                //int limitationTime = currentOrder;
            }
        }

        //統合前コードです。うまく結合できた気がする Kome
        /*
        // プレイヤーが食材を持っている場合、プレイヤーの上に食材の画像を重ねて描画
        if (player.getFood() != null) {
            Image heldFoodImage = null;
            switch (player.getFood().cabbage) { //食材の状況によって画像の分岐をする
                case 1:
                    heldFoodImage = Image1; // 未調理の食材
                    break;
                case 2:
                    heldFoodImage = Image5; // 切った状態の食材
                    break;
            }
            switch (player.getFood().tomato) {
                case 1: heldFoodImage = Image8; break;
                case 2: heldFoodImage = Image9; break;
            }

            if (heldFoodImage != null) {
                // 少し小さめにしてプレイヤーの上に描画
                int foodSize = cellSize / 2;
                int offsetX = cellSize / 4;
                int offsetY = cellSize / 4;
                if(player.direction == 1) offsetY -= cellSize / 2;
                else if(player.direction == 2) offsetX -= cellSize / 2;
                else if(player.direction == 3) offsetY += cellSize / 2;
                else if(player.direction == 4) offsetX += cellSize / 2;
                g.drawImage(heldFoodImage, player.x * cellSize + offsetX, player.y * cellSize + offsetY , foodSize, foodSize, this);
                
            }
        }
        */
    }
    private Image setFoodImage(int[] info){
        if(info[0]==1 && info[1]==0 && info[2] == 0) return Image1; //未加工キャベツ
        else if(info[0]==0 && info[1]==1 && info[2] == 0) return Image8; //未加工トマト
        else if(info[0]==2 && info[1]==0 && info[2] == 0) return Image5; //カットキャベツ
        else if(info[0]==0 && info[1]==2 && info[2] == 0) return Image9; //未加工キャベツ
        else if(info[0]==0 && info[1]==1 && info[2] == 0) return Image8; //未加工キャベツ
        else if(info[0] == 1 && info[1] ==  1){
            return imgCabTom;
        }
        return imgErrorBlock;
    }
    private Image setToolImage(int toolId){
        switch(toolId){
            case 1: return Image2;
            case 2: return Image4;
            case 3: return Image6;
            case 4: return Image10;
        }
        return imgErrorBlock;
    }
    private Image setFoodImage2(Food foodInfo){
        // switch文にしてもいいかもね
        if(foodInfo.foodName == "Kyabetu"){
            if(foodInfo.foodStatus == 1) return Image1;
            else if(foodInfo.foodStatus == 2) return Image5;
            else return imgErrorBlock;
        }else if(foodInfo.foodName == "Tomato"){
            if(foodInfo.foodStatus == 1) return Image8;
            else if(foodInfo.foodStatus == 2) return Image9;
            else return imgErrorBlock;
        }
        return imgErrorBlock;
    }
    private Image setPlateImage(Plate targetPlate){
        Food food[] = new Food[3];
        int kyabetu = 0; //そのプレートにおいてそれぞれの食材がどうなっているか
        int tomato = 0;
        int cucumber = 0;
        for(int i = 0; i < 3; i++){
            food[i] = targetPlate.get(i);
            if(food[i] == null){  break; }//これ以上の食材はないのでbreak
            if(food[i].foodName == "Kyabetu") kyabetu = food[i].foodStatus;
            else if(food[i].foodName == "Tomato") tomato = food[i].foodStatus;
            //else if(food[i].foodName == "cucumber") cucumber = food[i].foodStatus;
        }
        //System.out.printf("(%d, %d, %d)\n", kyabetu, tomato, cucumber);
        if(kyabetu==1 && tomato==0 && cucumber == 0) return Image1; //未加工キャベツ
        else if(kyabetu==0 && tomato==1 && cucumber == 0) return Image8; //未加工トマト
        else if(kyabetu==2 && tomato==0 && cucumber == 0) return Image5; //カットキャベツ
        else if(kyabetu==0 && tomato==2 && cucumber == 0) return Image9; //カットキャベツ
        else if(kyabetu == 2 && tomato == 2 && cucumber == 0) return imgCabTom; //キャベツ & トマト
        return imgErrorBlock;
    }

    private Image setOrderImage(Order order){
        System.out.println(order.orderName +"の画像を取得します。"); //デバッグ用
        if("salad".equals(order.orderName)){
            System.out.println(order.orderName +"の画像を取得しました。"); //デバッグ用
            return imgCabTom;
        } 
        else return null;
    }
}