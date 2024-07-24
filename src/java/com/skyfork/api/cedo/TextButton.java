package com.skyfork.api.cedo;

import com.skyfork.api.cedo.animations.Animation;
import com.skyfork.api.cedo.animations.Direction;
import com.skyfork.api.cedo.animations.impl.DecelerateAnimation;
import com.skyfork.api.dxg.Screen;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.langya.utils.MouseUtil;
import lombok.Getter;

import java.awt.*;

public class TextButton implements Screen {
        public float x, y;
        @Getter
        public final float width, height;
        public Runnable clickAction;
        public final String text;

        public final Animation hoverAnimation = new DecelerateAnimation(150, 1);

        public boolean addToEnd;

        public TextButton(String text) {
            this.text = text;
            width = FontManager.M16.getStringWidth(text);
            height = FontManager.M16.getHeight();
        }

        @Override
        public void initGui() {

        }

        @Override
        public void keyTyped(char typedChar, int keyCode) {

        }

        @Override
        public void drawScreen(int mouseX, int mouseY) {
            boolean hovered = MouseUtil.isHovering(x, y, width, height, mouseX, mouseY);
            hoverAnimation.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
            FontManager.M16.drawString(text, x, y - (height / 2f * hoverAnimation.getOutput().floatValue()), Color.WHITE.getRGB());
            if (addToEnd) {
                FontManager.M16.drawString(" | ", x + width, y, Color.WHITE.getRGB());
            }
        }

        @Override
        public void mouseClicked(int mouseX, int mouseY, int button) {
            boolean hovered = MouseUtil.isHovering(x, y, width, height, mouseX, mouseY);
            if (hovered && button == 0) {
                clickAction.run();
            }
        }

        @Override
        public void mouseReleased(int mouseX, int mouseY, int state) {

        }
    }