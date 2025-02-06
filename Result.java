import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Result extends JPanel {
    private MiniCook mainApp;
    private Font pixelFont;
    private int score;
    private JLabel scoreLabel; // スコア表示用ラベル
    private JLabel starLabel;

    public Result(MiniCook mainApp) {
        this.mainApp = mainApp;
        this.score = 0; // 初期スコア

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 20, 0);



        // フォントを読み込む
        loadCustomFont();
        
        // タイトルラベル
        JLabel titleLabel = new JLabel("Result", SwingConstants.CENTER);
        titleLabel.setFont(pixelFont.deriveFont(100f));
        gbc.gridy = 0;
        add(titleLabel, gbc);

        // スコアラベル（変更可能にする）
        scoreLabel = new JLabel("Score : " + score, SwingConstants.CENTER);
        scoreLabel.setFont(pixelFont.deriveFont(80f));
        gbc.gridy = 1;
        add(scoreLabel, gbc);

        starLabel = new JLabel(getStarRating(score), SwingConstants.CENTER);
        starLabel.setFont(new Font("Meiryo", Font.PLAIN, 80));         
        gbc.gridy = 2;
        add(starLabel, gbc);

        // ボタンパネル
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton restartButton = new JButton("Restart");
        restartButton.setFont(pixelFont.deriveFont(50f));
        restartButton.setPreferredSize(new Dimension(300, 100));
        restartButton.addActionListener(e -> mainApp.restartGame());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(pixelFont.deriveFont(50f));
        closeButton.setPreferredSize(new Dimension(300, 100));
        closeButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(restartButton);
        buttonPanel.add(closeButton);

        gbc.gridy = 3;
        add(buttonPanel, gbc);
    }

    // スコアを更新するメソッド（ゲーム終了時に呼び出す）
    public void updateScore(int newScore) {
        this.score = newScore;
        scoreLabel.setText("Score : " + score);
        starLabel.setText(getStarRating(score));
        repaint(); // 再描画
        revalidate(); // レイアウト更新
    }

    // スコアに応じた星の文字列を返す
    private String getStarRating(int score) {
        if (score >= 500) {
            return "\u2605 \u2605 \u2605"; // ★ ★ ★
        } else if (score >= 250) {
            return "\u2605 \u2605 \u2606"; // ★ ★ ☆
        } else if(score > 0){
            return "\u2605 \u2606 \u2606"; // ★ ☆ ☆
        } else{
            return "\u2606 \u2606 \u2606"; // ☆ ☆ ☆
        }
    }

    private void loadCustomFont() {
        try {
            File fontFile = new File("font/ByteBounce.ttf");
            pixelFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        } catch (Exception e) {
            e.printStackTrace();
            pixelFont = new Font("Monospaced", Font.PLAIN, 24);
        }
    }
}