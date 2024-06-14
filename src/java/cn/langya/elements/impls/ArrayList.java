package cn.langya.elements.impls;

import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;
import cn.cedo.misc.ColorUtil;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;

public class ArrayList extends Element {
    public ArrayList() {
        super(100, 100);
    }

    private static final ComboValue colorMode = new ComboValue("颜色", "客户端", "客户端", "自定义", "彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色", 0, 0, 255, 5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色", 0, 0, 255, 5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色", 0, 0, 255, 5);
    private final NumberValue spacing = new NumberValue("间距", 3, 1, 5, 1);

    @Override
    public void draw() {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        int y1 = 4;
        setHeight(50);
        setWidth(50);
        java.util.ArrayList<Class<?>> enabledModules = new java.util.ArrayList<>();
        for (Class<?> m : access.getModuleManager().getModules()) {
            if (access.getModuleManager().isEnabled(m) && access.getModuleManager().isVisible(m)) {
                enabledModules.add(m);
            }
        }
        enabledModules.sort((o1, o2) -> FontManager.M18.getStringWidth(access.getModuleManager().format(o2)) - FontManager.M18.getStringWidth(access.getModuleManager().format(o1)));

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
                    c = ColorUtil.rainbow();
            }
            FontManager.M18.drawRightAlignedStringWithShadow(access.getModuleManager().format(module), x , y + y1, c.getRGB());
            y1 += FontManager.M18.getHeight() + spacing.getValue().intValue();
        }
    }


}
