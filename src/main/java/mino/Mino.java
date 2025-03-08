package mino;

import block.Block;
import block.BlockApp;
import game.KeyHandler;
import game.PlayManager;

import java.awt.*;

public abstract class Mino {

    public BlockApp[] block = new BlockApp[4];
    public BlockApp[] tempB = new BlockApp[4];
    public int autoDropCounter = 0;
    public int direction = 1;
    boolean leftCollision;
    boolean rightCollision;
    boolean bottomCollision;
    public boolean active = true;

    public abstract void getDirection1();

    public abstract void getDirection2();

    public abstract void getDirection3();

    public abstract void getDirection4();

    public void checkMovementCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // 左の壁
        for (int i = 0; i < block.length; i++) {
            if (block[i].blockX == PlayManager.left_x) {
                leftCollision = true;
            }
        }

        // 右の壁
        for (int i = 0; i < block.length; i++) {
            if (block[i].blockX + BlockApp.createBlockSize().SIZE() == PlayManager.right_x) {
                System.out.println("block[i].blockX = " + block[i].blockX);
                System.out.println("BlockApp.createBlockSize().SIZE() = " + BlockApp.createBlockSize().SIZE());
                System.out.println("PlayManager.right_x = " + PlayManager.right_x);
                rightCollision = true;
            }
        }

        for (int i = 0; i < block.length; i++) {
            if (block[i].blockY + BlockApp.createBlockSize().SIZE() + 10 == PlayManager.bottom_y) {
                bottomCollision = true;
                autoDropCounter = 0;
                System.out.println("blockY: " + block[i].blockY);
                System.out.println("PlayManager.bottom_y: " + PlayManager.bottom_y);
                System.out.println("bottomCollision: " + bottomCollision);
            }
        }
    }

    public void checkRotationCollision() {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // 左の壁
        for (int i = 0; i < block.length; i++) {
            if (tempB[i].blockX < PlayManager.left_x) {
                leftCollision = true;
            }
        }

        // 右の壁
        for (int i = 0; i < block.length; i++) {
            if (tempB[i].blockX + BlockApp.createBlockSize().SIZE() > PlayManager.right_x) {
                System.out.println("tempB[i].blockX = " + block[i].blockX);
                System.out.println("BlockApp.createBlockSize().SIZE() = " + BlockApp.createBlockSize().SIZE());
                System.out.println("PlayManager.right_x = " + PlayManager.right_x);
                rightCollision = true;
            }
        }

        for (int i = 0; i < block.length; i++) {
            if (tempB[i].blockY + BlockApp.createBlockSize().SIZE() > PlayManager.bottom_y) {
                bottomCollision = true;
                autoDropCounter = 0;
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
            System.out.println("bottomCollision = " + bottomCollision);
            active = false;
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

    public void draw(Graphics2D g2) {
        int margin = 2;
        int blockSize = BlockApp.createBlockSize().SIZE() - (margin * 2);
        for (BlockApp b : block) {
            g2.setColor(b.blockC);
            g2.fillRect(b.blockX + margin, b.blockY + margin, blockSize, blockSize);
        }
    }
}