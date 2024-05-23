package cn.langya.elements.impls;

import cn.langya.elements.Element;
import cn.langya.font.FontManager;
import net.minecraft.client.Minecraft;
import org.union4dev.base.Access;
import org.union4dev.base.value.impl.ComboValue;

public class FPSInfo extends Element {

    public ComboValue textMode = new ComboValue("显示文本", "英文", "中文", "英文");
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
        String logoText = textMode.getValue().equals("中文") ? "帧率" : "FPS";
        String text = String.format("[%s: %s]",logoText, Minecraft.getDebugFPS());
        FontManager.M18.drawStringWithShadow(text, x,y, -1);
        setWidth(FontManager.M18.getStringWidth(text));
        setHeight(FontManager.M18.getHeight());
    }
}
