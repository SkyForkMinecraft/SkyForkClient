package com.skyfork.api.cedo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.skyfork.client.events.base.Cancellable;
import com.skyfork.client.events.base.Event;

@Getter
@Setter
@AllArgsConstructor
public class PlayerMoveUpdateEvent implements Cancellable,Event {

    private float strafe, forward, friction, yaw, pitch;
    private boolean cancel;

    public PlayerMoveUpdateEvent(float strafe, float forward, float friction, float yaw, float pitch) {
        this.strafe = strafe;
        this.forward = forward;
        this.friction = friction;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean state) {
        this.cancel = state;
    }
}