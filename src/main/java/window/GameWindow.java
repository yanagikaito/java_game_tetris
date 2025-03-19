package window;

import factory.FrameFactory;
import frame.GameFrame;
import game.KeyHandler;
import game.PlayManager;

import javax.swing.*;

import java.awt.*;

import static frame.FrameApp.baseDisplay;

public class GameWindow extends JPanel implements Window, Runnable {

    private GameFrame gameFrame = FrameFactory.createFrame(baseDisplay(), this);
    private static GameWindow instance;
    private PlayManager playManager = new PlayManager(this);
    private KeyHandler keyHandler = new KeyHandler();
    private Thread gameThread;

    private JLabel debugLabel;

    private GameWindow() {
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.startThread();
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
        this.setLayout(null);

        // デバッグ用ラベルの初期化
        debugLabel = new JLabel("デバッグ情報: ");
        // ラベルの位置とサイズを調整
        debugLabel.setBounds(10, 10, 400, 20);
        // テキストカラーを設定
        debugLabel.setForeground(Color.WHITE);
        // パネルにラベルを追加
        this.add(debugLabel);
    }

    // デバッグ情報を既存ラベルに更新するメソッド
    public void updateDebugText(String debugText) {
        if (debugLabel != null) {
            // 既存のラベルのテキストを更新
            debugLabel.setText(debugText);
        }
    }

    public static synchronized GameWindow getInstance() {
        if (instance == null) {
            instance = new GameWindow();
        }
        return instance;
    }

    @Override
    public void run() {
        int fps = 60;
        int nanosecond = 1000000000;
        double drawInterval = (double) nanosecond / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= nanosecond) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    @Override
    public void frame() {
        gameFrame.createFrame();
    }

    public void startThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {

        if (KeyHandler.pausePressed == false) {
            playManager.update();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        playManager.draw(g2);
    }
}