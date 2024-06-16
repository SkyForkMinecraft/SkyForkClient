package cn.langya;

import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.network.PacketSendEvent;

/**
 * @author LangYa
 * @since 2024/06/16/下午9:09
 */
public class PacketManager implements Access.InstanceAccess {

    @EventTarget
    public void onPs(PacketSendEvent event) {
        Packet packet = event.getPacket();
        if (!mc.isIntegratedServerRunning()) {
            if (packet instanceof C17PacketCustomPayload) {
                if (((C17PacketCustomPayload) packet).getChannelName().equalsIgnoreCase("MC|Brand")) {
                    mc.getNetHandler().addToSendQueue(
                            new C17PacketCustomPayload("MC|Brand",
                                    new PacketBuffer(Unpooled.buffer()).writeString("SkyForkMinecraft")));
                }
            }
        }
    }
}
