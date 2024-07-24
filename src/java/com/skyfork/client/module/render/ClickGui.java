package com.skyfork.client.module.render;

import com.skyfork.api.dxg.NormalClickGUI;
import com.skyfork.api.yapeteam.ClickUI.ClickUIScreen;
import com.skyfork.api.yapeteam.VapeClickGUi.VapeClickGui;
import org.lwjgl.input.Keyboard;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.module.Binding;
import com.skyfork.client.annotations.module.Enable;
import com.skyfork.client.gui.click.ClickGuiScreen;
import com.skyfork.client.value.impl.ComboValue;

@Binding(Keyboard.KEY_RSHIFT)
public class ClickGui implements Access.InstanceAccess {
    private ComboValue modeValue = new ComboValue("模式","普通","普通","测试","测试2","测试3");
    @Enable
    public void onEnable(){
        setEnable(this,false);
        if (modeValue.isMode("普通")) {
            access.setClickGui(new ClickGuiScreen());
        } else if (modeValue.isMode("测试")){
            access.setClickGui(new ClickUIScreen());
        } else if (modeValue.isMode("测试2")){
            access.setClickGui(new VapeClickGui());
        } else if (modeValue.isMode("测试3")){
            access.setClickGui(new NormalClickGUI());
        }
        mc.displayGuiScreen(access.getClickGui());
    }
}
