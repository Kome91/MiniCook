import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JPanel {
    private MiniCook mainApp;

    public Start(MiniCook mainApp) {
        this.mainApp = mainApp; // MiniCook のインスタンスを保持

        setLayout(new GridBagLayout()); // グリッドバッグレイアウトを使用
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0); // 上下の余白を設定

        // タイトルラベルの作成
        JLabel titleLabel = new JLabel("MiniCook", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        add(titleLabel, gbc); // ラベルを追加

        // スタートボタンの作成
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.startGame(); // MiniCook の startGame() を呼び出し
            }
        });

        gbc.gridy = 1; // ボタンを2行目に配置
        add(startButton, gbc); // ボタンを追加
    }
}
