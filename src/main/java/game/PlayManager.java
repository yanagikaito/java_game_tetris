package game;

import block.BlockApp;
import frame.FrameApp;
import mino.*;
import sound.SoundManager;

import javax.swing.Timer;

import java.awt.*;
import java.util.*;
import java.util.List;

public class PlayManager {

    private static final int WIDTH = 360;
    private static final int HEIGHT = 660;

    public static final int PAUSE_FONT_SIZE = 50;

    // 時間カウンター
    private int timeCounter = 0;

    // 銀色ブロック追加間隔
    private static final int SILVER_BLOCK_INTERVAL = 1800;
    private static final int MAX_ATTEMPTS = 10;

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
    // ATBゲージの現在値
    private int atbGauge = 0;
    // ATBゲージの最大値
    private static final int ATB_MAX = 1800;
    // 銀のブロックが出現可能か
    private boolean isSilverBlockReady = false;
    private static final int GLOW_ALPHA = 255;

    // コンボカウント
    private int comboCount = 0;

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

        // Timerの初期化と開始
        Timer timer = new Timer(SILVER_BLOCK_INTERVAL, e -> gameLoop());
        timer.start();
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

    private void addSilverMino() {

        int blockSize = BlockApp.createBlockSize().SIZE();

        // ランダムにミノの種類を選択
        List<Class<? extends Mino>> minoTypes = Arrays.asList(
                MinoL.class, MinoJ.class, MinoT.class, MinoI.class, MinoO.class, MinoZ.class, MinoS.class
        );
        Class<? extends Mino> silverMinoType = minoTypes.get(RANDOM.nextInt(minoTypes.size()));

        try {
            // ランダムなミノを生成
            Mino silverMino = silverMinoType.getDeclaredConstructor(PlayManager.class).newInstance(this);

            // 銀色に変更
            for (BlockApp block : silverMino.block) {
                block.blockC = Color.LIGHT_GRAY;
            }

            boolean positionValid = false;
            int attempts = 0;

            // 最大10回試行
            while (!positionValid && attempts < MAX_ATTEMPTS) {

                // ミノの幅を計算 (最大4ブロック分)
                int minoWidth = silverMino.block.length * blockSize;

                // X座標が左端から右端まで収まるように範囲を制限
                // ミノの右端がフィールドからはみ出さないように制限
                int minX = left_x + BlockApp.createBlockSize().SIZE();
                int maxX = right_x - minoWidth;
                int randomX = minX + RANDOM.nextInt((maxX - minX) / blockSize) * blockSize;

                // 範囲外の場合の調整
                randomX = Math.max(randomX, left_x);
                randomX = Math.min(randomX, right_x - minoWidth);

                // 最下段または既存のブロック直上に配置
                // フィールドの底
                int lowestY = bottom_y;
                for (BlockApp staticBlock : staticBlocks) {
                    if (staticBlock.blockX >= randomX && staticBlock.blockX < randomX + minoWidth) {
                        if (staticBlock.blockY < lowestY) {
                            lowestY = Math.min(lowestY, staticBlock.blockY);
                        }
                    }
                }
                int spawnY = lowestY - blockSize;
                if (spawnY < top_y) {
                    // フィールドの上端を超えないように調整
                    spawnY = top_y;
                } else if (spawnY + blockSize > bottom_y) {
                    // フィールドの底に収まるように調整
                    spawnY = bottom_y - blockSize;
                }

                // 銀色ミノの衝突チェックを適用
                boolean collisionDetected = false;
                for (BlockApp silverBlock : silverMino.block) {
                    silverBlock.blockX = randomX;
                    silverBlock.blockY = spawnY;
                    if (isCollision(new BlockApp[]{silverBlock})) {
                        collisionDetected = true;
                        break;
                    }
                }

                if (!collisionDetected) {
                    positionValid = true;

                    // ミノを配置
                    silverMino.setXY(randomX, spawnY);
                    staticBlocks.addAll(Arrays.asList(silverMino.block));
                    System.out.println("銀色ミノが生成されました。X: " + randomX + ", Y: " + spawnY);
                } else {
                    // 再試行
                    attempts++;
                }
            }

            if (!positionValid) {
                System.out.println("銀色ミノの配置に失敗しました。試行回数が上限に達しました。");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCollision(BlockApp[] blocks) {

        for (BlockApp block : blocks) {
            // 左端または右端を超えた場合も衝突とみなす
            if (block.blockX < left_x || block.blockX + BlockApp.createBlockSize().SIZE() > right_x) {
                return true;
            }

            // 他の静止ブロックとの衝突チェック
            for (BlockApp staticBlock : staticBlocks) {
                if (block.blockX == staticBlock.blockX && block.blockY == staticBlock.blockY) {
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

        timeCounter++;

        updateATBGauge();

        if (timeCounter >= SILVER_BLOCK_INTERVAL) {
            timeCounter = 0;
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

    private void updateATBGauge() {
        if (atbGauge < ATB_MAX) {
            // ゲージを1ずつ増加
            atbGauge++;
        } else {
            // 満タンになったらフラグをセット
            isSilverBlockReady = true;
            // ゲージをリセット
            atbGauge = 0;
        }
    }

    private void checkAndAddSilverMino() {
        if (isSilverBlockReady) {
            // 銀色ブロックを生成
            addSilverMino();
            // フラグをリセット
            isSilverBlockReady = false;
            System.out.println("ATBゲージ満タンにより銀色ブロックが出現しました！");
        }
    }

    private void gameLoop() {
        // ATBゲージを更新
        updateATBGauge();
        // 銀色ミノの出現を確認
        checkAndAddSilverMino();
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
        // このサイクルで消えたライン数（ここで初期化）
        int linesCleared = 0;

        // 全ての行をチェック
        for (int y = top_y; y < bottom_y; y += blockSize) {
            if (isRowComplete(y)) {
                // 行を削除
                deleteRow(y, blockSize);
                // 上のブロックを下に移動
                shiftBlocksDown(y, blockSize);
                anyRowDeleted = true;
                // ライン数をカウント
                linesCleared++;
            }
        }

        // 行が削除された場合のみスコア計算と音を再生
        if (anyRowDeleted) {
            // 基本ポイント
            int basePoints = 100;

            // スコア加算（2回目以降の消去でのみボーナス適用）
            if (comboCount > 0) {
                score += (basePoints * linesCleared) + (comboCount * 50);
            } else {
                score += basePoints * linesCleared;
            }

            // 総ライン数を更新
            lines += linesCleared;
            // コンボカウントを増加
            comboCount++;

            System.out.println("現在のスコア: " + score);
            System.out.println("消去したライン: " + linesCleared);
            System.out.println("コンボ数: " + comboCount);

            // サウンド再生
            soundManager.playClearSound("src/main/resources/ウィン.wav");
        } else {
            // コンボが途切れた場合
            comboCount = 0;
        }

        // スコアとレベルの更新
        updateScoreAndLevel(anyRowDeleted);
    }

    private void updateScoreAndLevel(boolean rowDeleted) {
        if (rowDeleted && lines / 10 > level - 1) {
            level++;
            increaseSpeed();
            System.out.println("レベルアップ！ 新しいレベル: " + level);
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
        int blockSize = BlockApp.createBlockSize().SIZE();
        return (int) staticBlocks.stream()
                .filter(block -> block.blockY == y)
                // ブロック数が横幅と一致する場合
                .count() == (WIDTH / blockSize);
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
        drawATBGauge(g2);

        // ゲームオーバーの描画を追加（最後に描画）
        drawGameOverScreen(g2);

        // 光るエフェクトを描画
        for (Iterator<GlowEffect> iterator = glowEffects.iterator(); iterator.hasNext(); ) {
            GlowEffect effect = iterator.next();

            // 光るエフェクトの描画
            // 黄色の光
            g2.setColor(new Color(255, 255, 0, effect.alpha));
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

    private void drawATBGauge(Graphics2D g2) {
        int gaugeWidth = 200;
        int gaugeHeight = 20;
        int gaugeX = 50;
        int gaugeY = 50;

        // 背景
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(gaugeX, gaugeY, gaugeWidth, gaugeHeight);

        Color glowColor = new Color(0, 0, 255, GLOW_ALPHA);
        g2.setColor(glowColor);

        // ゲージの進行
        int filledWidth = (int) (gaugeWidth * ((double) atbGauge / ATB_MAX));
        g2.fillRect(gaugeX, gaugeY, filledWidth, gaugeHeight);

        // 枠線
        g2.setColor(Color.WHITE);
        g2.drawRect(gaugeX, gaugeY, gaugeWidth, gaugeHeight);
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