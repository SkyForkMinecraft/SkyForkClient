package org.union4dev.base.module.render;

import cn.langya.utils.BlurUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.item.ItemStack;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Startup;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.events.render.ShaderEvent;
import org.union4dev.base.gui.font.FontManager;
import org.union4dev.base.module.movement.Sprint;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;
import skid.cedo.shader.RoundedUtil;

import java.awt.*;
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
        FontManager.F18.drawStringWithShadow("SkyFork Client", 4, 4, -1);
        FontManager.F18.drawStringWithShadow(String.format("[FPS: %s]", Minecraft.getDebugFPS()),event.getScaledResolution().getScaledWidth() / 2.0,0,-1);

        if(array.getValue()){
            int width = event.getScaledResolution().getScaledWidth();
            int y1 = 4;
            ArrayList<Class<?>> enabledModules = new ArrayList<>();
            for (Class<?> m : access.getModuleManager().getModules()) {
                if (access.getModuleManager().isEnabled(m) && access.getModuleManager().isVisible(m)) {
                    enabledModules.add(m);
                }
            }
            enabledModules.sort((o1, o2) -> FontManager.F18.getWidth(access.getModuleManager().format(o2)) - FontManager.F18.getWidth(access.getModuleManager().format(o1)));
            for (Class<?> module : enabledModules) {
                int moduleWidth = FontManager.F18.getWidth(access.getModuleManager().format(module));
                FontManager.F18.drawStringWithShadow(access.getModuleManager().format(module), width - moduleWidth - 4, y1, -1);
                y1 += FontManager.F18.getHeight() + spacing.getValue().intValue();
            }
        }

    }

    @EventTarget
    void onS(ShaderEvent e) {
        RoundedUtil.drawRound(100,100,100,100,10,new Color(0,0,0,50));
    }

}
