package com.skyfork.client.events.render;

import com.skyfork.client.events.base.Event;

public class Render3DEvent implements Event {

    private final float renderPartialTicks;

    public Render3DEvent(float renderPartialTicks) {
        this.renderPartialTicks = renderPartialTicks;
    }

    public float getRenderPartialTicks() {
        return renderPartialTicks;
    }
}
