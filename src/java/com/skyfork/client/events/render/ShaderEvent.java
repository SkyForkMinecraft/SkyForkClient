package com.skyfork.client.events.render;

import com.skyfork.api.langya.event.ShaderType;
import lombok.Getter;
import com.skyfork.client.events.base.Event;

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
