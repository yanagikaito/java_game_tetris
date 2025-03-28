package sound;

import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    // フィールド音楽専用クリップ
    private Clip backgroundClip;
    // ゲームオーバー音や消える音用クリップ
    private Clip effectClip;

    public void playWAV(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            // フィールド音楽専用
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            // 無限ループ
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            // リソース解放
            backgroundClip.close();
        }
    }

    public void playClearSound(String filePath) {
        // エフェクト音専用
        playEffectSound(filePath);
    }

    public void playGameOverSound(String filePath) {
        // エフェクト音専用
        playEffectSound(filePath);
    }

    public void playBlockPutSound(String filePath) {
        // エフェクト音専用
        playEffectSound(filePath);
    }

    public void playBlockRotate(String filePath) {
        // エフェクト音専用
        playEffectSound(filePath);
    }

    private void playEffectSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            // エフェクト音専用
            effectClip = AudioSystem.getClip();
            effectClip.open(audioStream);
            // 単発再生
            effectClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}