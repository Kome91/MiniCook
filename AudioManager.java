import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManager {
    private static Clip bgmClip;

    // BGMを再生
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

    // SEを再生
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
