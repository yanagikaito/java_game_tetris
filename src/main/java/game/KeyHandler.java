package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public static boolean upPressed;
    public static boolean downPressed;
    public static boolean leftPressed;
    public static boolean rightPressed;
    public static boolean pausePressed;
    public static boolean checkDrawTime = false;

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (code == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (code == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            if (pausePressed) {
                pausePressed = false;
            } else {
                pausePressed = true;
            }
        }

        // デバッグ
        if (code == KeyEvent.VK_T) {
            if (checkDrawTime == false) {
                checkDrawTime = true;
            } else if (checkDrawTime == true) {
                checkDrawTime = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
