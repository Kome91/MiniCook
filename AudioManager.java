import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {
    private static Clip bgmClip;

    // BGMを再生（WAVのみ対応）
    public static void playBGM(String filePath) {
        stopBGM(); // 既存のBGMを停止
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            bgmClip = AudioSystem.getClip();
            bgmClip.open(audioStream);
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // ループ再生
            bgmClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // BGM停止
    public static void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
        }
    }

    // SEを再生（WAVのみ対応）
    public static void playSE(String filePath) {
        new Thread(() -> {
            try {
                File soundFile = new File(filePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
                Clip seClip = AudioSystem.getClip();
                seClip.open(audioStream);
                seClip.start(); // 短いSEならそのまま再生
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }).start();
    }
}


/*
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class AudioManager {
    private static MediaPlayer bgmPlayer;

    // BGMを再生するメソッド
    public static void playBGM(String filePath) {
        if (bgmPlayer != null) {
            bgmPlayer.stop(); // すでに再生中なら停止
        }
        Media media = new Media(new File(filePath).toURI().toString());
        bgmPlayer = new MediaPlayer(media);
        bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // ループ再生
        bgmPlayer.play();
    }

    // BGMを停止するメソッド
    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    // SEを再生するメソッド（複数同時再生可能）
    public static void playSE(String filePath) {
        Media media = new Media(new File(filePath).toURI().toString());
        MediaPlayer sePlayer = new MediaPlayer(media);
        sePlayer.play();
    }
}
*/