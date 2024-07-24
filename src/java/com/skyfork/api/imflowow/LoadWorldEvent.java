package com.skyfork.api.imflowow;

import lombok.Getter;
import net.minecraft.client.multiplayer.WorldClient;
import com.skyfork.client.events.base.Event;

@Getter
public class LoadWorldEvent implements Event {
    private final WorldClient worldClient;

    public LoadWorldEvent(WorldClient worldClient) {
        this.worldClient = worldClient;
    }

}