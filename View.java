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
    //private Image Image11;
    private Image ImagePlayer;
    private Image ImagePlayer_up;
    private Image ImagePlayer_left;
    private Image ImagePlayer_down;
    private Image ImagePlayer_right;
    private Image imgCabTom;
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
        Image9 = new ImageIcon("CutTomato.png").getImage();
        Image10 = new ImageIcon("TomatoBox.png").getImage();
        ImagePlayer_up = new ImageIcon("player_up.png").getImage();
        ImagePlayer_left = new ImageIcon("player_left.png").getImage();
        ImagePlayer_down = new ImageIcon("player_down.png").getImage();
        ImagePlayer_right = new ImageIcon("player_right.png").getImage();
        imgCabTom = new ImageIcon("cabbage_and_tomato.png").getImage();
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

                if(grid[i][j].isPlatePlaced == true){ //皿は食材の土台にあるべきなので、皿のみの特殊描画処理
                    System.out.println("皿の描画を試みました");
                    g.drawImage(Image7, i * cellSize, j * cellSize, cellSize, cellSize, this);
                }else{
                    
                }

                
                //画像を描画
                Image selectedImage = null;
                
                if(grid[i][j].food != null){ //食材の描画です
                    int[] receivedInfo = grid[i][j].food.getInfo(); //そのマスのFood情報を受け取る;
                    selectedImage = setFoodImage(receivedInfo);
                    //setFoodImage(receivedInfo); //テスト用ですよ
                }else if(grid[i][j].tool != 0){ //そのマスが何かしらのツールだった場合にツールの描画
                    selectedImage = setToolImage(grid[i][j].tool);
                }

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

        //プレイヤーが皿を持っていたら皿をプレイヤーの上に表示
        if(player.hasPlate == true){
            int foodSize = cellSize * 2/3;
            int offsetX = cellSize /6;
            int offsetY = cellSize /6;
            if(player.direction == 1) offsetY -= cellSize / 2;
            else if(player.direction == 2) offsetX -= cellSize / 2;
            else if(player.direction == 3) offsetY += cellSize / 2;
            else if(player.direction == 4) offsetX += cellSize / 2;
            g.drawImage(Image7, player.x * cellSize + offsetX, player.y * cellSize + offsetY , foodSize, foodSize, this);
        }else{
        }
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
    }
    private Image setFoodImage(int[] info){
        if(info[0]==1 && info[1]==0 && info[2] == 0) return Image1; //未加工キャベツ
        else if(info[0]==0 && info[1]==1 && info[2] == 0) return Image8; //未加工トマト
        else if(info[0]==2 && info[1]==0 && info[2] == 0) return Image5; //カットキャベツ
        else if(info[0]==0 && info[1]==1 && info[2] == 0) return Image8; //未加工キャベツ
        else if(info[0]==0 && info[1]==1 && info[2] == 0) return Image8; //未加工キャベツ
        else if(info[0] == 1 && info[1] ==  1){
            return imgCabTom;
        }
        return Image3;
    }
    private Image setToolImage(int toolId){
        switch(toolId){
            case 1: return Image2;
            case 2: return Image4;
            case 3: return Image6;
            case 4: return Image10;
        }
        return Image3;
    }
}