package mino;

import block.Block;
import block.BlockApp;

import java.awt.*;

public class MinoL extends Mino {

    public MinoL() {
        init();
    }

    public void init() {
        Block block = createBlock(Color.ORANGE);
        block.createBlock();
    }

    @Override
    public void setXY(int x, int y) {

        //     o     3
        // o o o 1,0,2
        //
        block[0].blockX = x;
        block[0].blockY = y;
        block[1].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        block[1].blockY = block[0].blockY;
        block[2].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        block[2].blockY = block[0].blockY;
        block[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        block[3].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
    }

    @Override
    public void getDirection1() {

        //     o     3
        // o o o 1,0,2
        //
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        tempB[1].blockY = block[0].blockY;
        tempB[2].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[2].blockY = block[0].blockY;
        tempB[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[3].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();

        updateXY(1);
    }

    @Override
    public void getDirection2() {

        // o   1
        // o   0
        // o o 2 3
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX;
        tempB[1].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        tempB[2].blockX = block[0].blockX;
        tempB[2].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();
        tempB[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[3].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();

        updateXY(2);
    }

    @Override
    public void getDirection3() {

        //
        // o o o 2,0,1
        // o     3
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[1].blockY = block[0].blockY;
        tempB[2].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        tempB[2].blockY = block[0].blockY;
        tempB[3].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        tempB[3].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();

        updateXY(3);
    }

    @Override
    public void getDirection4() {

        // o o 3,2
        //   o   0
        //   o   1
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX;
        tempB[1].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();
        tempB[2].blockX = block[0].blockX;
        tempB[2].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        tempB[3].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        tempB[3].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();

        updateXY(4);
    }
}
