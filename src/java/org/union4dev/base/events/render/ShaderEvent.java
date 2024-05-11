package org.union4dev.base.events.render;

import lombok.Getter;
import org.union4dev.base.events.base.Event;

/**
 * @author LangYa466
 * @date 2024/5/10 21:39
 */

@Getter
public class ShaderEvent implements Event {
    private final boolean bloom;

    public ShaderEvent(boolean bloom) {
        this.bloom = bloom;
    }

}
