import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MiniCook extends JFrame {
    DrawModel model;
    DrawView view;
    DrawController cont;

    public MiniCook() {
        System.out.printf("\n---Start---\n\n"); //見やすいように Kome
        model = new DrawModel();
        //model = DrawModel.getInstance();
        view = new DrawView(model);
        cont = new DrawController(model, view);
        view.addKeyListener(cont); // キーリスナーを設定

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