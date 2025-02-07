import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class DrawController implements KeyListener {
    protected DrawModel model;
    protected DrawView view;
    protected Player player;
    protected Timer orderTimer;
    public boolean spacePushing =false;
    private Timer gameTimer;
    private MiniCook mainApp;
    private int cCount = 0;

    public DrawController(DrawModel m, DrawView v, MiniCook app) {
        model = m;
        view = v;
        player = model.getPlayer(); //ここでplayerを取得しておく
        mainApp = app;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int dx = 0, dy = 0;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                dy = -1;
                player.direction = 1; //プレイヤーの向きを変更
                model.movePlayer(dx, dy);
                break;
            case KeyEvent.VK_S:
                dy = 1;
                player.direction = 3;
                model.movePlayer(dx, dy);
                break;
            case KeyEvent.VK_A:
                dx = -1;
                player.direction = 2;
                model.movePlayer(dx, dy);
                break;
            case KeyEvent.VK_D:
                dx = 1;
                player.direction = 4;
                model.movePlayer(dx, dy);
                break;
            case KeyEvent.VK_C:
                cCount++;
                if(cCount >= 5){ cCount = 0; printCredit(); }
                break;
            case KeyEvent.VK_SPACE: //スペースキーでaction
                spacePushing =true;
                //player.action();
                break;
            case KeyEvent.VK_J: //Jキーで拾う
                player.pick_up(); 
                break;
            case KeyEvent.VK_K: //Kキーで置く
                player.put();
                break;
            case KeyEvent.VK_I: //デバッグ用にIキーで情報を表示する
                model.printInfo();
                break;
            case KeyEvent.VK_ESCAPE: // ESCキーでゲーム終了
            System.exit(0);
            break;
        }
        
        // 再描画
        //view.repaint();
    }
    public void stopOrderTimer() {
        if (orderTimer != null) {
            for(int i=0; i<model.orders.length; i++){
                if(model.orders[i] != null){
                    model.orders[i].cancelTimer();
                }
            }
            orderTimer.stop();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE: // スペースキーを離したら false にする
                spacePushing = false;
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    //　以下ゲーム時間に関わるメソッド Yoshida
    public void startGame(){
        //スタート画面、ゲーム画面、リザルト画面を同一ウィンドウで表示する都合上、このメソッド内でオーダータイマーとゲームタイマーを管理 Yoshida
        model.generateOrder();
        view.repaint();

        //こんな文法あるんだね。知らんかった Kome
        orderTimer = new Timer(8*1000, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                model.generateOrder();
                view.repaint();
                System.out.println("新しい注文が追加されました！");
            }
        });
        orderTimer.start();
        //System.out.println("Timer started: " + orderTimer);
        
        if(gameTimer != null) return; //二重起動防止

        gameTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (model.getGameTime() > 0) {
                    model.decreaseTime();
                    view.updateTime(model.getGameTime());
                    if(model.getGameTime() == 10){
                        AudioManager se = new AudioManager();
                        se.playSE("./sound/music_timer2.wav");
                    }else if(model.getGameTime() == 0){
                        AudioManager.playBGM("./sound/music_resultSE.wav");
                    }
                } 
                else {
                    gameTimer.stop();
                    gameTimer = null;
                    stopOrderTimer();//オーダータイマーも止める
    
                    // ゲーム終了時に Result 画面を表示
                    System.out.printf("\n\n\n---Result---\n\n\n"); //デバッグ用
                    AudioManager.playBGM("./sound/music_result.wav");
                    mainApp.showResult();
                    
                }
            }
        });
    
        gameTimer.start(); // タイマー開始
    }
    private void printCredit(){
        System.out.printf("\r\n" + //
                        "\r\n" + //
                        "----------------------------------\r\n" + //
                        "\r\n" + //
                        "--- Credit ---\r\n" + //
                        "\r\n" + //
                        "----------------------------------\r\n" + //
                        "\r\n" + //
                        "<Team Members>\r\n" + //
                        "\r\n" + //
                        "Y. Kometani\r\n" + //
                        "\r\n" + //
                        "S. Suzuki\r\n" + //
                        "\r\n" + //
                        "H. Yoshida\r\n" + //
                        "\r\n" + //
                        "\r\n" + //
                        "\r\n" + //
                        "<Special Thanks>\r\n" + //
                        "\r\n" + //
                        "S. Maejima (Character Designer)\r\n" + //
                        "\r\n" + //
                        "K. Isahaya (Background Designer)\r\n" + //
                        "\r\n" + //
                        "K. Kubo (Design Adviser)\r\n" + //
                        "\r\n" + //
                        "and All Players\r\n" + //
                        "\r\n" + //
                        "----------------------------------\r\n" + //
                        "\r\n" + //
                        "\r\n" + //
                        "");
    }
}
