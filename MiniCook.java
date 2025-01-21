import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MiniCook extends JFrame {
    DrawModel model;
    DrawView view;
    DrawController cont;

    public MiniCook() {
        //前はこっちだったんだけど
        //model = new DrawModel();
        //こっちになってるけど、これって意味が違うんかな Kome
        model = DrawModel.getInstance();
        view = new DrawView(model);
        cont = new DrawController(model, view);
        view.addKeyListener(cont); // キーリスナーを設定

        //model.setImageAtPosition(5, 5, 1);
        //model.getGrid()[5][5].food = new Food(1,0,0,true);

        //model.getPlayer().move(1, 1, model.getGrid());

        this.setBackground(Color.white);
        this.setTitle("MiniCook");
        this.setSize(960, 720);
        this.add(view);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new MiniCook();
    }
}