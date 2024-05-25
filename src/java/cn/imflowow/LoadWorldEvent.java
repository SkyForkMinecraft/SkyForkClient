package cn.imflowow;

import lombok.Getter;
import net.minecraft.client.multiplayer.WorldClient;
import org.union4dev.base.events.base.Event;

@Getter
public class LoadWorldEvent implements Event {
    private final WorldClient worldClient;

    public LoadWorldEvent(WorldClient worldClient) {
        this.worldClient = worldClient;
    }

}