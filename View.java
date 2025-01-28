import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.util.concurrent.*;

class DrawView extends JPanel {

    //int orderXAnim = 2000;
    int speed = 20;
    double easingFactor = 0.2;

    private BufferedImage backgroundImage = null;

    private Timer drawTimer60fps; //60Hzでpaintcomponent()を呼び出すために使う Kome
    protected DrawModel model;
    private DrawController cont;
    Grid[][] grid;
    private Image ImagePlayer;
    private Image imgPlayerUp;
    private Image imgPlayerLeft;
    private Image imgPlayerDown;
    private Image imgPlayerRight;
    private Image imgErrorBlock;
    private Image imgKnife;
    private Image imgPlateBox;
    private Image imgPlate;
    private Image imgCabbageBox;
    private Image imgCabbage;
    private Image imgCabbageCut;
    private Image imgTomatoBox;
    private Image imgTomato;
    private Image imgTomatoCut;
    private Image imgCucumberBox;
    private Image imgCucumber;
    private Image imgCucumberCut;
    private Image imgCabTom;
    private Image imgCounter;
    private Image orderPaper;
    private Image imgKnifeBlack;
    Player player;
    int headerBlank = 150;
    int fotterBlank = 300;
    double playerSpeed;


    private ScheduledExecutorService executor;


    //public boolean moving = true;
    private String text = "sample_text";
    private Font textFont = new Font("Arial", Font.BOLD, 24);
    private Color textColor = Color.RED;
    public DrawView(DrawModel m) {
        model = m;
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        player = model.getPlayer();
        grid = model.getGrid();
        generateBackground();
        //60fpsでの描画を開始
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(this::repaint); // Swingスレッドで描画
        }, 0, 16, TimeUnit.MILLISECONDS);

        /*//timerを使うと60fpsからずれるらしいから変えた　Kome
        drawTimer60fps = new Timer(1, e -> {
            long currentTime = System.nanoTime();
            double elapsedTime = (currentTime - lastTime) / 1_000_000.0; // ミリ秒に変換
            frameCount++;
        
            if (frameCount % 60 == 0) { // 1秒ごとにFPS表示
                System.out.println("FPS: " + (1000.0 / elapsedTime) * frameCount);
                frameCount = 0;
                lastTime = currentTime;
            }
        
            repaint(); // コメントアウトして FPS の変化を見る
        });
        drawTimer60fps.start();
        */
        playerSpeed = player.getPlayerSpeed();
        //画像読み込み
        imgPlayerUp = new ImageIcon("img/player_up.png").getImage();
        imgPlayerLeft = new ImageIcon("img/player_left.png").getImage();
        imgPlayerDown = new ImageIcon("img/player_down.png").getImage();
        imgPlayerRight = new ImageIcon("img/player_right.png").getImage();
        imgErrorBlock = new ImageIcon("img/error_image.png").getImage();

        imgKnife=new ImageIcon("img/knife.png").getImage();
        imgPlateBox = new ImageIcon("img/plate_box.png").getImage();
        imgPlate = new ImageIcon("img/plate.png").getImage();

        imgCabbageBox=new ImageIcon("img/cabbage_box.png").getImage();
        imgCabbage=new ImageIcon("img/cabbage.png").getImage();
        imgCabbageCut = new ImageIcon("img/cabbage_cut.png").getImage();

        imgTomatoBox = new ImageIcon("img/tomato_box.png").getImage();
        imgTomato = new ImageIcon("img/tomato.png").getImage();
        imgTomatoCut = new ImageIcon("img/tomato_cut.png").getImage();

        imgCucumberBox = new ImageIcon("img/cucumber_box.png").getImage();
        imgCucumber = new ImageIcon("img/cucumber.png").getImage();
        imgCucumberCut = new ImageIcon("img/cucumber_box.png").getImage();

        imgCabTom = new ImageIcon("img/cabbage_and_tomato.png").getImage();

        imgCounter = new ImageIcon("img/counter.png").getImage();
        orderPaper = new ImageIcon("img/order_paper_test.png").getImage();
        imgKnifeBlack = new ImageIcon("img/knife_black.png").getImage();
    }
    public void setController(DrawController cont) { this.cont = cont; }

    private void generateBackground() { //画像を毎回描画すると処理が重いから、代用
        int[] size = model.getFieldSize();
        int cellSize = model.getCellSize();
        int width = size[0] * cellSize;
        int height = size[1] * cellSize;
        backgroundImage = new BufferedImage(960, 900, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = backgroundImage.createGraphics();
        g2d.setColor(Color.lightGray);
        g2d.fillRect(0, 0, 960, 900);
    
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                if (grid[i][j].wall) {
                    g2d.setColor(Color.GRAY);
                } else if (grid[i][j].obstacle) {
                    g2d.setColor(Color.RED);
                } else {
                    g2d.setColor(Color.DARK_GRAY);
                }
                g2d.fillRect(i * cellSize, j * cellSize + headerBlank, cellSize, cellSize);
            }
        }
    
        g2d.dispose(); // リソース解放
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[] size = model.getFieldSize();
        int cellSize = model.getCellSize();
        g.drawImage(backgroundImage, 0, 0, this);
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                //generateBackGround()によってまとめました
                /*
                if (grid[i][j].wall) {
                    g.setColor(Color.GRAY);
                } else if (grid[i][j].obstacle) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.DARK_GRAY);
                }
                g.fillRect(i * cellSize, j * cellSize  + headerBlank, cellSize, cellSize);
                */
                //カウンターの画像を描画 //Yoshida
                if(grid[i][j].isCounter == true){
                    g.drawImage(imgCounter, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);
                }

                if(grid[i][j].isPlatePlaced == true){ //皿は食材の土台にあるべきなので、皿のみの特殊描画処理
                    g.drawImage(imgPlate, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);
                }else{
                    
                }

                //食材画像を描画
                Image selectedImage = null;
                if(grid[i][j].plate == null && grid[i][j].food != null){ //そのマスはplateをもっていなく かつ そのマスにはしょくざいがある とき
                    //つまり皿の描画はなくFoodだけの描画の場合です。
                    selectedImage = setFoodImage(grid[i][j].food);
                }else if(grid[i][j].plate != null && grid[i][j].plate.hasAnyFood() == true){ //皿があって食材がおいてある場合
                    selectedImage = setPlateImage(grid[i][j].plate);
                }

                //ツールマスに関しての描画
                if(grid[i][j].tool != 0){
                    selectedImage = setToolImage(grid[i][j].tool);
                }

                if (selectedImage != null) {
                    g.drawImage(selectedImage, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);
                }

                if(grid[i][j].isPlatePlaced && grid[i][j].plate.hasAnyFood()){
                    setIngredientsImage(cellSize, grid[i][j].x, grid[i][j].y, 0, 0, grid[i][j].plate, g);
                }
                
            }
        }
        
        // プレイヤーを描画
        switch(player.direction){
            case 1: ImagePlayer = imgPlayerUp; break;
            case 2: ImagePlayer = imgPlayerLeft; break;
            case 3: ImagePlayer = imgPlayerDown; break;
            case 4: ImagePlayer = imgPlayerRight; break;
        }
        //アニメーション処理
        if(Math.abs(player.x - player.xAnim) <= playerSpeed){
            player.xAnim = player.x;
            player.moving = false;
        }
        else if(player.x > player.xAnim){
            player.xAnim += playerSpeed;
            player.moving = true;
        } 
        else if(player.x < player.xAnim){
            player.xAnim -= playerSpeed;
            player.moving = true;
        }

        if(Math.abs(player.y - player.yAnim) <= playerSpeed){
            player.yAnim = player.y;
            player.moving = (player.moving || false);
        }
        else if(player.y > player.yAnim){
            player.yAnim += playerSpeed;
            player.moving = true;
        }
        else if(player.y < player.yAnim){
            player.yAnim -= playerSpeed;
            player.moving = true;
        }
        g.drawImage(ImagePlayer,(int)(player.xAnim*cellSize), (int)(player.yAnim*cellSize) + headerBlank, cellSize, cellSize, this);

        if(player.hasPlate == true){ //プレイヤーが皿を持っていたら
            //皿と画像の比率を調整
            int foodSize = cellSize * 2/3;
            int offsetX = cellSize /6;
            int offsetY = cellSize /6;
            if(player.direction == 1) offsetY -= cellSize / 2;
            else if(player.direction == 2) offsetX -= cellSize / 2;
            else if(player.direction == 3) offsetY += cellSize / 2;
            else if(player.direction == 4) offsetX += cellSize / 2;
            g.drawImage(imgPlate, player.x * cellSize + offsetX, player.y * cellSize + offsetY  + headerBlank, foodSize, foodSize, this);
        }
        Image heldFoodImage = null;
        if(player.hasPlate == true && player.plate.hasAnyFood() == true){ //食材ありの皿を持ってたら
            heldFoodImage = setPlateImage(player.plate);
        }
        else if(player.getFood() != null){ //単体の食材を持っていたら
            heldFoodImage = setFoodImage(player.getFood());
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
            g.drawImage(heldFoodImage, player.x * cellSize + offsetX, player.y * cellSize + offsetY + headerBlank, foodSize, foodSize, this);
        }
        if(player.hasPlate == true && player.plate.hasAnyFood()){
            int offsetX = cellSize / 4;
            int offsetY = cellSize / 4;
            if(player.direction == 1) {offsetX = 0; offsetY -= cellSize *2/ 3;}
            else if(player.direction == 2) {offsetX -= cellSize *2/ 3; offsetY = 0;}
            else if(player.direction == 3) {offsetX = 0; offsetY += cellSize ;}
            else if(player.direction == 4) {offsetX += cellSize / 3; offsetY = 0;}
            setIngredientsImage(cellSize, player.x, player.y, offsetX, offsetY, player.plate, g);
        }

        //UIの描画

        if (true) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setFont(textFont);
            g2d.setColor(textColor);
            g2d.drawString("score : "+Integer.toString(model.score), 850, 750);
        }

        //オーダー用紙の描画
        for(int i = 0; i < model.orders.length; i++){
            Image orderImage;
            if(model.orders[i] != null){
                Order order = model.orders[i];
                orderImage = setOrderImage(order);
                int targetPos = 20 + (i * 3 * cellSize);
                double dx = targetPos - order.posAnim;
                order.posAnim += dx * easingFactor;
                
                if (Math.abs(dx) < 1.0) {
                    order.posAnim = targetPos;
                    if(order.timeAnim == 0){
                        order.timeAnim = 1;
                    }
                    
                }
                if(1 <= order.timeAnim) {
                    if(30 <= order.timeAnim){
                        dx = order.subOrderPosY - order.subOrderPosYAnim;
                        order.subOrderPosYAnim += easingFactor * dx;
                        if(Math.abs(dx) < 1.0){ 
                            order.subOrderPosYAnim = order.subOrderPosY;
                        }
                        int sOPYA = (int)order.subOrderPosYAnim; //文字が長いんでint型にキャストして入れ直し
                        if(order.ingredient1 != null){
                            g.setColor(new Color(174, 207, 227));
                            g.fillRect((int)order.posAnim+8+(cellSize-6)*0, sOPYA, 50, 90);
                            g.drawImage(setCorrectRaw(order.ingredient1), (int)order.posAnim+(cellSize-6)*0 + 10, sOPYA+10, 42,42,this);
                            if(setCorrectMethod(order.ingredient1)!=null){
                                g.drawImage(setCorrectMethod(order.ingredient1), (int)order.posAnim+(cellSize-6)*0 + 10, sOPYA+50, 42,42,this);
                            }
                        }
                        if(order.ingredient2 != null){
                            g.setColor(new Color(174, 207, 227));
                            g.fillRect((int)order.posAnim+8+(cellSize-6)*1, sOPYA, 50, 90);
                            g.drawImage(setCorrectRaw(order.ingredient2), (int)order.posAnim+(cellSize-6)*1 + 10, sOPYA+10, 42,42,this);
                            if(setCorrectMethod(order.ingredient2)!=null){
                                g.drawImage(setCorrectMethod(order.ingredient2), (int)order.posAnim+(cellSize-6)*1 + 10, sOPYA+50, 42,42,this);
                            }
                        }
                        if(order.ingredient3 != null){
                            g.setColor(new Color(174, 207, 227));
                            g.fillRect((int)order.posAnim+8+(cellSize-6)*2, sOPYA, 50, 90);
                            g.drawImage(setCorrectRaw(order.ingredient3), (int)order.posAnim+(cellSize-6)*2 + 10, sOPYA+10, 42,42,this);
                            if(setCorrectMethod(order.ingredient3)!=null){
                                g.drawImage(setCorrectMethod(order.ingredient3), (int)order.posAnim+(cellSize-6)*2 + 10, sOPYA+50, 42,42,this);
                            }
                        }
                    }else{

                    }
                    order.timeAnim++;
                }
                
                //g.fillRect((int)order.posAnim, 0 * cellSize +20, 3*(cellSize-2), 60);
                g.drawImage(orderPaper, (int)order.posAnim, 0 * cellSize +20, 3*(cellSize-2), 90, this);
                drawGauge(g, "down", (int)(order.posAnim)+10, 26, 155, 13, order.getRemainingTime()/order.timeLimit);
                g.drawImage(orderImage, cellSize + (int)order.posAnim, 0*cellSize +20, cellSize-2, cellSize-2, this);
                
            }
        }
        if(cont.spacePushing == true){ player.actionCharge += 1; }
        else{ player.actionCharge = 0; }
        if(0 < player.actionCharge && player.actionCharge < 60){
            drawGauge(g, "up", (int)(player.xAnim*cellSize) + 10, (int)(player.yAnim*cellSize)+headerBlank,(int)(0.7*cellSize),8,player.actionCharge/60.0);
        }else if(player.actionCharge == 60) player.action();
    }
    private void drawGauge(Graphics g, String type, int x, int y, int width, int height, double ratio){
        if(ratio > 1) { System.out.println("Warning : ゲージの割合が100%を超えています"); }
        //System.out.printf("ratio = %.1f%n", ratio); //デバッグ用
        
        if(type == "up"){
            g.setColor(Color.WHITE);
            g.fillRect(x-2, y-2, width+4, height+4);
            g.setColor(Color.GREEN);
            g.fillRect(x, y, (int)(width*ratio), height);
        }
        else if(type == "down"){
            g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
            if(ratio >= 0.5) { g.setColor(new Color(75, 140, 35)); }
            else if(ratio >= 0.25) { g.setColor(Color.YELLOW); }
            else{ g.setColor(Color.RED); }
            g.fillRect(x, y, (int)(width*ratio), height);
        }
    }

    private Image setToolImage(int toolId){
        switch(toolId){
            case 1: return imgKnife;
            case 2: return imgCabbageBox;
            case 3: return imgPlateBox;
            case 4: return imgTomatoBox;
            case 5: return imgCucumberBox;
        }
        return imgErrorBlock;
    }
    private Image setCorrectRaw(Food foodInfo){
        if(foodInfo.foodName == "cabbage") return imgCabbage;
        else if(foodInfo.foodName == "tomato") return imgTomato;
        else if(foodInfo.foodName == "cucumber") return imgCucumber;
        else return imgErrorBlock;
    }
    private Image setCorrectMethod(Food foodInfo){
        if(foodInfo.foodStatus == 2) return imgKnifeBlack;
        else return null;
    }
    private Image setFoodImage(Food foodInfo){
        // switch文にしてもいいかもね
        if(foodInfo.foodName == "cabbage"){
            if(foodInfo.foodStatus == 1) return imgCabbage;
            else if(foodInfo.foodStatus == 2) return imgCabbageCut;
            else return imgErrorBlock;
        }else if(foodInfo.foodName == "tomato"){
            if(foodInfo.foodStatus == 1) return imgTomato;
            else if(foodInfo.foodStatus == 2) return imgTomatoCut;
            else return imgErrorBlock;
        }else if(foodInfo.foodName == "cucumber"){
            if(foodInfo.foodStatus == 1) return imgCucumber;
            else if(foodInfo.foodStatus == 2) return imgCucumberCut;
            else return imgErrorBlock;
        }
        return imgErrorBlock;
    }
    private Image setPlateImage(Plate targetPlate){
        Food food[] = new Food[3];
        int cabbage = 0; //そのプレートにおいてそれぞれの食材がどうなっているか
        int tomato = 0; //0:存在しない 1:生 2:カット
        int cucumber = 0;
        //plateに乗っている具材情報を取得
        for(int i = 0; i < 3; i++){
            food[i] = targetPlate.get(i);
            if(food[i] == null){  break; }//これ以上の食材はないのでbreak
            if(food[i].foodName == "cabbage") cabbage = food[i].foodStatus;
            else if(food[i].foodName == "tomato") tomato = food[i].foodStatus;
            else if(food[i].foodName == "cucumber") cucumber = food[i].foodStatus;
        }
        //取得した具材情報を利用してImageにセットする画像を返す。
        if(cabbage==1 && tomato==0 && cucumber == 0) return imgCabbage; //未加工キャベツ
        else if(cabbage==0 && tomato==1 && cucumber == 0) return imgTomato; //未加工トマト
        else if(cabbage==0 && tomato==0 && cucumber == 1) return imgCucumber; //未加工きゅうり
        else if(cabbage==2 && tomato==0 && cucumber == 0) return imgCabbageCut; //カットキャベツ
        else if(cabbage==0 && tomato==2 && cucumber == 0) return imgTomatoCut; //カットトマト
        else if(cabbage==0 && tomato==0 && cucumber == 2) return imgCucumberCut; //カットキュウリ
        else if(cabbage == 1 && tomato ==  1 && cucumber == 0) return imgCabTom;
        return imgErrorBlock;
    }

    private Image setOrderImage(Order order){
        //System.out.println(order.orderName +"の画像を取得します。"); //デバッグ用
        if("salad".equals(order.orderName)){
            //System.out.println(order.orderName +"の画像を取得しました。"); //デバッグ用
            return imgCabTom;
        } 
        else return null;
    }

    // Imageを返すわけではなく、この関数を呼び出せば画像を貼れる Yoshida
    // paintComponentに書いても良かったけど煩雑になりそうだったので関数化しました。引数が多くてすいません。
    private void setIngredientsImage(int cellSize, int x, int y, int offsetX, int offsetY, Plate plate, Graphics g){
        Image ingredients[] = new Image[3];
        Food ing[] = new Food[3];
        int size = cellSize/4;
        int offset = 20;
        for(int i=0; i<3; i++){
            if(plate.foods[i] != null){
                ing[i] = plate.foods[i];
                //ing[i].foodStatus = 1; //生の状態を表示したい(調理した食材を皿に置いて、1歩あると画像が生になってしまうのでコメントアウトしてます。)
            }
        }

        for(int i=0; i<3; i++){
            if(ing[i] != null){
                ingredients[i] = setFoodImage(ing[i]); 
                g.drawImage(ingredients[i], x*cellSize+offset*i+offsetX, y*cellSize+headerBlank+offsetY, size, size, this);
            }
        }
    }
}