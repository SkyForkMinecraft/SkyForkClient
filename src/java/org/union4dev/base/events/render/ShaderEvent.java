package org.union4dev.base.events.render;

import cn.langya.event.ShaderType;
import lombok.Getter;
import org.union4dev.base.events.base.Event;

/**
 * @author LangYa466
 * @since 2024/5/10 21:39
 */

@Getter
public class ShaderEvent implements Event {
    private final ShaderType type;
    public ShaderEvent(ShaderType type) {
        this.type = type;
    }

}
