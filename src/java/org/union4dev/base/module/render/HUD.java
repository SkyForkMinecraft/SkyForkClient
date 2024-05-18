package org.union4dev.base.module.render;

import net.minecraft.client.Minecraft;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.render.Render2DEvent;
import cn.langya.font.FontManager;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;

import java.util.ArrayList;

@Startup // enable when client startup
public class HUD implements Access.InstanceAccess {

    /**
     * An Example Bool value
     */
    public BooleanValue array = new BooleanValue("Array", true);
    public BooleanValue blur = new BooleanValue("Blur", false);
    public NumberValue spacing = new NumberValue("Spacing",3,1,5,1);

    /**
     * Subscribe a {@link Render2DEvent}
     *
     * @param event Event
     */
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        FontManager.M50.drawStringWithShadow("天空分支", 4, 4, -1);
        FontManager.M18.drawStringWithShadow(String.format("[FPS: %s]", Minecraft.getDebugFPS()), (float) (event.getScaledResolution().getScaledWidth() / 2.0),0,-1);

        if(array.getValue()){
            int width = event.getScaledResolution().getScaledWidth();
            int y1 = 4;
            ArrayList<Class<?>> enabledModules = new ArrayList<>();
            for (Class<?> m : access.getModuleManager().getModules()) {
                if (access.getModuleManager().isEnabled(m) && access.getModuleManager().isVisible(m)) {
                    enabledModules.add(m);
                }
            }
            enabledModules.sort((o1, o2) -> FontManager.M18.getStringWidth(access.getModuleManager().format(o2)) - FontManager.M18.getStringWidth(access.getModuleManager().format(o1)));
            for (Class<?> module : enabledModules) {
                int moduleWidth = FontManager.M18.getStringWidth(access.getModuleManager().format(module));
                FontManager.M18.drawStringWithShadow(access.getModuleManager().format(module), width - moduleWidth - 4, y1, -1);
                y1 += FontManager.M18.getHeight() + spacing.getValue().intValue();
            }
        }

    }

}
