package net.minecraft.client.gui;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.DecelerateAnimation;
import cn.cedo.misc.ColorUtil;
import cn.cedo.shader.blur.GaussianBlur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import cn.langya.font.FontManager;
import cn.cedo.shader.RoundedUtil;

import java.awt.*;

public class GuiButton extends Gui
{
    protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
    protected int width;
    protected int height;
    public int xPosition;
    public int yPosition;
    public String displayString;
    public int id;
    public boolean enabled;
    public boolean visible;
    protected boolean hovered;

    public GuiButton(int buttonId, int x, int y, String buttonText)
    {
        this(buttonId, x, y, 200, 20, buttonText);
    }

    public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        this.width = 200;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.displayString = buttonText;
    }

    protected int getHoverState(boolean mouseOver)
    {
        int i = 1;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = 2;
        }

        return i;
    }

    public Animation hoverAnimation = new DecelerateAnimation(250, 1, Direction.BACKWARDS);

    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            GaussianBlur.startBlur();
            if(hovered) RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height, 10, new Color(0, 0, 0, 160));
            else RoundedUtil.drawRound(this.xPosition, this.yPosition, this.width, this.height, 10, new Color(0, 0, 0, 80));
            GaussianBlur.endBlur(25,2);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            hoverAnimation.setDirection(Direction.FORWARDS);
            if (!hoverAnimation.isDone() || hoverAnimation.finished(Direction.FORWARDS)) {
                RoundedUtil.drawRoundOutline(this.xPosition, this.yPosition, width, height, 10, 1,
                        ColorUtil.applyOpacity(Color.WHITE, 0), ColorUtil.applyOpacity(Color.WHITE, hoverAnimation.getOutput().floatValue()));
            } else {
                hoverAnimation.reset();
            }

            this.mouseDragged(mc, mouseX, mouseY);

            FontManager.M18.drawCenteredString(this.displayString, this.xPosition + this.width / 2, (float) (this.yPosition + (this.height - 8.0) / 2), -1);
        }
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}
