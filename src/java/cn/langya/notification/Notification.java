package cn.langya.notification;

import cn.cedo.shader.RoundedUtil;
import cn.langya.font.FontManager;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;

@Getter
@Setter
public class Notification {
    private String text;
    private long time;
    private boolean isEnd;
    private int x;


    public Notification(String text) {
        this.text = text;
    }

    public void draw() {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x,0,0);
        RoundedUtil.drawRound(0,0,100,40,2,new Color(0,0,0,80));
        FontManager.M16.drawStringWithShadow(text,2,2,-1);
        GlStateManager.popMatrix();
    }

}

