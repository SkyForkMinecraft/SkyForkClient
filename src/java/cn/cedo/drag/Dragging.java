package cn.cedo.drag;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.DecelerateAnimation;
import cn.cedo.misc.ColorUtil;
import cn.cedo.shader.RoundedUtil;
import cn.langya.RenderUtil;
import cn.langya.font.FontManager;
import cn.langya.utils.MouseUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ibm.icu.impl.duration.impl.Utils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.ScaledResolution;
import org.union4dev.base.Access;

import java.awt.*;
import java.util.List;

@Getter
@Setter
public class Dragging implements Access.InstanceAccess {
    @Expose
    @SerializedName("scale-x")
    @Getter
    @Setter
    private float bPos;

    @Expose
    @SerializedName("scale-y")
    @Getter
    @Setter
    private float cPos;

    @Expose
    @SerializedName("x")
    @Getter
    @Setter
    private float xPos;

    @Expose
    @SerializedName("y")
    @Getter
    @Setter
    private float yPos;

    @Expose
    @SerializedName("name")
    @Getter
    @Setter
    private String name;

    public float initialXVal;
    public float initialYVal;

    private float startX, startY;
    private boolean dragging;

    @Getter
    @Setter
    private float width, height;

    @Getter
    private Class<?> moduleClazz;

    public Animation hoverAnimation = new DecelerateAnimation(100, 1, Direction.BACKWARDS);

    public Dragging(Class<?> clazz,String name, float initialXVal, float initialYVal) {
        this.moduleClazz = clazz;
        this.name = name;
        this.xPos = initialXVal;
        this.yPos = initialYVal;
        this.initialXVal = initialXVal;
        this.initialYVal = initialYVal;
    }

    public final void onDraw(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        boolean hovering = MouseUtil.isHovering(xPos, yPos, width, height, mouseX, mouseY);
        if (dragging) {
            xPos = (mouseX - startX);
            yPos = (mouseY - startY);
            if (xPos < 0)
                xPos = 0;
            if (yPos < 0)
                yPos = 0;
            if (xPos + getWidth() > sr.getScaledWidth())
                xPos = sr.getScaledWidth() - getWidth();
            if (yPos + getHeight() > sr.getScaledHeight())
                yPos = sr.getScaledHeight() - getHeight();
            this.setBPos((float) (xPos / sr.getScaledWidth_double()));
            this.setCPos((float) (yPos / sr.getScaledHeight_double()));
        }
        hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        if ((xPos - 4 + xPos - 4 + width + 8) / 2 < (float) sr.getScaledWidth() / 2) {
            FontManager.MB14.drawString("X:" + String.format("%.1f", xPos), xPos - 4 + width + 8,
                    yPos + 6, new Color(255, 255, 255, 160));
            FontManager.MB14.drawString("Y:" + String.format("%.1f", yPos), xPos - 4 + width + 8,
                    yPos + 4 + FontManager.MB14.getStringWidth("Y:" + String.format("%.1f", yPos)) - 4, new Color(255, 255, 255, 160));
        } else {
            FontManager.MB14.drawString("X:" + String.format("%.1f", xPos), xPos - 3 -
                    FontManager.MB14.getStringWidth("X:" + String.format("%.1f", xPos)) - 1, yPos + 6, new Color(255, 255, 255, 160));
            FontManager.MB14.drawString("Y:" + String.format("%.1f", yPos), xPos - 3 -
                    FontManager.MB14.getStringWidth("Y:" + String.format("%.1f", yPos)) - 1, yPos - 4 +
                    FontManager.MB14.getStringWidth("Y:" + String.format("%.1f", yPos)) + 4, new Color(255, 255, 255, 160));
        }
        FontManager.MB18.drawString(this.getName(), (float) (xPos + (27 * hoverAnimation.getOutput()) + 2), yPos - 11 - 3 + 3, new Color(255, 255, 255, 150).getRGB());
        if (!hoverAnimation.isDone() || hoverAnimation.finished(Direction.FORWARDS)) {
            RenderUtil.scaleStart((xPos + FontManager.MB18.getStringWidth("Move") / 2f), yPos + 3, hoverAnimation.getOutput().floatValue());
            FontManager.MB18.drawString("Move", (float) (xPos - 8 - 17 + (27 * hoverAnimation.getOutput()) + 2), yPos - 14 + 3,
                    new Color(255, 255, 255, 180).getRGB());
            RenderUtil.scaleEnd();
        }
    }

    public final void onClick(int mouseX, int mouseY, int button) {
        boolean canDrag = MouseUtil.isHovering(xPos, yPos, width, height, mouseX, mouseY);
        if (button == 0 && canDrag) {
            dragging = true;
            startX = (int) (mouseX - xPos);
            startY = (int) (mouseY - yPos);
        }
    }

    public void setWH(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public final void onRelease(int button) {
        if (button == 0) dragging = false;
    }

    public float getX() {
        return xPos;
    }

    public void setX(float x) {
        this.xPos = x;
    }

    public float getY() {
        return yPos;
    }

    public void setY(float y) {
        this.yPos = y;
    }
}