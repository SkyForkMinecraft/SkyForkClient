package com.skyfork.client.events.misc;

import net.minecraft.util.MovingObjectPosition;
import com.skyfork.client.events.base.Event;

public class MiddleClickEvent implements Event {
    private final MovingObjectPosition objectPosition;

    public MiddleClickEvent(MovingObjectPosition mouseOver) {
        this.objectPosition = mouseOver;
    }

    public MovingObjectPosition getObjectPosition() {
        return objectPosition;
    }
}
