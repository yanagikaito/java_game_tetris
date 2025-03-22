package mino;

import block.Block;
import block.BlockApp;
import game.KeyHandler;
import game.PlayManager;

import java.awt.*;

public abstract class Mino {

    public BlockApp[] block = new BlockApp[4];
    public BlockApp[] tempB = new BlockApp[4];
    public BlockApp[] ghostBlock = new BlockApp[4];
    public PlayManager playManager;
    public int autoDropCounter = 0;
    public int direction = 1;
    boolean leftCollision;
    boolean rightCollision;
    boolean bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    public int deactivateCounter = 0;

    public abstract void getDirection1();

    public abstract void getDirection2();

    public abstract void getDirection3();

    public abstract void getDirection4();

    public Mino(PlayManager playManager) {
        this.playManager = playManager;
        // 必要な初期化処理をここに書く
        for (int i = 0; i < ghostBlock.length; i++) {
            ghostBlock[i] = new BlockApp(Color.GRAY); // 仮の色で初期化
        }
    }

    public void checkMovementCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // 左の壁
        for (int i = 0; i < block.length; i++) {
            if (block[i].blockX == PlayManager.left_x) {
                leftCollision = true;
            }
        }

        // 右の壁
        for (int i = 0; i < block.length; i++) {
            if (block[i].blockX + BlockApp.createBlockSize().SIZE() == PlayManager.right_x) {
                rightCollision = true;
            }
        }

        for (int i = 0; i < block.length; i++) {
            if (block[i].blockY + BlockApp.createBlockSize().SIZE() == PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    public void checkRotationCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        checkStaticBlockCollision();

        // 左の壁
        for (int i = 0; i < block.length; i++) {
            if (tempB[i].blockX < PlayManager.left_x) {
                leftCollision = true;
            }
        }

        // 右の壁
        for (int i = 0; i < block.length; i++) {
            if (tempB[i].blockX + BlockApp.createBlockSize().SIZE() > PlayManager.right_x) {
                rightCollision = true;
            }
        }

        for (int i = 0; i < block.length; i++) {
            if (tempB[i].blockY + BlockApp.createBlockSize().SIZE() > PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    private void checkStaticBlockCollision() {

        // staticBlocksの衝突チェック
        for (int i = 0; i < PlayManager.staticBlocks.size(); i++) {

            int targetX = PlayManager.staticBlocks.get(i).blockX;
            int targetY = PlayManager.staticBlocks.get(i).blockY;

            // 下
            for (int ii = 0; ii < block.length; ii++) {
                if (block[ii].blockY + BlockApp.createBlockSize().SIZE()
                        == targetY && block[ii].blockX == targetX) {
                    bottomCollision = true;
                }
            }

            // 左
            for (int ii = 0; ii < block.length; ii++) {
                if (block[ii].blockX - BlockApp.createBlockSize().SIZE()
                        == targetX && block[ii].blockY == targetY) {
                    leftCollision = true;
                }
            }

            // 右
            for (int ii = 0; ii < block.length; ii++) {
                if (block[ii].blockX + BlockApp.createBlockSize().SIZE()
                        == targetX && block[ii].blockY == targetY) {
                    rightCollision = true;
                }
            }
        }
    }

    public Block createBlock(Color c) {

        return () -> {
            block[0] = new BlockApp(c);
            block[1] = new BlockApp(c);
            block[2] = new BlockApp(c);
            block[3] = new BlockApp(c);

            tempB[0] = new BlockApp(c);
            tempB[1] = new BlockApp(c);
            tempB[2] = new BlockApp(c);
            tempB[3] = new BlockApp(c);
        };
    }

    public abstract void setXY(int x, int y);

    public void updateXY(int direction) {

        checkRotationCollision();

        // 衝突が発生していない時にブロックを回転できる
        if (leftCollision == false && rightCollision == false && bottomCollision == false) {

            this.direction = direction;
            block[0].blockX = tempB[0].blockX;
            block[0].blockY = tempB[0].blockY;
            block[1].blockX = tempB[1].blockX;
            block[1].blockY = tempB[1].blockY;
            block[2].blockX = tempB[2].blockX;
            block[2].blockY = tempB[2].blockY;
            block[3].blockX = tempB[3].blockX;
            block[3].blockY = tempB[3].blockY;
        }
    }

    public void update() {

        if (!deactivating && active) {
            // ゴーストミノの更新
            updateGhost();
        }

        if (deactivating) {
            deactivating();
        }

        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }
            KeyHandler.upPressed = false;
        }
        checkMovementCollision();

        if (KeyHandler.downPressed) {

            if (!bottomCollision) {

                block[0].blockY += BlockApp.createBlockSize().SIZE();
                block[1].blockY += BlockApp.createBlockSize().SIZE();
                block[2].blockY += BlockApp.createBlockSize().SIZE();
                block[3].blockY += BlockApp.createBlockSize().SIZE();

                autoDropCounter = 0;
            }
            KeyHandler.downPressed = false;

        }
        if (KeyHandler.leftPressed) {

            if (!leftCollision) {

                block[0].blockX -= BlockApp.createBlockSize().SIZE();
                block[1].blockX -= BlockApp.createBlockSize().SIZE();
                block[2].blockX -= BlockApp.createBlockSize().SIZE();
                block[3].blockX -= BlockApp.createBlockSize().SIZE();
            }

            KeyHandler.leftPressed = false;

        }
        if (KeyHandler.rightPressed) {

            if (!rightCollision) {

                block[0].blockX += BlockApp.createBlockSize().SIZE();
                block[1].blockX += BlockApp.createBlockSize().SIZE();
                block[2].blockX += BlockApp.createBlockSize().SIZE();
                block[3].blockX += BlockApp.createBlockSize().SIZE();
            }

            KeyHandler.rightPressed = false;
        }

        if (bottomCollision) {
            deactivating = true;

        } else {

            autoDropCounter++;

            if (autoDropCounter == PlayManager.dropInterval) {

                block[0].blockY += BlockApp.createBlockSize().SIZE();
                block[1].blockY += BlockApp.createBlockSize().SIZE();
                block[2].blockY += BlockApp.createBlockSize().SIZE();
                block[3].blockY += BlockApp.createBlockSize().SIZE();
                autoDropCounter = 0;
            }
        }
    }

    public void updateGhost() {
        // ゴーストミノを現在のミノと同じ座標にコピー
        for (int i = 0; i < block.length; i++) {
            ghostBlock[i] = new BlockApp(block[i].blockC);
            ghostBlock[i].blockX = block[i].blockX;
            ghostBlock[i].blockY = block[i].blockY;
        }

        // ゴーストミノをフィールド内で下げる処理を開始
        boolean collisionDetected = false;
        while (!collisionDetected) {
            for (int i = 0; i < ghostBlock.length; i++) {
                // ゴーストがNEXT欄の領域に入らないようにチェック
                if (ghostBlock[i].blockX >= playManager.getNextMinoX() &&
                        ghostBlock[i].blockX < playManager.getNextMinoX() + 150 &&
                        ghostBlock[i].blockY >= playManager.getNextMinoY() &&
                        ghostBlock[i].blockY < playManager.getNextMinoY() + 150) {
                    // NEXT欄の処理をスキップ
                    return;
                }

                // 1ブロック分下げる
                ghostBlock[i].blockY += BlockApp.createBlockSize().SIZE();
            }

            for (int i = 0; i < ghostBlock.length; i++) {
                if (checkGhostCollision(ghostBlock[i])) {
                    collisionDetected = true;
                    break;
                }
            }

            if (collisionDetected) {
                // 衝突した場合、一段上に戻す
                for (int i = 0; i < ghostBlock.length; i++) {
                    ghostBlock[i].blockY -= BlockApp.createBlockSize().SIZE();
                }
            }
        }
    }

    // ゴーストミノの衝突判定
    private boolean checkGhostCollision(BlockApp ghostBlock) {
        // フィールドの底部との衝突
        if (ghostBlock.blockY >= PlayManager.bottom_y) {
            return true;
        }

        // NEXT欄のブロックは無視
        if (ghostBlock.blockX >= playManager.getNextMinoX() &&
                ghostBlock.blockX < playManager.getNextMinoX() + 150 &&
                ghostBlock.blockY >= playManager.getNextMinoY() &&
                ghostBlock.blockY < playManager.getNextMinoY() + 150) {
            // NEXT欄を無視
            return false;
        }

        // 静止ブロックとの衝突
        for (BlockApp staticBlock : PlayManager.staticBlocks) {
            if (ghostBlock.blockX == staticBlock.blockX && ghostBlock.blockY == staticBlock.blockY) {
                return true;
            }
        }

        return false;
    }

    public void drawGhost(Graphics2D g2) {
        int margin = 2;
        int blockSize = BlockApp.createBlockSize().SIZE() - (margin * 2);

        for (BlockApp b : ghostBlock) {

            g2.setColor(new Color(200, 200, 200, 100)); // ゴーストの半透明
            g2.fillRect(b.blockX + margin, b.blockY + margin, blockSize, blockSize);
        }
    }

    private void deactivating() {

        deactivateCounter++;

        if (deactivateCounter == 45) {

            deactivateCounter = 0;
            checkMovementCollision();

            if (bottomCollision) {
                active = false;
            }
        }
    }

    public void draw(Graphics2D g2) {

        // ゴースト位置を更新
        updateGhost();
        // ゴーストを描画
        drawGhost(g2);

        // 通常のミノ描画
        int margin = 2;
        int blockSize = BlockApp.createBlockSize().SIZE() - (margin * 2);
        for (BlockApp b : block) {
            g2.setColor(b.blockC);
            g2.fillRect(b.blockX + margin, b.blockY + margin, blockSize, blockSize);
        }
    }
}