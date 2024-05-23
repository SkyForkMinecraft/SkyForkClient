package net.erouax.combodisplay;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.misc.AttackEvent;
import org.union4dev.base.events.network.PacketReceiveEvent;
import org.union4dev.base.events.render.Render2DEvent;
import org.union4dev.base.events.update.TickEvent;

public class Combo {
    private final Minecraft mc = Minecraft.getMinecraft();
    private long sentAttackTime;
    private int lastAttackId;
    private int currentCombo;
    private int sentAttack = -1;
    private long lastHitTime;

    @EventTarget
    private void onA(AttackEvent event) {
        this.sentAttack = event.getTarget().getEntityId();
        this.sentAttackTime = System.currentTimeMillis();
    }

    @EventTarget
    private void onT(TickEvent e) {
        if (System.currentTimeMillis() - this.lastHitTime > 2000L) {
            this.currentCombo = 0;
        }
    }


    @EventTarget
    private void onPr(PacketReceiveEvent e) {
        if (!(e.getPacket() instanceof S19PacketEntityStatus)) return;
        S19PacketEntityStatus packet = (S19PacketEntityStatus) e.getPacket();
        if (packet.entityId != 2) {
            return;
        }
        Entity target = packet.getEntity(this.mc.theWorld);
        if (target == null) {
            return;
        }
        if (this.sentAttack != -1 && target.getEntityId() == this.sentAttack) {
            this.sentAttack = -1;
            if (System.currentTimeMillis() - this.sentAttackTime > 2000L) {
                this.sentAttackTime = 0L;
                this.currentCombo = 0;
                return;
            }
            this.currentCombo = this.lastAttackId == target.getEntityId() ? ++this.currentCombo : 1;
            this.lastHitTime = System.currentTimeMillis();
            this.lastAttackId = target.getEntityId();
        } else if (target.getEntityId() == this.mc.thePlayer.getEntityId()) {
            this.currentCombo = 0;
        }
    }

}
