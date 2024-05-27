package org.union4dev.base.events.movement;

import lombok.Setter;
import org.union4dev.base.events.base.Event;

public class JumpEvent extends Event.EventCancellable {
    private double jumpMotion;
    @Setter
    private float yaw;

    public JumpEvent(double jumpMotion, float yaw) {
        this.jumpMotion = jumpMotion;
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public double getJumpMotion() {
        return jumpMotion;
    }

    public void setJumpMotion(float jumpMotion) {
        this.jumpMotion = jumpMotion;
    }
}