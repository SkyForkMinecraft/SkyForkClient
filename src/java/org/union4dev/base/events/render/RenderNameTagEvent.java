package org.union4dev.base.events.render;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.Entity;
import org.union4dev.base.events.base.Event;

@Getter
@Setter
public class RenderNameTagEvent extends Event.EventCancellable {

    private Entity target;
    private String string;

    public RenderNameTagEvent(String str,Entity entity) {
        this.string = str;
        this.target = entity;
    }
}
