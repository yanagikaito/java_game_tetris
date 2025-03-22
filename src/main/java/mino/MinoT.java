package mino;

import block.Block;
import block.BlockApp;
import game.PlayManager;

import java.awt.*;

public class MinoT extends Mino {

    public MinoT(PlayManager playManager) {
        super(playManager);
        init();
    }

    public void init() {
        Block block = createBlock(Color.MAGENTA);
        block.createBlock();
    }

    @Override
    public void setXY(int x, int y) {

        //   o     1
        // o o o 2,0,3
        //
        block[0].blockX = x;
        block[0].blockY = y;
        block[1].blockX = block[0].blockX;
        block[1].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        block[2].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        block[2].blockY = block[0].blockY;
        block[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        block[3].blockY = block[0].blockY;
    }

    @Override
    public void getDirection1() {

        //   o     1
        // o o o 2,0,3
        //
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX;
        tempB[1].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        tempB[2].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        tempB[2].blockY = block[0].blockY;
        tempB[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[3].blockY = block[0].blockY;

        updateXY(1);
    }

    @Override
    public void getDirection2() {

        //  o    2
        //  o o  0,1
        //  o    3
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[1].blockY = block[0].blockY;
        tempB[2].blockX = block[0].blockX;
        tempB[2].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        tempB[3].blockX = block[0].blockX;
        tempB[3].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();

        updateXY(2);
    }

    @Override
    public void getDirection3() {

        //
        // o o o 3,0,2
        //   o     1
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX;
        tempB[1].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();
        tempB[2].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[2].blockY = block[0].blockY;
        tempB[3].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        tempB[3].blockY = block[0].blockY;

        updateXY(3);
    }

    @Override
    public void getDirection4() {

        //   o   3
        // o o 1,0
        //   o   2
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        tempB[1].blockY = block[0].blockY;
        tempB[2].blockX = block[0].blockX;
        tempB[2].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();
        tempB[3].blockX = block[0].blockX;
        tempB[3].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();

        updateXY(4);
    }
}