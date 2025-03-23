package game;

import block.BlockApp;
import frame.FrameApp;
import mino.*;
import sound.SoundManager;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PlayManager {

    private static final int WIDTH = 360;
    private static final int HEIGHT = 660;

    // 一列が揃うブロック数
    public static final int BLOCK_COUNT_THRESHOLD = 12;
    public static final int PAUSE_FONT_SIZE = 50;

    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    private Mino currentMino;
    private final int MINO_START_X;
    private final int MINO_START_Y;

    private Mino nextMino;
    private final int NEXT_MINO_X;
    private final int NEXT_MINO_Y;

    private static final Random RANDOM = new Random();
    public static final List<BlockApp> staticBlocks = new ArrayList<>();
    private final Queue<Class<? extends Mino>> minoQueue = new LinkedList<>();
    private final List<GlowEffect> glowEffects = new ArrayList<>();
    private boolean isGameOver = false;

    // サウンド
    private SoundManager soundManager;

    // スコア
    int level = 1;
    int lines;
    int score;

    public static int dropInterval = 60;

    public PlayManager() {

        try {
            initializeBoard();
            System.out.println("PlayManagerコンストラクタでinitializeBoard()が呼び出されました");
        } catch (Exception e) {
            // 例外を確認
            e.printStackTrace();
        }

        MINO_START_X = left_x + (WIDTH / 2) - BlockApp.createBlockSize().SIZE();
        MINO_START_Y = top_y + BlockApp.createBlockSize().SIZE();

        NEXT_MINO_X = right_x + 135;
        NEXT_MINO_Y = top_y + 618;

        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);

        // フィールド音楽の開始
        soundManager = new SoundManager();
        soundManager.playWAV("src/main/resources/バーダックマン.wav");
    }

    private void initializeBoard() {
        left_x = (FrameApp.baseDisplay().width() / 2) - (WIDTH / 2);
        right_x = left_x + WIDTH;
        top_y = 80;
        bottom_y = top_y + HEIGHT;

        // デバッグ: top_yの値をログ出力
        System.out.println("initializeBoard()内でのtop_yの値: " + top_y);
    }

    public Mino pickMino() {
        if (minoQueue.isEmpty()) {
            List<Class<? extends Mino>> minoTypes = Arrays.asList(
                    MinoL.class, MinoJ.class,
                    MinoT.class, MinoI.class,
                    MinoO.class, MinoZ.class,
                    MinoS.class
            );
            Collections.shuffle(minoTypes, RANDOM);
            minoQueue.addAll(minoTypes);
        }
        try {
            // 次のミノの取得
            Class<? extends Mino> minoClass = minoQueue.poll();

            // 次のミノのデバッグ情報を更新
            System.out.println("次のミノ: " + minoClass.getSimpleName());

            // ミノのインスタンスを生成
            return minoClass.getDeclaredConstructor(PlayManager.class).newInstance(this);
        } catch (Exception e) {
            throw new RuntimeException("Mino作成中にエラーが発生しました。", e);
        }
    }

    private boolean isCollision(BlockApp[] blocks) {
        for (BlockApp block : blocks) {
            for (BlockApp staticBlock : staticBlocks) {
                if (block.blockX == staticBlock.blockX && block.blockY == staticBlock.blockY) {
                    // 衝突が検出された
                    return true;
                }
            }
        }
        // 衝突なし
        return false;
    }

    public void update() {
        if (isGameOver) {
            // ゲームオーバー時はすべての処理を停止
            return;
        }

        if (!currentMino.active) {
            // 静止ブロックに追加
            addCurrentMinoToStaticBlocks();
            // 次のミノを生成
            activateNextMino();
            // 行の削除チェック
            checkDeleteRows();
        } else {
            // ミノを更新
            currentMino.update();
        }
    }

    private void addCurrentMinoToStaticBlocks() {
        staticBlocks.addAll(Arrays.asList(currentMino.block));
        currentMino.deactivating = false;
    }

    private void activateNextMino() {

        // 次のミノを現在のミノに切り替え
        currentMino = nextMino;
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        // 天井での衝突をチェック
        if (isCollision(currentMino.block)) {
            System.out.println("ゲームオーバー判定: 天井でミノが衝突");
            isGameOver = true;

            // フィールドの音楽停止
            soundManager.stopBackgroundMusic();

            // ゲームオーバー音の再生
            soundManager.playGameOverSound("src/main/resources/物音03.wav");

            return;
        }

        // 次のミノを生成
        nextMino = pickMino();
        nextMino.setXY(NEXT_MINO_X, NEXT_MINO_Y);
    }

    private void checkDeleteRows() {
        if (isGameOver) {
            return;
        }

        int blockSize = BlockApp.createBlockSize().SIZE();
        // 行が削除されたかを追跡
        boolean anyRowDeleted = false;

        // 全ての行をチェック
        for (int y = top_y; y < bottom_y; y += blockSize) {
            if (isRowComplete(y)) {
                // 行を削除
                deleteRow(y, blockSize);
                // 上のブロックを下に移動
                shiftBlocksDown(y, blockSize);
                // 行が削除されたことを記録
                anyRowDeleted = true;
            }
        }

        // 行が削除された場合のみ音を再生
        if (anyRowDeleted) {
            soundManager.playClearSound("src/main/resources/ウィン.wav");
        }

        // スコアとレベルを更新
        updateScoreAndLevel(anyRowDeleted);
    }

    private void updateScoreAndLevel(boolean rowDeleted) {
        if (rowDeleted) {
            // ライン数を増加
            lines++;
            // スコア加算
            score += 100;

            System.out.println("現在のスコア: " + score);
            System.out.println("消去したライン: " + lines);

            // レベルアップ条件をチェック
            if (lines / 10 > level - 1) {
                level++;
                // レベルアップ時に速度を増加
                increaseSpeed();
            }
        }
    }

    private void increaseSpeed() {
        if (dropInterval > 20) {
            // 最低値 20ms
            dropInterval = Math.max(20, dropInterval - 5);
        } else {
            dropInterval -= -1;
        }
        System.out.println("レベルアップ！ 新しい落下間隔: " + dropInterval);
    }

    private boolean isRowComplete(int y) {
        return (int) staticBlocks.stream()
                .filter(block -> block.blockY == y)
                .count() == BLOCK_COUNT_THRESHOLD;
    }

    private void deleteRow(int y, int blockSize) {
        if (y <= top_y) {
            // 天井の行は削除しない
            return;
        }

        // 光るエフェクトを追加
        triggerGlowEffect(y);

        // 実際のブロック削除
        staticBlocks.removeIf(block -> block.blockY == y);
    }

    private void triggerGlowEffect(int y) {
        int blockSize = BlockApp.createBlockSize().SIZE();
        for (BlockApp block : staticBlocks) {
            if (block.blockY == y) {
                glowEffects.add(new GlowEffect(block.blockX, block.blockY, blockSize));
            }
        }
    }

    private void shiftBlocksDown(int y, int blockSize) {
        staticBlocks.stream()
                .filter(block -> block.blockY < y)
                .forEach(block -> block.blockY += blockSize);
    }

    public void draw(Graphics2D g2) {

        drawGameBoard(g2);
        drawNextMinoFrame(g2);
        currentMino.draw(g2);
        nextMino.draw(g2);
        staticBlocks.forEach(block -> block.draw(g2));
        drawPauseScreen(g2);
        drawGameBoard(g2);
        drawScoreAndLevel(g2);

        // ゲームオーバーの描画を追加（最後に描画）
        drawGameOverScreen(g2);

        // 光るエフェクトを描画
        for (Iterator<GlowEffect> iterator = glowEffects.iterator(); iterator.hasNext(); ) {
            GlowEffect effect = iterator.next();

            // 光るエフェクトの描画
            g2.setColor(new Color(255, 255, 0, effect.alpha)); // 黄色の光
            g2.fillRect(effect.x, effect.y, effect.size, effect.size);

            // エフェクトの更新
            effect.update();
            if (effect.isExpired()) {
                // 寿命が尽きたエフェクトを削除
                iterator.remove();
            }
        }
    }

    private void drawGameBoard(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x, top_y, WIDTH, HEIGHT);

        g2.setColor(Color.GRAY);
        int blockSize = BlockApp.createBlockSize().SIZE();
        for (int y = top_y; y <= bottom_y; y += blockSize) {
            g2.drawLine(left_x, y, right_x, y);
        }
        for (int x = left_x; x <= right_x; x += blockSize) {
            g2.drawLine(x, top_y, x, bottom_y);
        }
    }

    private void drawNextMinoFrame(Graphics2D g2) {
        int x = right_x + 80;
        int y = bottom_y - 120;
        g2.drawRect(x, y, 150, 150);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 40, y + 40);
    }

    private void drawPauseScreen(Graphics2D g2) {
        if (KeyHandler.pausePressed) {
            g2.setColor(Color.YELLOW);
            g2.setFont(g2.getFont().deriveFont((float) PAUSE_FONT_SIZE));
            int x = left_x + 70;
            int y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }
    }

    private void drawGameOverScreen(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (isGameOver) {
            int x = left_x + 25;
            int y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
        }
    }

    private void drawScoreAndLevel(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("SCORE : " + score, right_x - 640, top_y + 30);
        g2.drawString("LINES : " + lines, right_x - 640, top_y + 50);
        g2.drawString("LEVEL : " + level, right_x - 640, top_y + 70);
    }

    public int getNextMinoX() {
        return NEXT_MINO_X;
    }

    public int getNextMinoY() {
        return NEXT_MINO_Y;
    }
}