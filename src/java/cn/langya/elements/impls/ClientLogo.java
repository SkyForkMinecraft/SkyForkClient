package cn.langya.elements.impls;

import cn.cedo.render.RenderUtil;
import cn.cedo.shader.GradientUtil;
import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import org.union4dev.base.Access;
import org.union4dev.base.module.render.HUD;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;
import cn.cedo.misc.ColorUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @since 2024/4/11 19:05
 */

public class ClientLogo extends Element {
    public ComboValue textMode = new ComboValue("显示文本", "SkyFork", "天空分支", "SkyFork");
    public ClientLogo() {
        super(50, 50);
    }

    @Override
    public void draw() {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }


        float xVal = x;
        float yVal = y;
        float versionX = xVal + FontManager.M50.getStringWidth(textMode.getValue());
        float versionWidth = FontManager.M16.getStringWidth(Access.CLIENT_VERSION);

        FontManager.M50.drawGradientStringWithShadow(textMode.getValue(), xVal, yVal);


        FontManager.M16.drawStringWithShadow(Access.CLIENT_VERSION, versionX, yVal, -1);
        this.setWidth(FontManager.M50.getStringWidth(textMode.getValue()) + versionWidth);
        this.setHeight(FontManager.M50.getHeight());
    }

}