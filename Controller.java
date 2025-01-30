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
                model.movePlayer(dx, dy);
                player.direction = 1; //プレイヤーの向きを変更
                break;
            case KeyEvent.VK_S:
                dy = 1;
                model.movePlayer(dx, dy);
                player.direction = 3;
                break;
            case KeyEvent.VK_A:
                dx = -1;
                model.movePlayer(dx, dy);
                player.direction = 2;
                break;
            case KeyEvent.VK_D:
                dx = 1;
                model.movePlayer(dx, dy);
                player.direction = 4;
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
        orderTimer = new Timer(5*1000, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                model.generateOrder();
                view.repaint();
                System.out.println("新しい注文が追加されました！");
            }
        });
        orderTimer.start();
        System.out.println("Timer started: " + orderTimer);
        
        if(gameTimer != null) return; //二重起動防止

        gameTimer = new Timer(5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (model.getGameTime() > 0) {
                    model.decreaseTime();
                    view.updateTime(model.getGameTime());
                } else {
                    gameTimer.stop();
                    gameTimer = null;
                    stopOrderTimer();//オーダータイマーも止める
    
                    // ゲーム終了時に Result 画面を表示
                    System.out.println("リザルト画面に切り替えます。"); //デバッグ用
                    mainApp.showResult();
                    
                }
            }
        });
    
        gameTimer.start(); // タイマー開始
    }
}
