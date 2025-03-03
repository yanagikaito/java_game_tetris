package game;

import frame.FrameApp;

import java.awt.*;

public record PlayManager(int WIDTH, int HEIGHT) {

    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    public void init() {
        left_x = (FrameApp.baseDisplay().width() / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;
    }

    public void update() {

    }

    public void draw(Graphics2D g2) {

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x, top_y, WIDTH, HEIGHT);

        // 次のテトリスのブロックが表示される枠
        int x = right_x + 80;
        int y = bottom_y - 150;
        g2.drawRect(x, y, 150, 150);
    }
}