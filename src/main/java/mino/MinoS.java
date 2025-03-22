package mino;

import block.Block;
import block.BlockApp;
import game.PlayManager;

import java.awt.*;

public class MinoS extends Mino {

    public MinoS(PlayManager playManager) {
        super(playManager);
        init();
    }

    public void init() {
        Block block = createBlock(Color.GREEN);
        block.createBlock();
    }

    @Override
    public void setXY(int x, int y) {

        //    o o   2,3
        //  o o   1,0
        //
        block[0].blockX = x;
        block[0].blockY = y;
        block[1].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        block[1].blockY = block[0].blockY;
        block[2].blockX = block[0].blockX;
        block[2].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        block[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        block[3].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
    }

    @Override
    public void getDirection1() {

        //    o o   2,3
        //  o o   1,0
        //
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX - BlockApp.createBlockSize().SIZE();
        tempB[1].blockY = block[0].blockY;
        tempB[2].blockX = block[0].blockX;
        tempB[2].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        tempB[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[3].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();

        updateXY(1);
    }

    @Override
    public void getDirection2() {

        //  o    1
        //  o o  0,2
        //    o    3
        tempB[0].blockX = block[0].blockX;
        tempB[0].blockY = block[0].blockY;
        tempB[1].blockX = block[0].blockX;
        tempB[1].blockY = block[0].blockY - BlockApp.createBlockSize().SIZE();
        tempB[2].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[2].blockY = block[0].blockY;
        tempB[3].blockX = block[0].blockX + BlockApp.createBlockSize().SIZE();
        tempB[3].blockY = block[0].blockY + BlockApp.createBlockSize().SIZE();

        updateXY(2);
    }

    @Override
    public void getDirection3() {

        getDirection1();
    }

    @Override
    public void getDirection4() {

        getDirection2();
    }
}