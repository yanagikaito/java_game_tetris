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
}
