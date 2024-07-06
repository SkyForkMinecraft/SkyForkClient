package cn.langya.elements.impls;

import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import net.minecraft.client.Minecraft;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;
import cn.cedo.misc.ColorUtil;

import java.awt.*;

public class FPSInfo extends Element {

    public ComboValue textMode = new ComboValue("显示文本", "FPS", "帧率", "FPS");
    private static final ComboValue colorMode = new ComboValue("颜色", "客户端", "客户端", "自定义", "彩虹");
    private static final NumberValue customColorRed = new NumberValue("自定义红色", 0, 0, 255, 5);
    private static final NumberValue customColorGreen = new NumberValue("自定义绿色", 0, 0, 255, 5);
    private static final NumberValue customColorBlue = new NumberValue("自定义蓝色", 0, 0, 255, 5);
    private final BooleanValue colorText = new BooleanValue("彩虹色文字",true);

    public FPSInfo() {
        super(20, 20);
    }

    @Override
    public void draw() {
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }

        String logoText = textMode.getValue();
        String text = String.format("[%s: %s]",logoText, Minecraft.getDebugFPS());
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
        if (colorText.getValue()) {
            FontManager.M18.drawGradientStringWithShadow(text, x,y);
        } else {
            FontManager.M18.drawStringWithShadow(text, x,y, c.getRGB());
        }
        setWidth(FontManager.M18.getStringWidth(text));
        setHeight(FontManager.M18.getHeight());
    }
}
