import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Result extends JPanel {
    private MiniCook mainApp;
    private Font pixelFont;

    public Result(MiniCook mainApp) {
        this.mainApp = mainApp; // MiniCook のインスタンスを保持

        setLayout(new GridBagLayout()); // GridBagLayout を使用
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0); // 上下の余白を設定

        // フォントを読み込む
        loadCustomFont();
        
        // タイトルラベルの作成
        JLabel titleLabel = new JLabel("Result", SwingConstants.CENTER);
        titleLabel.setFont(pixelFont.deriveFont(100f));
        add(titleLabel, gbc); // ラベルを追加

        // ボタンパネルの作成（横並び）
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // リスタートボタン
        JButton restartButton = new JButton("Restart");
        restartButton.setFont(pixelFont.deriveFont(50f));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainApp.restartGame(); // ゲームをリスタート
            }
        });

        // 終了ボタン
        JButton closeButton = new JButton("Close");
        closeButton.setFont(pixelFont.deriveFont(50f));
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // アプリを終了
            }
        });

        // パネルにボタンを追加
        buttonPanel.add(restartButton);
        buttonPanel.add(closeButton);

        // ボタンパネルの配置設定
        gbc.gridy = 1;
        add(buttonPanel, gbc); // ボタンパネルを追加
    }

    private void loadCustomFont() {
        try {
            File fontFile = new File("font/ByteBounce.ttf"); // フォントのパス
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            pixelFont = new Font("Monospaced", Font.PLAIN, 24); // フォールバック用フォント
        }
    }
}
