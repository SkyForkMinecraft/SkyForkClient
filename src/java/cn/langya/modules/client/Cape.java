package cn.langya.modules.client;

import net.minecraft.util.ResourceLocation;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.value.impl.ComboValue;

/**
 * @author LangYa466
 * @date 2024/5/6 20:42
 */

public class Cape implements Access.InstanceAccess {
    public static final ComboValue capeMode = new ComboValue("Cape Mode", "MINECON2016", "MINECON", "MIGRATOR","None");

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

