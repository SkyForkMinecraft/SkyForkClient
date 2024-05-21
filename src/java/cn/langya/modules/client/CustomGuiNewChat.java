package cn.langya.modules.client;

import cn.langya.font.FontDrawer;
import cn.langya.font.FontManager;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;

public class CustomGuiNewChat {
    public static final BooleanValue rect = new BooleanValue("聊天框背景",false);
    public static final BooleanValue font = new BooleanValue("聊天框文字优化",true);
    private static final ComboValue fontScale = new ComboValue("聊天框文字大小","14","14","16","18","20");

    public static FontDrawer getCustomFont() {
       switch (fontScale.getValue()) {
           case "14": return FontManager.M14;
           case "16": return FontManager.M16;
           case "18": return FontManager.M18;
           case "20": return FontManager.M20;
       }
        return FontManager.M14;
    }
}
