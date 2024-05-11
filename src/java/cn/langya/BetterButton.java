package cn.langya;

import org.union4dev.base.gui.font.CFontRenderer;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/5/9 17:00
 */

public class BetterButton {
    public final float x,y;
    public float width;
    public final float height;
    private final String text;
    private final CFontRenderer fontRenderer;
    public boolean isHover;

    public BetterButton(String text , float x , float y , CFontRenderer fontRenderer) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.fontRenderer = fontRenderer;
        this.height = fontRenderer.getHeight() * 2.0F;
        this.width = fontRenderer.getWidth(text) * 2.0F;
    }

    public BetterButton(String text , double x , double y , CFontRenderer fontRenderer) {
        this.x = (float) x;
        this.y = (float) y;
        this.text = text;
        this.fontRenderer = fontRenderer;
        this.height = fontRenderer.getHeight() * 2.0F;
        this.width = fontRenderer.getWidth(text) * 2.0F;
    }

    public void draw() {
        if (isHover) RoundedUtil.drawRound(x, y, width, height, 5, new Color(0, 0, 0, 80)); else
            RoundedUtil.drawRound(x, y, width, height, 5, new Color(0, 0, 0, 160));

        fontRenderer.drawStringWithShadow(text,x + width / 4,y + height / 4,-1);
    }

}
