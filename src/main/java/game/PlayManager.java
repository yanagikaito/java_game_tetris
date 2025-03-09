package game;

import block.BlockApp;
import frame.FrameApp;
import mino.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PlayManager {

    public final int WIDTH = 360;
    public final int HEIGHT = 700;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;

    Mino nextMino;
    final int NEXT_MINO_X;
    final int NEXT_MINO_Y;
    public static ArrayList<BlockApp> staticBlocks = new ArrayList<>();

    public static int dropInterval = 60;

    public PlayManager() {

        left_x = (FrameApp.baseDisplay().width() / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        System.out.println("right_x = " + right_x);
        top_y = 80;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - BlockApp.createBlockSize().SIZE();
        MINO_START_Y = top_y + BlockApp.createBlockSize().SIZE();

        NEXT_MINO_X = right_x + 135;
        NEXT_MINO_Y = top_y + 618;

        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);
    }

    private Mino pickMino() {

        Mino mino = null;
        int i = new Random().nextInt(7);

        switch (i) {
            case 0:
                mino = new MinoL1();
                break;
            case 1:
                mino = new MinoL2();
                break;
            case 2:
                mino = new MinoT();
                break;
            case 3:
                mino = new MinoBar();
                break;
            case 4:
                mino = new MinoSquare();
                break;
            case 5:
                mino = new MinoZ1();
                break;
            case 6:
                mino = new MinoZ2();
                break;
        }
        return mino;
    }

    public void update() {

        currentMino.update();
    }

    public void draw(Graphics2D g2) {

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x, top_y, WIDTH, HEIGHT - 8);

        // 次のテトリスのブロックが表示される枠
        int x = right_x + 80;
        int y = bottom_y - 158;
        g2.drawRect(x, y, 150, 150);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 40, y + 40);

        if (currentMino != null) {
            currentMino.draw(g2);
        }

        nextMino.draw(g2);

        // 一時停止
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }
}