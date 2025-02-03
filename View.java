import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


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
    private Image imgBoil;
    private Image imgBoilRice;
    private Image imgPlateBox;
    private Image imgPlate;
    private Image imgPan;
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
    private Image imgCabCuc;
    private Image imgTomCuc;
    private Image imgCabTomCuc;
    private Image imgRiceBox;
    private Image imgRice;
    private Image imgRiceBoil;
    private Image imgTunaBox;
    private Image imgTuna;
    private Image imgTunaCut;
    private Image imgSquidBox;
    private Image imgSquid;
    private Image imgSquidCut;
    private Image imgSeaweedBox;
    private Image imgSeaweed;
    private Image imgRicTun;
    private Image imgRicSqu;
    private Image imgRicSea;
    private Image imgRicCuc;
    private Image imgTunSqu;
    private Image imgRicCucSea;
    private Image imgRicTunSea;
    private Image imgRicTunSqu;




    private Image imgCounter;
    private Image orderPaper;
    private Image imgKnifeBlack;
    private Image imgBoilBlack;
    private Image imgFloor1;
    private Image imgFloor2;
    private Image imgFloor3;
    private Image imgTable;
    private Image imgSampleSalad;

    private Image imgA;
    private Image imgB;
    private Image imgC;
    private Image imgF1;
    private Image imgF2;
    

    private Image imgFire;

    private Image imgUIBG;

    Player player;
    int headerBlank = 220;
    int fotterBlank = 300;
    double playerSpeed;

    Waiter[] waiters = new Waiter[5];

    private ScheduledExecutorService executor;
    private int frameCount = 0; // フレーム数をカウント
    private double fps = 0.0; // 計算したFPSを格納
    private long lastTime = System.nanoTime(); // 前回の時間
    private static final long FPS_UPDATE_INTERVAL = 100_000_000; // 100ms（ナノ秒）



    //public boolean moving = true;
    private String text = "sample_text";
    private Font customFont;
    private Color textColor = Color.RED;
    public DrawView(DrawModel m) {
        model = m;
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        player = model.getPlayer();
        grid = model.getGrid();
        loadCustomFont();
        generateBackground();
        //60fpsでの描画を開始
        
        executor = Executors.newScheduledThreadPool(1);
        
        /*
        executor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(this::repaint); // Swingスレッドで描画
        }, 0, 50, TimeUnit.MILLISECONDS);
        */
        
        
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            long currentTime = System.nanoTime();
            frameCount++;

            // 100ms ごとに FPS を計算
            if (frameCount >= 30) {
                double timeDiff = (currentTime - lastTime) / 1_000_000.0;
                double fps = 1000.0 * 30 / timeDiff;
                frameCount = 0; // フレーム数をリセット
                lastTime = currentTime; // 時間を更新
                //System.out.println("FPS: " + fps); // デバッグ出力
            }

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
        //imgErrorBlock = new ImageIcon("img/error_image.png").getImage();
        imgErrorBlock = new ImageIcon("img/miss.png").getImage();

        //皿とツール
        imgKnife=new ImageIcon("img/knife.png").getImage();
        imgBoil=new ImageIcon("img/boil.png").getImage();
        imgBoilRice=new ImageIcon("img/rice_boil.png").getImage();
        imgPlateBox = new ImageIcon("img/plate_box2.png").getImage();
        imgPlate = new ImageIcon("img/plate.png").getImage();
        imgPan = new ImageIcon("img/pan.png").getImage();

        imgCabbageBox=new ImageIcon("img/cabbage_box.png").getImage();
        imgCabbage=new ImageIcon("img/cabbage.png").getImage();
        imgCabbageCut = new ImageIcon("img/cabbage_cut.png").getImage();

        imgTomatoBox = new ImageIcon("img/tomato_box.png").getImage();
        imgTomato = new ImageIcon("img/tomato.png").getImage();
        imgTomatoCut = new ImageIcon("img/tomato_cut.png").getImage();

        imgCucumberBox = new ImageIcon("img/cucumber_box.png").getImage();
        imgCucumber = new ImageIcon("img/cucumber.png").getImage();
        imgCucumberCut = new ImageIcon("img/cucumber_cut.png").getImage();

        imgCabTom = new ImageIcon("img/cab_tom.png").getImage();
        imgCabCuc = new ImageIcon("img/cab_cuc.png").getImage();
        imgTomCuc = new ImageIcon("img/tom_cuc.png").getImage();
        imgCabTomCuc = new ImageIcon("img/cab_tom_cuc.png").getImage();

        imgRiceBox = new ImageIcon("img/rice_box.png").getImage();
        imgRice = new ImageIcon("img/rice.png").getImage();
        imgRiceBoil = new ImageIcon("img/rice_boil2.png").getImage();

        imgTunaBox = new ImageIcon("img/tuna_box.png").getImage();
        imgTuna = new ImageIcon("img/tuna.png").getImage();
        imgTunaCut = new ImageIcon("img/tuna_cut.png").getImage();

        imgSquidBox = new ImageIcon("img/squid_box.png").getImage();
        imgSquid = new ImageIcon("img/squid.png").getImage();
        imgSquidCut = new ImageIcon("img/squid_cut.png").getImage();

        imgSeaweedBox = new ImageIcon("img/seaweed_box.png").getImage();
        imgSeaweed = new ImageIcon("img/seaweed.png").getImage();

        imgRicTun = new ImageIcon("img/ric_tun.png").getImage();
        imgRicSqu = new ImageIcon("img/ric_squ.png").getImage();
        imgRicSea = new ImageIcon("img/ric_sea.png").getImage();
        imgRicCuc = new ImageIcon("img/ric_cuc.png").getImage();
        imgTunSqu = new ImageIcon("img/tun_squ.png").getImage();
        imgRicCucSea = new ImageIcon("img/ric_cuc_sea.png").getImage();
        imgRicTunSea = new ImageIcon("img/ric_tun_sea.png").getImage();
        imgRicTunSqu = new ImageIcon("img/ric_tun_squ.png").getImage();



        imgCounter = new ImageIcon("img/counter.png").getImage();
        orderPaper = new ImageIcon("img/order_paper_short.png").getImage();
        imgKnifeBlack = new ImageIcon("img/knife_black.png").getImage();
        imgBoilBlack = new ImageIcon("img/boil_black.png").getImage();



        imgFloor1 = new ImageIcon("img/floor1.jpg").getImage();
        imgFloor2 = new ImageIcon("img/floor2.jpg").getImage();
        imgFloor3 = new ImageIcon("img/floor3.png").getImage();
        imgA = new ImageIcon("img/test/B.png").getImage();
        imgB = new ImageIcon("img/test/D.png").getImage();
        imgC = new ImageIcon("img/test/C.jpg").getImage();
        imgF1 = new ImageIcon("img/test/F1.png").getImage();
        imgF2 = new ImageIcon("img/test/F2.png").getImage();

        imgTable = new ImageIcon("img/table.png").getImage();
        
        imgSampleSalad = new ImageIcon("img/cab_tom_cuc.png").getImage();

        imgFire = new ImageIcon("img/fires.png").getImage();

        imgUIBG = new ImageIcon("img/ui_background.png").getImage();


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
                    g2d.fillRect(i * cellSize, j * cellSize + headerBlank, cellSize, cellSize);
                } else if (grid[i][j].obstacle) {
                    g2d.setColor(Color.RED);
                    g2d.fillRect(i * cellSize, j * cellSize + headerBlank, cellSize, cellSize);
                } else {
                    g2d.setColor(Color.DARK_GRAY);
                    g2d.drawImage(imgCabbage, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);
                    //g2d.fillRect(i * cellSize, j * cellSize + headerBlank, cellSize, cellSize);
                }
                
            }
        }
    
        g2d.dispose(); // リソース解放
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[] size = model.getFieldSize();
        int cellSize = model.getCellSize();
        //g.drawImage(backgroundImage, 0, 0, this);
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, 960, 900);
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                //generateBackGround()によってまとめました
                
                if (grid[i][j].wall) {
                    if((i == 0 || i == size[0]-1) && j != size[1]-1){
                        g.drawImage(imgA, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);
                    }else{
                        g.drawImage(imgB, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);
                    }

                    //g.drawImage(imgTable, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);
                } else if (grid[i][j].obstacle) {
                    g.setColor(Color.RED);
                    g.fillRect(i * cellSize, j * cellSize + headerBlank, cellSize, cellSize);
                } else {
                    g.setColor(Color.DARK_GRAY);
                    if((i + j)%2 == 0){g.drawImage(imgF1, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);}
                    else {g.drawImage(imgF2, i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);}
                    //g2d.fillRect(i * cellSize, j * cellSize + headerBlank, cellSize, cellSize);
                }
                
            
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
                    setIngredientsImage(cellSize, grid[i][j].x, grid[i][j].y, 0, 0, grid[i][j].plate, g, 0);
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
            setIngredientsImage(cellSize, player.x, player.y, offsetX, offsetY, player.plate, g, player.direction);
        }

        //UIの描画
        g.drawImage(imgUIBG, 60, 750, 250, 90, this);
        g.drawImage(imgUIBG, 660, 750, 250, 90, this);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(customFont);
        g2d.setColor(Color.WHITE);
        int leftTimeAllSec = model.getGameTime();
        int leftTimeMin = leftTimeAllSec/60;
        int leftTimeSec = leftTimeAllSec%60;
        g2d.drawString(Integer.toString(model.score), 170, 820);
        g2d.drawString(Integer.toString(leftTimeMin)+":"+Integer.toString(leftTimeSec), 730, 820);

        //オーダー用紙の描画
        for(int i = 0; i < model.orders.length; i++){
            Image orderImage;
            int orderW = 160;
            int orderH = 100;
            if(model.orders[i] != null){
                Order order = model.orders[i];
                orderImage = setOrderImage(order);
                int targetPos = 20 + i * (orderW +5);
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
                        int interval = cellSize-11;
                        int wid = 45;
                        if(order.ingredient1 != null){
                            g.setColor(new Color(174, 207, 227));
                            g.fillRect((int)order.posAnim+7+interval*0, sOPYA, wid, 90);
                            g.drawImage(setCorrectRaw(order.ingredient1), (int)order.posAnim+interval*0 + 8, sOPYA+10, 42,42,this);
                            if(setCorrectMethod(order.ingredient1)!=null){
                                g.drawImage(setCorrectMethod(order.ingredient1), (int)order.posAnim+interval*0 + 10, sOPYA+50, 42,42,this);
                            }
                        }
                        if(order.ingredient2 != null){
                            g.setColor(new Color(174, 207, 227));
                            g.fillRect((int)order.posAnim+7+interval*1, sOPYA, wid, 90);
                            g.drawImage(setCorrectRaw(order.ingredient2), (int)order.posAnim+interval*1 + 8, sOPYA+10, 42,42,this);
                            if(setCorrectMethod(order.ingredient2)!=null){
                                g.drawImage(setCorrectMethod(order.ingredient2), (int)order.posAnim+interval*1 + 10, sOPYA+50, 42,42,this);
                            }
                        }
                        if(order.ingredient3 != null){
                            g.setColor(new Color(174, 207, 227));
                            g.fillRect((int)order.posAnim+7+interval*2, sOPYA, wid, 90);
                            g.drawImage(setCorrectRaw(order.ingredient3), (int)order.posAnim+interval*2 + 8, sOPYA+10, 42,42,this);
                            if(setCorrectMethod(order.ingredient3)!=null){
                                g.drawImage(setCorrectMethod(order.ingredient3), (int)order.posAnim+interval*2 + 10, sOPYA+50, 42,42,this);
                            }
                        }
                    }
                    order.timeAnim++;
                }
                
                //g.fillRect((int)order.posAnim, 0 * cellSize +20, 3*(cellSize-2), 60);
                g.drawImage(orderPaper, (int)order.posAnim, 15, orderW, orderH, this);
                drawGauge(g, "down", (int)(order.posAnim)+8, 22, orderW-16, 17, order.getRemainingTime()/order.timeLimit);
                //g.drawImage(orderImage, 53 + (int)order.posAnim, 70, cellSize+5, cellSize+5, this);
                g.drawImage(imgSampleSalad, 42 + (int)order.posAnim, 30, 75, 75, this);//プレビューのためです Kome
                
            }
        }
        
        if(cont.spacePushing == true){
            if(player.getFrontGrid().tool == 12){player.actionCharge += 0.5;} //フライパンの時は長め
            else player.actionCharge += 1;
        }
        else{ player.actionCharge = 0; }
        if(0 < player.actionCharge && player.actionCharge < 60){
            drawGauge(g, "up", (int)(player.xAnim*cellSize) + 10, (int)(player.yAnim*cellSize)+headerBlank,(int)(0.7*cellSize),8,player.actionCharge/60.0);
        }else if(player.actionCharge == 60) player.action();

        
        // しょぼいんですけど、フライパンの火の描画です Yoshida
        if(player.food != null && player.food.canHeat){
            if(player.getFrontGrid().tool == 12 && cont.spacePushing == true){
                if(player.actionCharge>0 && player.actionCharge<60){
                    float fireScall = player.actionCharge % 30;
                    //1行目は大きめ、2行目は小さめ
                    //g.drawImage(imgFire, player.getFrontGrid().x * cellSize +30-(int)(fireScall), player.getFrontGrid().y * cellSize + headerBlank+55-(int)(fireScall), (int)(fireScall*cellSize/30), (int)(fireScall*cellSize/30), this);
                    g.drawImage(imgFire, player.getFrontGrid().x * cellSize +30-(int)(fireScall/2), player.getFrontGrid().y * cellSize + headerBlank+55-(int)(fireScall/2), (int)(fireScall*cellSize/60), (int)(fireScall*cellSize/60), this);
    
                }
            }
        } 

        //米炊く　Yoshida
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                if(grid[i][j].tool == 10 && grid[i][j].hasFood()){
                    if(grid[i][j].cookingGauge < 60.0)grid[i][j].cookingGauge += 0.1;

                    if(grid[i][j].cookingGauge > 0 && grid[i][j].cookingGauge < 60){
                        drawGauge(g, "up", i*cellSize+7, j*cellSize+headerBlank-10, (int)(0.7*cellSize), 8, grid[i][j].cookingGauge/60.0);
                    }
                    else if(grid[i][j].cookingGauge >= 60.0){
                        if(grid[i][j].food.foodName == "rice"){
                            g.drawImage(setToolImage(11), i * cellSize, j * cellSize + headerBlank, cellSize, cellSize, this);
                        }
                    }
                }
            }
        }

        for(int i = 0; i < 5; i++){
            if(waiters[i] != null && waiters[i].active == true){
                //System.out.printf("waiters[%d]のdrawMe()を呼びます\n", i);
                waiters[i].drawMe(g, this);
            }
        }
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
            case 6: return imgRiceBox;
            case 7: return imgTunaBox;
            case 8: return imgSquidBox;
            case 9: return imgSeaweedBox;
            case 10: return imgBoil;
            case 11: return imgBoilRice;
            case 12: return imgPan;


        }
        return imgErrorBlock;
    }
    private Image setCorrectRaw(Food foodInfo){
        if(foodInfo.foodName == "cabbage") return imgCabbage;
        else if(foodInfo.foodName == "tomato") return imgTomato;
        else if(foodInfo.foodName == "cucumber") return imgCucumber;
        else if(foodInfo.foodName == "rice") return imgRice;
        else if(foodInfo.foodName == "tuna") return imgTuna;
        else if(foodInfo.foodName == "squid") return imgSquid;
        else if(foodInfo.foodName == "seaweed") return imgSeaweed;
      
        else return imgErrorBlock;
    }
    private Image setCorrectMethod(Food foodInfo){
        if(foodInfo.foodStatus == 2) return imgKnifeBlack;
        else if(foodInfo.foodStatus == 3)return imgBoilBlack;
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
        }else if(foodInfo.foodName == "rice"){
            if(foodInfo.foodStatus == 1) return imgRice;
            else if(foodInfo.foodStatus == 3) return imgRiceBoil;//boilは3?heiwa
            else return imgErrorBlock;
        }else if(foodInfo.foodName == "tuna"){
            if(foodInfo.foodStatus == 1) return imgTuna;
            else if(foodInfo.foodStatus == 2) return imgTunaCut;
            else return imgErrorBlock;
        }else if(foodInfo.foodName == "squid"){
            if(foodInfo.foodStatus == 1) return imgSquid;
            else if(foodInfo.foodStatus == 2) return imgSquidCut;
            else return imgErrorBlock;
        }else if(foodInfo.foodName == "cucumber"){
            if(foodInfo.foodStatus == 1) return imgCucumber;
            else if(foodInfo.foodStatus == 2) return imgCucumberCut;
            else return imgErrorBlock;
        }else if(foodInfo.foodName == "seaweed"){
            if(foodInfo.foodStatus == 1) return imgSeaweed;
            else return imgErrorBlock;
        }
        return imgErrorBlock;
    }
    public Image setPlateImage(Plate targetPlate){
        Food food[] = new Food[3];
        int cabbage = 0; //そのプレートにおいてそれぞれの食材がどうなっているか
        int tomato = 0; //0:存在しない 1:生 2:カット、3:ボイル
        int cucumber = 0;
        int rice = 0;
        int tuna = 0;
        int squid = 0;
        int seaweed = 0;
 

        //plateに乗っている具材情報を取得
        for(int i = 0; i < 3; i++){
            food[i] = targetPlate.get(i);
            if(food[i] == null){  break; }//これ以上の食材はないのでbreak
            if(food[i].foodName == "cabbage") cabbage = food[i].foodStatus;
            else if(food[i].foodName == "tomato") tomato = food[i].foodStatus;
            else if(food[i].foodName == "cucmber") cucumber = food[i].foodStatus;
            else if(food[i].foodName == "rice") rice = food[i].foodStatus;
            else if(food[i].foodName == "tuna") tuna = food[i].foodStatus;
            else if(food[i].foodName == "squid") squid = food[i].foodStatus;
            else if(food[i].foodName == "seaweed") seaweed = food[i].foodStatus;

        }
        //取得した具材情報を利用してImageにセットする画像を返す。0:未所持,1:未処理,2:カット,3:ボイル,
        
        if(rice==0 && tuna==0 && squid==0 && seaweed==0){
            //System.out.printf("rice = %d", rice);//デバック用
            if(cabbage==1 && tomato==0 && cucumber == 0) return imgCabbage; //未加工キャベツ
            else if(cabbage==0 && tomato==1 && cucumber == 0) return imgTomato; //未加工トマト
            else if(cabbage==0 && tomato==0 && cucumber == 1) return imgCucumber; //未加工きゅうり
            else if(cabbage==2 && tomato==0 && cucumber == 0) return imgCabbageCut; //カットキャベツ
            else if(cabbage==0 && tomato==2 && cucumber == 0) return imgTomatoCut; //カットトマト
            else if(cabbage==0 && tomato==0 && cucumber == 2) return imgCucumberCut; //カットキュウリ
            else if(cabbage == 2 && tomato == 2 && cucumber == 0) return imgCabTom;//キャベツトマト
            else if(cabbage == 2 && tomato == 0 && cucumber == 2) return imgCabCuc;//キャベツキュウリ
            else if(cabbage == 0 && tomato == 2 && cucumber == 2) return imgTomCuc;//トマトキュウリ
            else if(cabbage == 2 && tomato == 2 && cucumber == 2) return imgCabTomCuc;//キャベツトマトキュウリ
        }
        else if(cabbage==0 && tomato==0 && cucumber==0 && squid==0){
            //System.out.print("まぐろ");//デバック用
            if(rice == 1 && tuna == 0 && seaweed== 0) return imgRice;//加工前
            else if(rice == 0 && tuna == 1 && seaweed== 0) return imgTuna;//
            else if(rice == 0 && tuna == 0 && seaweed== 1) return imgSeaweed;//
            else if(rice == 3 && tuna == 0 && seaweed== 0) return imgRiceBoil;//加工後
            else if(rice == 0 && tuna == 2 && seaweed== 0) return imgTunaCut;//
            else if(rice == 3 && tuna == 2 && seaweed== 0) return imgRicTun;//まぐろにぎり
            else if(rice == 3 && tuna == 0 && seaweed== 1) return imgRicSea;//
            else if(rice == 3 && tuna == 2 && seaweed== 1) return imgRicTunSea;//鉄火巻
        }
        else if(cabbage==0 && tomato==0 && cucumber==0 && tuna==0 && seaweed==0){
            //System.out.print("いか");//デバック用
            if(rice == 1 && squid == 0) return imgRice;//加工前
            else if(rice == 0 && squid == 1 ) return imgSquid;//
            else if(rice == 3 && squid == 0 ) return imgRiceBoil;//加工後
            else if(rice == 0 && squid == 2 ) return imgSquidCut;//
            else if(rice == 3 && squid == 2 ) return imgRicTun;//いかにぎり
        }
        else if(cabbage==0 && tomato==0 && cucumber==0 && seaweed==0){
            //System.out.print("海鮮丼");//デバック用
            if(rice == 1 && tuna == 0 && squid== 0) return imgRice;//加工前
            else if(rice == 0 && tuna == 1 && squid== 0) return imgTuna;//
            else if(rice == 0 && tuna == 0 && squid== 1) return imgSquid;//
            else if(rice == 3 && tuna == 0 && squid== 0) return imgRiceBoil;//加工後
            else if(rice == 0 && tuna == 2 && squid== 0) return imgTunaCut;//
            else if(rice == 0 && tuna == 0 && squid== 2) return imgSquidCut;//
            else if(rice == 3 && tuna == 2 && squid== 0) return imgRicTun;//まぐろにぎり
            else if(rice == 3 && tuna == 0 && squid== 2) return imgRicSqu;//いかにぎり
            else if(rice == 0 && tuna == 2 && squid== 2) return imgTunSqu;//
            else if(rice == 3 && tuna == 2 && squid== 2) return imgRicTunSqu;//海鮮丼
        }
        else if(cabbage==0 && tomato==0 && tuna==0 && squid==0){
            //System.out.print("かっぱ巻き");//デバック用
            if(rice == 1 && cucumber == 0 && seaweed== 0) return imgRice;//加工前
            else if(rice == 0 && cucumber == 1 && seaweed== 0) return imgCucumber;//
            else if(rice == 0 && cucumber == 0 && seaweed== 1) return imgSeaweed;//
            else if(rice == 3 && cucumber == 0 && seaweed== 0) return imgRiceBoil;//加工後
            else if(rice == 0 && cucumber == 2 && seaweed== 0) return imgCucumberCut;//
            else if(rice == 3 && cucumber == 2 && seaweed== 0) return imgRicCuc;//
            else if(rice == 3 && cucumber == 0 && seaweed== 1) return imgRicSea;//
            else if(rice == 3 && cucumber == 2 && seaweed== 1) return imgRicCucSea;//かっぱ巻
        }

        return imgErrorBlock;
    }

    public Image setOrderImage(Order order){
        //System.out.println(order.orderName +"の画像を取得します。"); //デバッグ用
        if("salad".equals(order.orderName)){
            //System.out.println(order.orderName +"の画像を取得しました。"); //デバッグ用
            return imgCabTomCuc;
        }else if("tekkamaki".equals(order.orderName)){
            //System.out.println(order.orderName +"の画像を取得しました。"); //デバッグ用
            return imgRicTunSea;
        }else if("kappamaki".equals(order.orderName)){
            //System.out.println(order.orderName +"の画像を取得しました。"); //デバッグ用
            return imgRicCucSea;
        }else if("tunanigiri".equals(order.orderName)){
            //System.out.println(order.orderName +"の画像を取得しました。"); //デバッグ用
            return imgRicTun;
        }else if("ikanigiri".equals(order.orderName)){
            //System.out.println(order.orderName +"の画像を取得しました。"); //デバッグ用
            return imgRicSqu;
        }else if("kaisendon".equals(order.orderName)){
            //System.out.println(order.orderName +"の画像を取得しました。"); //デバッグ用
            return imgRicTunSqu;
        }
        else return null;
    }

    // Imageを返すわけではなく、この関数を呼び出せば画像を貼れる Yoshida
    // paintComponentに書いても良かったけど煩雑になりそうだったので関数化しました。引数が多くてすいません。
    private void setIngredientsImage(int cellSize, int x, int y, int offsetX, int offsetY, Plate plate, Graphics g, int playerDirection){
        Image ingredients[] = new Image[3];
        int holdStatus[] = new int[3];
        Food ing[] = new Food[3];
        int size = cellSize/3;
        int ingOffsetX = 20;
        int ingOffsetY = 20;
        if(playerDirection == 3){ingOffsetY = 0;}
        for(int i=0; i<3; i++){
            if(plate.foods[i] != null){
                ing[i] = plate.foods[i];
                holdStatus[i] = plate.foods[i].foodStatus;
                ing[i].foodStatus = 1; //生の状態を表示したい(調理した食材を皿に置いて、1歩あると画像が生になってしまうのでコメントアウトしてます。)
            }
        }

        for(int i=0; i<3; i++){
            if(ing[i] != null){
                ingredients[i] = setFoodImage(ing[i]); 
                g.setColor(Color.WHITE);
                g.fillOval(x*cellSize+ingOffsetX*i+offsetX-3, y*cellSize+headerBlank+offsetY-ingOffsetY-2, size+5, size+5);
                g.drawImage(ingredients[i], x*cellSize+ingOffsetX*i+offsetX, y*cellSize+headerBlank+offsetY-ingOffsetY, size, size, this);
                ing[i].foodStatus = holdStatus[i];
            }
        }
    }

    //時間に関するメソッド Yoshida
    public void updateTime(int time){
        System.out.print(time+"秒"); //仮のタイマー表示
    }

    // JFrame を取得するメソッド(Controllerでリザルト画面に移るときにゲームのウィンドウを閉じる時に使います) Yoshida
    public JFrame getFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
    private void loadCustomFont() {
        try {
            //File fontFile = new File("font/CHEESE10.TTF"); // フォントファイルのパス
            File fontFile = new File("font/ByteBounce.TTF"); // フォントファイルのパス
            customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(90f); // フォントサイズ24
        } catch (IOException | FontFormatException e) {
            System.err.println("フォントの読み込みに失敗: " + e.getMessage());
            customFont = new Font("Arial", Font.BOLD, 24); // 失敗時はデフォルトのフォント
        }
    }
    public void addWaiter(Image mealImage){
        for(int i = 0; i < 5; i++){
            if(waiters[i] == null || waiters[i].active == false){
                System.out.println("Waiter Instatance made.");
                waiters[i] = new Waiter(model, mealImage, headerBlank);
                return;
            }
        }
    }
}