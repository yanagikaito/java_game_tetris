package mino;

import block.Block;
import block.BlockApp;

import java.awt.*;

public class MinoL1 extends Mino {

    public MinoL1() {
        init();
    }

    public void init() {
        Block block = createBlock(Color.ORANGE);
        block.createBlock();
    }

    public void setXY(int x, int y) {

        // o
        // o
        // o o
        block[0].blockX = x;
        block[0].blockY = y;
        block[1].blockX = block[0].blockX;
        block[1].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        block[2].blockX = block[0].blockX;
        block[2].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();
        block[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        block[3].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();
    }
}
