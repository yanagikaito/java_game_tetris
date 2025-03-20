package game;

public class GlowEffect {

    int x, y, size;
    int alpha = 255; // 初期透明度

    public GlowEffect(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    public void update() {
        // 徐々に透明度を減らす
        alpha -= 15;
    }

    public boolean isExpired() {
        // 寿命が尽きたか判定
        return alpha <= 0;
    }
}