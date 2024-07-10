package cn.langya.elements.impls;

import cn.cedo.drag.Dragging;
import cn.langya.font.FontManager;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.module.render.HUD;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

import java.awt.*;

public class ArrayList implements Access.InstanceAccess {

    private static final ComboValue colorMode = new ComboValue("颜色", "客户端", "客户端", "自定义", "彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色", 0, 0, 255, 5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色", 0, 0, 255, 5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色", 0, 0, 255, 5);
    private final NumberValue spacing = new NumberValue("间距", 3, 1, 5, 1);

    private final Dragging pos = Access.getInstance().getDragManager().createDrag(this.getClass(),"arraylist", 150, 150);

    @EventTarget
    public void onRender2D(Render2DEvent event) {
        int y1 = 4;
        pos.setHeight(50);
        pos.setWidth(50);
        float x = pos.getXPos();
        float y = pos.getYPos();
        java.util.ArrayList<Class<?>> enabledModules = new java.util.ArrayList<>();
        for (Class<?> m : access.getModuleManager().getModules()) {
            if (access.getModuleManager().isEnabled(m) && access.getModuleManager().isVisible(m)) {
                enabledModules.add(m);
            }
        }
        enabledModules.sort((o1, o2) -> FontManager.M18.getStringWidth(access.getModuleManager().format(o2)) - FontManager.M18.getStringWidth(access.getModuleManager().format(o1)));
        int count = 0;
        for (Class<?> module : enabledModules) {
            Color c = Access.CLIENT_COLOR;
            switch (colorMode.getValue()) {
                case "客户端":
                    c = Access.CLIENT_COLOR;
                    break;
                case "自定义":
                    c = new Color(customColorRed.getValue().intValue(), customColorGreen.getValue().intValue(), customColorBlue.getValue().intValue());
                    break;
                case "彩虹":
                    c = HUD.color(count);
            }
            FontManager.M18.drawRightAlignedStringWithShadow(access.getModuleManager().format(module), x , y + y1, c.getRGB());
            y1 += FontManager.M18.getHeight() + spacing.getValue().intValue();
            ++count;
        }
    }


}
