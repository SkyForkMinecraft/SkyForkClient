package com.skyfork.api.yapeteam.ClickUI.component.impl.values;

import com.skyfork.api.cedo.render.RenderUtil;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.yapeteam.ClickUI.ClickUIScreen;
import com.skyfork.api.yapeteam.ClickUI.component.Component;
import net.minecraft.util.ResourceLocation;
import com.skyfork.client.value.impl.BooleanValue;

import java.awt.*;

public class BooleanValueComponent implements Component {
    BooleanValue value;
    float x, y;

    public BooleanValueComponent(BooleanValue setting) {
        this.value = setting;
    }

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        this.x = x;
        this.y = y;
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        RenderUtil.drawImage(new ResourceLocation("client/" + (value.getValue() ? "enabled" : "disabled") + ".png"), (int) lx, (int) y, 8, 8, ClickUIScreen.globalAlpha / 255f);
        FontManager.M14.drawString(value.getName(), lx + 12, y, setAlpha(new Color(0)).getRGB());
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        if (mouseButton == 0 && isHovered(lx, y, 8, 8, mouseX, mouseY)) {
            value.setValue(!value.getValue());
        }
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {
    }
}
