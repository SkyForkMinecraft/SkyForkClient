package com.skyfork.api.langya.modules.client;

import net.minecraft.util.ResourceLocation;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.render.Render2DEvent;
import com.skyfork.client.value.impl.ComboValue;

/**
 * @author LangYa466
 * @since 2024/5/6 20:42
 */

public class Cape implements Access.InstanceAccess {
    public static final ComboValue capeMode = new ComboValue("披风模式", "MINECON", "MINECON", "MIGRATOR","None");

    public static ResourceLocation getCape() {
        if(capeMode.getValue() == null) return null;
        switch (capeMode.getValue()) {
            case "MINECON":
                return new ResourceLocation("client/capes/MINECON2016.png");
            case "MIGRATOR":
                return new ResourceLocation("client/capes/MIGRATOR.png");
            case "None" : return null;
        }
        return null;
    }

}

