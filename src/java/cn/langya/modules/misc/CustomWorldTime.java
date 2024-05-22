package cn.langya.modules.misc;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.network.PacketReceiveEvent;
import org.union4dev.base.events.network.PacketSendEvent;
import org.union4dev.base.events.update.TickEvent;
import org.union4dev.base.value.impl.ComboValue;
import org.union4dev.base.value.impl.NumberValue;

public class CustomWorldTime implements Access.InstanceAccess {
    private final ComboValue timeMode = new ComboValue("时间模式", "早上", "早上", "晚上", "自定义");
    private final NumberValue customTime = new NumberValue("自定义时间", 18000, 0, 24000, 1000);

    @EventTarget
    private void onPs(PacketSendEvent e) {
        if (e.getPacket() instanceof S03PacketTimeUpdate) e.setCancelled(true);
    }

    @EventTarget
    private void onPr(PacketReceiveEvent e) {
        if (e.getPacket() instanceof S03PacketTimeUpdate) e.setCancelled(true);
    }

    @EventTarget
    private void onT(TickEvent event) {
        long time = 0;
        if (mc.theWorld != null) {
            switch (timeMode.getValue()) {
                case "早上":
                    time = 1000L;
                    break;
                case "晚上":
                    time = 13000L;
                    break;
                case "自定义":
                    time = customTime.getValue().longValue();
                    break;
            }
            mc.theWorld.setWorldTime(time);
        }
    }
}
