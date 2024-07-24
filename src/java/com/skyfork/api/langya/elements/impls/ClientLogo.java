package com.skyfork.api.langya.elements.impls;

import com.skyfork.api.cedo.drag.Dragging;
import com.skyfork.api.langya.font.FontManager;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.value.impl.ComboValue;

/**
 * @author LangYa466
 * @since 2024/4/11 19:05
 */

public class ClientLogo {
    public ComboValue textMode = new ComboValue("显示文本", "SkyFork", "天空分支", "SkyFork");
    private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(),"clientlogo", 150, 150);


    @EventTarget
    public void draw(Render2DEvent event) {

        float x = pos.getXPos();
        float y = pos.getYPos();
        float xVal = x;
        float yVal = y;
        float versionX = xVal + FontManager.M50.getStringWidth(textMode.getValue());
        float versionWidth = FontManager.M16.getStringWidth(Access.CLIENT_VERSION);

        FontManager.M50.drawGradientStringWithShadow(textMode.getValue(), xVal, yVal);


        FontManager.M16.drawStringWithShadow(Access.CLIENT_VERSION, versionX, yVal, -1);
        pos.setWidth(FontManager.M50.getStringWidth(textMode.getValue()) + versionWidth);
        pos.setHeight(FontManager.M50.getHeight());
    }

}