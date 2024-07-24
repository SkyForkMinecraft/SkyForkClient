package com.skyfork.api.yapeteam.ClickUI.component.impl.slots;

import com.skyfork.api.cedo.shader.GradientUtil;
import com.skyfork.api.cedo.shader.RoundedUtil;
import com.skyfork.api.langya.font.FontDrawer;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.api.yapeteam.ClickUI.ClickUIScreen;
import com.skyfork.api.yapeteam.ClickUI.component.Component;
import com.skyfork.api.yapeteam.cloudmusic.MusicManager;
import com.skyfork.api.yapeteam.cloudmusic.impl.Track;
import lombok.Setter;
import com.skyfork.client.module.render.HUD;

import java.awt.*;

public class TrackSlotComponent implements Component {
    private final Track track;
    private float x;
    private float y;
    @Setter
    private float width;
    @Setter
    private float height;

    public TrackSlotComponent(Track track) {
        this.track = track;
    }

    private Color setAlpha(Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), ClickUIScreen.globalAlpha);
    }

    @Override
    public void draw(float x, float y, float mouseX, float mouseY) {
        if (y + height < ClickUIScreen.y || y > ClickUIScreen.y + ClickUIScreen.height)
            return;
        this.x = x;
        this.y = y;
        FontDrawer font = FontManager.M18;
        RoundedUtil.drawRound(x, y, width, height, 4, ClickUIScreen.boxColor);
        GradientUtil.applyGradientHorizontal((int) (x + 2), (int) (y + 1), font.getStringWidth(track.name), font.getHeight(), ClickUIScreen.globalAlpha / 255f, HUD.getClientColors().getFirst(), HUD.getClientColors().getSecond(), () ->
                font.drawString(track.name, (int) (x + 2), (int) (y + 1), setAlpha(Color.GRAY).getRGB())
        );
        font.drawString(track.artists, (int) (x + 2), (int) (y + 10), setAlpha(Color.GRAY).getRGB());
        RoundedUtil.drawRound(x + width - 15, y, 14, height, 4, isHoveredIn(x + width - 15, y, 15, height, mouseX, mouseY) ? new Color(220, 220, 220, ClickUIScreen.globalAlpha) : ClickUIScreen.boxColor);
        FontManager.icon18.drawString("J", (int) (x + width - 11.5f), (int) (y + 8), Color.GRAY.getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHoveredIn(ClickUIScreen.x + ClickUIScreen.leftWidth + 10,
                ClickUIScreen.y + ClickUIScreen.topHeight + 22,
                ClickUIScreen.width - ClickUIScreen.leftWidth - 15,
                ClickUIScreen.height - 50 - 20 - 25, mouseX, mouseY) &&
                isHoveredIn(x + width - 13, y + 5, 15, 15, mouseX, mouseY) &&
                mouseButton == 0) {
            try {
                MusicManager.INSTANCE.play(track);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void mouseReleased(float mouseX, float mouseY, int state) {
    }

    public boolean isHoveredIn(float x, float y, float width, float height, float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
