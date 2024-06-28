package org.union4dev.base.module.render;

import cn.yapeteam.ClickUI.ClickUIScreen;
import cn.yapeteam.VapeClickGUi.VapeClickGui;
import org.lwjgl.input.Keyboard;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.module.Binding;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.gui.click.ClickGuiScreen;
import org.union4dev.base.value.impl.ComboValue;

@Binding(Keyboard.KEY_RSHIFT)
public class ClickGui implements Access.InstanceAccess {
    private ComboValue modeValue = new ComboValue("模式","普通","普通","测试","测试2");
    @Enable
    public void onEnable(){
        setEnable(this,false);
        if (modeValue.isMode("普通")) {
            access.setClickGui(new ClickGuiScreen());
        } else if (modeValue.isMode("测试")){
            access.setClickGui(new ClickUIScreen());
        } else {
            access.setClickGui(new VapeClickGui());
        }
        mc.displayGuiScreen(access.getClickGui());
    }
}
