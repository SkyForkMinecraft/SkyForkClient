package com.skyfork.client.events.render;

import net.minecraft.client.gui.ScaledResolution;
import com.skyfork.client.events.base.Event;

public class PreRenderEvent implements Event {

    private final ScaledResolution scaledResolution;
    private final float renderPartialTicks;

    public PreRenderEvent(ScaledResolution scaledResolution, float renderPartialTicks) {
        this.scaledResolution = scaledResolution;
        this.renderPartialTicks = renderPartialTicks;
    }

    public ScaledResolution getScaledResolution() {
        return scaledResolution;
    }

    public float getRenderPartialTicks() {
        return renderPartialTicks;
    }
}
