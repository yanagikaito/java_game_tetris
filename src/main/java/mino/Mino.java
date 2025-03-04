package mino;

import block.Block;
import block.BlockApp;

import java.awt.*;

public class Mino {

    public BlockApp[] block = new BlockApp[4];
    public BlockApp[] tempB = new BlockApp[4];

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

    public void setXY(int x, int y) {

    }

    public void updateXY(int direction) {

    }

    public void update() {

    }

    public void draw(Graphics2D g2) {

        g2.setColor(block[0].blockC);
        g2.fillRect(block[0].blockX, block[0].blockY,
                BlockApp.createBlockSize().SIZE(), BlockApp.createBlockSize().SIZE());
        g2.fillRect(block[1].blockX, block[1].blockY,
                BlockApp.createBlockSize().SIZE(), BlockApp.createBlockSize().SIZE());
        g2.fillRect(block[2].blockX, block[2].blockY,
                BlockApp.createBlockSize().SIZE(), BlockApp.createBlockSize().SIZE());
        g2.fillRect(block[3].blockX, block[3].blockY,
                BlockApp.createBlockSize().SIZE(), BlockApp.createBlockSize().SIZE());
    }
}
