package cn.cedo;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.DecelerateAnimation;
import cn.cedo.render.RenderUtil;
import cn.dxg.Screen;
import cn.langya.font.FontManager;
import cn.langya.utils.MouseUtil;
import net.minecraft.util.ResourceLocation;

public class MenuButton implements Screen {

    public final String text;
    private Animation hoverAnimation;
    public float x, y, width, height;
    public Runnable clickAction;

    public MenuButton(String text) {
        this.text = text;
    }


    @Override
    public void initGui() {
        hoverAnimation = new DecelerateAnimation(200, 1);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    private static final ResourceLocation rs = new ResourceLocation("client/menu-rect.png");

    @Override
    public void drawScreen(int mouseX, int mouseY) {

        boolean hovered = MouseUtil.isHovering(x, y, width, height, mouseX, mouseY);
        hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);


        RenderUtil.color(-1);
        RenderUtil.drawImage(rs, x, y, width, height);

        FontManager.M22.drawCenteredString(text, x + width / 2f, y + FontManager.M22.getMiddleOfBox(height), -1);
    }

    public void drawOutline() {
        RenderUtil.drawImage(rs, x, y, width, height);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hovered = MouseUtil.isHovering(x, y, width, height, mouseX, mouseY);
        if (hovered) {
            clickAction.run();
        }

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }
}