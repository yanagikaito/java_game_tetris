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

        int margine = 2;
        int size = createBlockSize().SIZE() - (margine * 2);

        // グラデーションの設定
        GradientPaint gradient = new GradientPaint(blockX + margine, blockY + margine, blockC,
                blockX + margine, blockY + margine + size, Color.WHITE);

        g2.setPaint(gradient);
        g2.fillRect(blockX + margine, blockY + margine, size, size);

        // 枠線の描画
        g2.setColor(Color.BLACK);
        g2.drawRect(blockX + margine, blockY + margine, size, size);

        for (int i = 0; i < 10; i++) {
            // ランダムな位置とサイズを生成
            int particleX = blockX + margine + (int) (Math.random() * size);
            int particleY = blockY + margine + (int) (Math.random() * size);
            int particleSize = (int) (Math.random() * 5 + 1); // 1～5のサイズ

            // 半透明の白色またはランダムな色
            g2.setColor(new Color(255, 255, 255, (int) (Math.random() * 128 + 128)));
            g2.fillOval(particleX, particleY, particleSize, particleSize);

            // 明度を変化
            float[] hsbValues = Color.RGBtoHSB(blockC.getRed(), blockC.getGreen(), blockC.getBlue(), null);
            blockC = Color.getHSBColor(hsbValues[0], hsbValues[1], (float) (Math.random() * 0.2 + 0.8));
        }
    }
}