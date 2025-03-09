package mino;

import block.Block;
import block.BlockApp;

import java.awt.*;

public class MinoSquare extends Mino {

    public MinoSquare() {
        init();
    }

    public void init() {
        Block block = createBlock(Color.YELLOW);
        block.createBlock();
    }

    @Override
    public void setXY(int x, int y) {

        // o o 0,1
        // o o 2,3
        //
        block[0].blockX = x;
        block[0].blockY = y;
        block[1].blockX = block[0].blockX;
        block[1].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();
        block[2].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        block[2].blockY = block[0].blockY;
        block[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        block[3].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();
    }

    @Override
    public void getDirection1() {

    }

    @Override
    public void getDirection2() {

    }

    @Override
    public void getDirection3() {

    }

    @Override
    public void getDirection4() {

    }
}