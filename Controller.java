import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class DrawController implements KeyListener {
    protected DrawModel model;
    protected DrawView view;
    protected Player player;
    protected Timer orderTimer;

    public DrawController(DrawModel m, DrawView v) {
        model = m;
        view = v;
        player = model.getPlayer(); //ここでplayerを取得しておく

        System.out.println("DrawModel in Controller: " + model);

        model.generateOrder();
        view.repaint();

        //こんな文法あるんだね。知らんかった Kome
        orderTimer = new Timer(30*1000, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                model.generateOrder();
                view.repaint();
                System.out.println("新しい注文が追加されました！");
            }
        });
        orderTimer.start();
        System.out.println("Timer started: " + orderTimer);

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
                player.action();
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
        view.repaint();
    }
    public void stopOrderTimer() {
        if (orderTimer != null) {
            orderTimer.stop();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
