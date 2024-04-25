package org.union4dev.base.module.render;

import net.minecraft.client.Minecraft;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.module.movement.Sprint;
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
    public NumberValue blurRadius = new NumberValue("blurRadius",3,0,20,1);
    public NumberValue spacing = new NumberValue("Spacing",3,1,5,1);


    /**
     * Subscribe a {@link Render2DEvent}
     *
     * @param event Event
     */
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        access.getFontManager().F18.drawStringWithShadow("SkyClient Fork", 4, 4, -1);
        access.getFontManager().F18.drawStringWithShadow(String.format("[FPS: %s]", Minecraft.getDebugFPS()),event.getScaledResolution().getScaledWidth() / 2.0,0,-1);

        double x = event.getScaledResolution().getScaledWidth() / 1.85;
        double y = event.getScaledResolution().getScaledHeight() * 0.8;
        access.getFontManager().F18.drawStringWithShadow(String.format("[Sprint: %s]", new Sprint().isEnabled(Sprint.class) ? "Toggled" : "Disable"),x,y,-1);

        if(array.getValue()){
            int width = event.getScaledResolution().getScaledWidth();
            int y1 = 4;
            ArrayList<Class<?>> enabledModules = new ArrayList<>();
            for (Class<?> m : access.getModuleManager().getModules()) {
                if (access.getModuleManager().isEnabled(m) && access.getModuleManager().isVisible(m)) {
                    enabledModules.add(m);
                }
            }
            enabledModules.sort((o1, o2) -> access.getFontManager().F18.getWidth(access.getModuleManager().format(o2)) - access.getFontManager().F18.getWidth(access.getModuleManager().format(o1)));
            for (Class<?> module : enabledModules) {
                int moduleWidth = access.getFontManager().F18.getWidth(access.getModuleManager().format(module));
                access.getFontManager().F18.drawStringWithShadow(access.getModuleManager().format(module), width - moduleWidth - 4, y1, -1);
                y1 += access.getFontManager().F18.getHeight() + spacing.getValue().intValue();
            }
        }
    }

}
