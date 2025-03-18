package game;

import block.BlockApp;
import frame.FrameApp;
import mino.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PlayManager {

    public final int WIDTH = 360;
    public final int HEIGHT = 660;
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
    private final Random RANDOM = new Random(12345);
    private boolean firstBatchGenerated = false;
    public static ArrayList<BlockApp> staticBlocks = new ArrayList<>();
    private final Queue<Class<? extends Mino>> minoQueue = new LinkedList<>();

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
        
        if (minoQueue.isEmpty()) {
            // 最初の一回のみ指定された順序でミノを追加
            if (!firstBatchGenerated) {
                // 指定された順序でミノをリストに追加
                List<Class<? extends Mino>> firstMinoTypes = Arrays.asList(
                        MinoL1.class, MinoL2.class,
                        MinoT.class, MinoBar.class,
                        MinoSquare.class, MinoZ1.class,
                        MinoZ2.class
                );
                minoQueue.addAll(firstMinoTypes);
                // 一度だけ実行されるようにフラグをセット
                firstBatchGenerated = true;
            } else {
                // 以降はランダムな順序
                List<Class<? extends Mino>> minoTypes = Arrays.asList(
                        MinoL1.class, MinoL2.class,
                        MinoT.class, MinoBar.class,
                        MinoSquare.class, MinoZ1.class,
                        MinoZ2.class
                );
                Collections.shuffle(minoTypes, RANDOM);
                minoQueue.addAll(minoTypes);
            }
        }
        try {
            return minoQueue.poll().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void update() {

        if (!currentMino.active) {

            staticBlocks.add(currentMino.block[0]);
            staticBlocks.add(currentMino.block[1]);
            staticBlocks.add(currentMino.block[2]);
            staticBlocks.add(currentMino.block[3]);

            currentMino.deactivating = false;

            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);

        } else {
            currentMino.update();
        }
    }

    public void draw(Graphics2D g2) {

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x, top_y, WIDTH, HEIGHT);

        // 網目模様の描画
        g2.setColor(Color.GRAY);
        int blockSize = BlockApp.createBlockSize().SIZE();

        // 横線を描画
        for (int y = top_y; y <= bottom_y; y += blockSize) {
            g2.drawLine(left_x, y, right_x, y);
        }

        // 縦線を描画
        for (int x = left_x; x <= right_x; x += blockSize) {
            g2.drawLine(x, top_y, x, bottom_y);
        }


        // 次のテトリスのブロックが表示される枠
        int x = right_x + 80;
        int y = bottom_y - 120;
        g2.drawRect(x, y, 150, 150);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 40, y + 40);

        if (currentMino != null) {
            currentMino.draw(g2);
        }

        nextMino.draw(g2);

        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

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