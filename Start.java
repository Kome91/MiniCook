import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JPanel {
    private MiniCook mainApp;

    public Start(MiniCook mainApp) {
        this.mainApp = mainApp; // MiniCook のインスタンスを保持

        setLayout(new BorderLayout());

        // タイトルラベルの作成
        JLabel titleLabel = new JLabel("MiniCook", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, BorderLayout.CENTER);

        // スタートボタンの作成
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));

        // ボタンのクリックイベント
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.startGame(); // MiniCook の startGame() を呼び出し
            }
        });
        add(startButton, BorderLayout.SOUTH);
    }
}
