package cn.starx;

import cn.yapeteam.notification.NotificationType;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.module.Enable;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

// @ModuleInfo(name = "WaveyCapes", description = "Make your cape waving look better", chineseDescription = "使你的披风飘动更真实", category = Category.ADDONS)
public class WaveyCapes implements Access.InstanceAccess {
    public static int abc = 1;
    private final BooleanValue wind = new BooleanValue("Wind", true);
    private final ComboValue style = new ComboValue("Cape Style", "Smooth", "Blocky", "Smooth");
    private final ComboValue cm = new ComboValue("Cape Movement", "Vanilla", "Basic simulation", "Vanilla");
    private final NumberValue gravity = new NumberValue("Gravity",  25, 5, 32, 1);
    private final NumberValue hm = new NumberValue("Height Multiplier", 6, 4 , 16, 1);

    @Enable
    public void onEnable() {
        if (Access.getInstance().getModuleManager().isEnabled(MoBends.class)) {
            Access.getInstance().getNotificationManager().post("WaveyCapes and SkinLayers3D are not supported by MoBends.", NotificationType.FAILED);
            Access.getInstance().getModuleManager().setEnable(MoBends.class,false);
        }
    }

}
