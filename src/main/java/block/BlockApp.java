package block;

import java.awt.*;

public class BlockApp {

    public int blockX;
    public int blockY;
    public Color blockC;

    public static BlockSize createBlockSize() {
        return new BlockSize(30);
    }

    public BlockApp(Color c) {
        this.blockC = c;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(blockC);
        g2.fillRect(blockX, blockY, createBlockSize().SIZE(), createBlockSize().SIZE());
    }
}
