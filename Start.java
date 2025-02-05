import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Start extends JPanel {
    private MiniCook mainApp;
    private Font pixelFont;

    public Start(MiniCook mainApp) {
        this.mainApp = mainApp; // MiniCook のインスタンスを保持

        setLayout(new GridBagLayout()); // グリッドバッグレイアウトを使用

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0); // 上下の余白を設定

        // フォントを読み込む
        loadCustomFont();

        // タイトルラベルの作成
        JLabel titleLabel = new JLabel("MiniCook", SwingConstants.CENTER);
        titleLabel.setFont(pixelFont.deriveFont(100f));
        add(titleLabel, gbc); // ラベルを追加

        // スタートボタンの作成
        JButton startButton = new JButton("Start");
        startButton.setFont(pixelFont.deriveFont(80f));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AudioManager se = new AudioManager();
                se.playSE("./sound/music_start.wav");
                mainApp.startGame(); // MiniCook の startGame() を呼び出し
            }
        });

        gbc.gridy = 1; // ボタンを2行目に配置
        add(startButton, gbc); // ボタンを追加
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
