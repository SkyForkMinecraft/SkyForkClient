package cn.langya.elements.impls;

import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;
import cn.cedo.misc.ColorUtil;

import java.awt.*;

/**
 * @author LangYa466
 * @date 2024/4/11 19:05
 */

public class ClientLogo extends Element {
    public ComboValue textMode = new ComboValue("显示文本", "SkyFork", "天空分支", "SkyFork");
    private static final ComboValue colorMode = new ComboValue("颜色", "客户端", "客户端", "自定义", "彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色", 0, 0, 255, 5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色", 0, 0, 255, 5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色", 0, 0, 255, 5);

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
        String text = textMode.getValue();
        Color c = Access.CLIENT_COLOR;
        switch (colorMode.getValue()) {
            case "客户端":
                c = Access.CLIENT_COLOR;
                break;
            case "自定义":
                c = new Color(customColorRed.getValue().intValue(), customColorGreen.getValue().intValue(), customColorBlue.getValue().intValue());
                break;
            case "彩虹":
                c = new Color(ColorUtil.getColor(-(1 + 5 * 1.7f), 0.7f, 1));
        }
        FontManager.M50.drawStringWithShadow(text, getX(), getY(), c.getRGB());
        this.setWidth(FontManager.M50.getStringWidth(text) + 6);
        this.setHeight(FontManager.M50.getHeight());
    }

}