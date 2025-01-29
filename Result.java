import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Result extends JPanel {
    private MiniCook mainApp;

    public Result(MiniCook mainApp) {
        this.mainApp = mainApp; // MiniCook のインスタンスを保持

        setLayout(new BorderLayout());

        // タイトルラベルの作成
        JLabel titleLabel = new JLabel("Result", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, BorderLayout.CENTER);

        // ボタンパネルの作成（横並び）
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // リスタートボタン
        JButton restartButton = new JButton("もう一度");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.restartGame(); // ゲームをリスタート
            }
        });

        // 終了ボタン
        JButton closeButton = new JButton("終了する");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // アプリを終了
            }
        });

        // パネルにボタンを追加
        buttonPanel.add(restartButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
