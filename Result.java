import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Result extends JFrame {
    public Result(){
        setTitle("Result"); // ウィンドウのタイトルを設定
        setSize(600,500); // ウィンドウサイズを設定
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 閉じるボタンでアプリケーションを終了
        setLocationRelativeTo(null); // 画面中央に表示
        setLayout(new BorderLayout()); // レイアウトをボーダーレイアウトに設定

        // タイトルラベルの作成と設定
        JLabel titleLabel = new JLabel("Result", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30)); // フォントの設定
        add(titleLabel, BorderLayout.CENTER); // 画面中央にタイトルラベルを配置

        // ボタンパネルの作成（横並び配置）
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // 横並びにする（間隔20px）

        // リスタートボタンの作成
        JButton restartButton = new JButton("もう一度");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20)); // ボタンのフォント設定
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 現在のリザルト画面を閉じる
                MiniCook.main(null); // MiniCook のメインメソッドを実行
            }
        });

        // 終了ボタンの作成
        JButton closeButton = new JButton("終了する");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20)); // ボタンのフォント設定
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // 現在のウィンドウを閉じる
                System.exit(0); // アプリケーションを終了する
            }
        });

        // パネルにボタンを追加
        buttonPanel.add(restartButton);
        buttonPanel.add(closeButton);

        // ボタンパネルを画面下部に追加
        add(buttonPanel, BorderLayout.SOUTH);
    }

    //時間切れになった時に以下を他のファイル(ModelかController)で呼び出して下さい
    //new Result().setVisible(true);
    
}
