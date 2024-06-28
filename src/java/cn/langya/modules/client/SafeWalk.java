package cn.langya.modules.client;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.annotations.module.Disable;
import org.union4dev.base.events.update.UpdateEvent;

/**
 * @author LangYa
 * @since 2024/06/19/下午8:47
 */
public class SafeWalk implements Access.InstanceAccess {

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        mc.gameSettings.keyBindSneak.pressed = mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() == Blocks.air;
    }

    @Disable
    public void onDisable() {
        if (mc.gameSettings.keyBindSneak.pressed) {
            mc.gameSettings.keyBindSneak.pressed = false;
        }
    }

}
