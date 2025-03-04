package game;

import block.BlockApp;
import frame.FrameApp;
import mino.Mino;
import mino.MinoL1;

import java.awt.*;

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

    public PlayManager() {
        left_x = (FrameApp.baseDisplay().width() / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 80;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDTH / 2) - BlockApp.createBlockSize().SIZE();
        MINO_START_Y = top_y + BlockApp.createBlockSize().SIZE();

        currentMino = new MinoL1();
        currentMino.setXY(MINO_START_X, MINO_START_Y);
    }

    public void update() {

        currentMino.update();
    }

    public void draw(Graphics2D g2) {

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x, top_y, WIDTH, HEIGHT);

        // 次のテトリスのブロックが表示される枠
        int x = right_x + 80;
        int y = bottom_y - 150;
        g2.drawRect(x, y, 150, 150);

        if (currentMino != null) {
            currentMino.draw(g2);
        }
    }
}