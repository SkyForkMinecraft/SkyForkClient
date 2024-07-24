package com.skyfork.api.yapeteam.ClickUI.component.impl.values;

import com.skyfork.api.cedo.render.RenderUtil;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.yapeteam.ClickUI.ClickUIScreen;
import com.skyfork.api.yapeteam.ClickUI.component.Component;
import org.lwjgl.input.Mouse;
import com.skyfork.client.value.impl.NumberValue;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberValueComponent implements Component {
    NumberValue value;
    boolean dragging = false;
    float x, y;

    public NumberValueComponent(NumberValue setting) {
        this.value = setting;
    }

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 30f;
        double diff = Math.min(width * (value.getMaximum().floatValue() - value.getMinimum()) / (value.getMaximum().floatValue() - value.getMinimum()), Math.max(0, mouseX - lx - 10));
        this.x = x;
        this.y = y;
        FontManager.M14.drawString(value.getName() + ":" + String.format("%.1f", value.getValue()), lx, y, new Color(0, 0, 0).getRGB());

        FontManager.M14.drawString("-", lx, y + 11, setAlpha(new Color(94, 94, 94)).getRGB());
        FontManager.M14.drawString("+", lx + ClickUIScreen.rightWidth - 12, y + 11, setAlpha(new Color(66, 66, 66)).getRGB());

        RoundedUtil.drawRound(lx + 10, y + 15, (width * (value.getMaximum().floatValue() - value.getMinimum().floatValue()) / (value.getMaximum().floatValue() - value.getMinimum().floatValue())), 3, 1, setAlpha(new Color(222, 222, 222)));
        RoundedUtil.drawRound(lx + 10, y + 15, (width * (value.getValue().floatValue() - value.getMinimum().floatValue()) / (value.getMaximum().floatValue() - value.getMinimum().floatValue())), 3, 1, setAlpha(new Color(87, 175, 255)));
        RenderUtil.circle(lx + 10 + (width * (value.getValue().floatValue() - value.getMinimum().floatValue()) / (value.getMaximum().floatValue() - value.getMinimum().floatValue())), y + 16.5, 3, setAlpha(new Color(87, 175, 255)).getRGB());
        if (dragging && Mouse.isButtonDown(0)) {
            if (diff == 0) {
                value.setValue((double) value.getMinimum().floatValue());
            } else {
                double newValue = roundToPlace(((diff / width) * (value.getMaximum().floatValue() - value.getMinimum().floatValue()) + value.getMinimum().floatValue()), 2);
                value.setValue(newValue);
            }
        } else
            dragging = false;
    }

    public boolean isHovered(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        float lx = ClickUIScreen.x + ClickUIScreen.width - (ClickUIScreen.currentModule != null ? ClickUIScreen.rightWidth : 0) + 5;
        float width = ClickUIScreen.rightWidth - 30f;
        if (isHovered(lx + 10 - 1.5f, y + 15f - 1.5f, width + 3, 3 + 3, mouseX, mouseY) && mouseButton == 0 && !this.dragging) {
            this.dragging = true;
        }
    }

    private double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {
        this.dragging = false;
    }
}
