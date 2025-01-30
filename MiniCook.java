import javax.swing.*;
import java.awt.*;

class MiniCook extends JFrame {
    DrawModel model;
    DrawView view;
    DrawController cont;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MiniCook() {
        System.out.printf("\n---Start---\n\n");
        model = new DrawModel();
        view = new DrawView(model);
        cont = new DrawController(model, view, this);
        model.getPlayer().setController(cont);
        view.setController(cont);
        view.addKeyListener(cont);

        this.setBackground(Color.white);
        this.setTitle("MiniCook");
        this.setSize(960, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // カードレイアウトの設定
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 各画面の追加
        Start startScreen = new Start(this);
        Result resultScreen = new Result(this);

        cardPanel.add(startScreen, "start");
        cardPanel.add(resultScreen, "result");

        // ゲーム画面
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(view, BorderLayout.CENTER);

        cardPanel.add(gamePanel, "game");

        add(cardPanel);
        cardLayout.show(cardPanel, "start");
    }

    // スタート画面からゲーム画面に切り替える
    public void startGame() {
        cardLayout.show(cardPanel, "game");
        cont.startGame();

        // キーボード入力を受け取るためにフォーカスを設定
        view.requestFocusInWindow();
    }

    // ゲーム終了時にリザルト画面を表示する
    public void showResult() {
        System.out.println("リザルト画面を表示します。");
        cardLayout.show(cardPanel, "result");
    }

    // リザルト画面からもう一度プレイ
    public void restartGame() {
        model.reset(); // ゲームデータをリセット（必要なら実装）
        startGame(); // ゲームを開始
    }

    public static void main(String[] args) {
        new MiniCook().setVisible(true);
    }
}
