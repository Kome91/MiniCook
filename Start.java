import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Start extends JFrame {
    public Start(){
        setTitle("MiniCook"); //ウィンドウのタイトルを設定
        setSize(600,500); //ウィンドウサイズを設定
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //閉じるボタンでアプリケーションを終了
        setLocationRelativeTo(null); //画面中央に表示
        setLayout(new BorderLayout()); // レイアウトをボーダーレイアウトに設定

        // タイトルラベルの作成と設定
        JLabel titleLabel = new JLabel("MiniCook", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));// フォントの設定
        add(titleLabel, BorderLayout.CENTER);// 画面中央にタイトルラベルを配置

        // スタートボタンの作成
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));// ボタンのフォント設定

        // ボタンのクリックイベント処理
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // スタート画面を閉じる
                MiniCook.main(null); // MiniCook のメインメソッドを実行
            }
        });
        add(startButton, BorderLayout.SOUTH);// ボタンを画面下部に配置
    }

    // メインメソッド：スタート画面を表示
    public static void main(String[] args) {
        new Start().setVisible(true);
    }
}
