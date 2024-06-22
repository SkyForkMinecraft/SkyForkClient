package cn.superskidder.modules;

import net.minecraft.crash.CrashReportCategory;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

public class OldAnimation {
    public static final ComboValue mode = new ComboValue("模式", "Stella",
            "Stella", "Middle", "1.7", "Exhi", "Exhi 2", "Exhi 3", "Exhi 4", "Exhi 5", "Shred", "Smooth", "Sigma");

    public static BooleanValue oldRod = new BooleanValue("鱼竿动画", false);
    public static BooleanValue oldBow = new BooleanValue("弓箭动画", false);
    public static BooleanValue oldSwing = new BooleanValue("挥手动画", false);

    public static NumberValue swingSpeed = new NumberValue("挥手速度",1,15,1,1);
}
