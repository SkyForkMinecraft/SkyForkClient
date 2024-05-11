package cn.langya.modules.render;

import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Disable;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.value.impl.NumberValue;

/**
 * @author LangYa466
 * @date 2024/4/25 20:31
 */

public class ComboInfo implements Access.InstanceAccess {

    public NumberValue x = new NumberValue("X",500,0,1020,10);
    public NumberValue y = new NumberValue("Y",500,0,1020,10);

    @EventTarget
    private void onRender2D(Render2DEvent event) {
        new cn.langya.elements.impls.ComboInfo().setState(true);
        setSuffix(String.valueOf(new cn.langya.elements.impls.ComboInfo().getCombo()),this);
        new cn.langya.elements.impls.ComboInfo().setX(x.getValue().intValue());
        new cn.langya.elements.impls.ComboInfo().setY(y.getValue().intValue());
    }

    @Enable
    public void onEnable() { new cn.langya.elements.impls.ComboInfo().setState(true); }

    @Disable
    public void onDisable() { new cn.langya.elements.impls.ComboInfo().setState(false); }

}