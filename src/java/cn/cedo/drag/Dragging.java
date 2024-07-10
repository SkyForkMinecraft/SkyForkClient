package cn.cedo.drag;

import cn.cedo.animations.Animation;
import cn.cedo.animations.Direction;
import cn.cedo.animations.impl.DecelerateAnimation;
import cn.cedo.misc.ColorUtil;
import cn.cedo.shader.RoundedUtil;
import cn.langya.utils.MouseUtil;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import org.union4dev.base.Access;

import java.awt.*;

@Getter
@Setter
public class Dragging implements Access.InstanceAccess {
    @Expose
    @SerializedName("x")
    private float xPos;
    @Expose
    @SerializedName("y")
    private float yPos;

    public float initialXVal;
    public float initialYVal;

    private float startX, startY;
    private boolean dragging;

    private float width, height;

    @Expose
    @SerializedName("name")
    private String name;

    @Getter
    private Class<?> moduleClazz;

    public Animation hoverAnimation = new DecelerateAnimation(250, 1, Direction.BACKWARDS);

    public Dragging(Class<?> clazz,String name, float initialXVal, float initialYVal) {
        this.moduleClazz = clazz;
        this.name = name;
        this.xPos = initialXVal;
        this.yPos = initialYVal;
        this.initialXVal = initialXVal;
        this.initialYVal = initialYVal;
    }

    private String longestModule;

    public final void onDraw(int mouseX, int mouseY) {
        boolean hovering = MouseUtil.isHovering(xPos, yPos, width, height, mouseX, mouseY);
        if (dragging) {
            xPos = mouseX - startX;
            yPos = mouseY - startY;
        }
        hoverAnimation.setDirection(hovering ? Direction.FORWARDS : Direction.BACKWARDS);
        if (!hoverAnimation.isDone() || hoverAnimation.finished(Direction.FORWARDS)) {
            RoundedUtil.drawRoundOutline(xPos - 4, yPos - 4, width + 8, height + 8, 10, 2,
                    ColorUtil.applyOpacity(Color.WHITE, 0), ColorUtil.applyOpacity(Color.WHITE, hoverAnimation.getOutput().floatValue()));
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



    public final void onRelease(int button) {
        if (button == 0) dragging = false;
    }

    public void setWH(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
