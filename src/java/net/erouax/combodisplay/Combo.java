package net.erouax.combodisplay;

import cn.cedo.shader.RoundedUtil;
import cn.langya.elements.Element;
import cn.langya.font.FontDrawer;
import cn.langya.font.FontManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import org.union4dev.base.Access;
import org.union4dev.base.annotations.event.EventTarget;
import org.union4dev.base.events.misc.AttackEvent;
import org.union4dev.base.events.network.PacketReceiveEvent;
import org.union4dev.base.events.update.TickEvent;
import org.union4dev.base.value.impl.BooleanValue;
import org.union4dev.base.value.impl.NumberValue;

import java.awt.*;

public class Combo extends Element implements Access.InstanceAccess {
    private long sentAttackTime;
    private int lastAttackId;
    private int currentCombo;
    private int sentAttack = -1;
    private long lastHitTime;

    private final BooleanValue backgroundValue = new BooleanValue("背景",true);
    private final NumberValue backgroundRadiusValue = new NumberValue("背景自圆角值", 2,0,10,1);
    private final BooleanValue colorText = new BooleanValue("彩虹色文字",true);
    private final BooleanValue blur = new BooleanValue("模糊背景",true);

    public Combo() {
        super(4, 4);
    }

    @Override
    public void draw() {
        String text = String.format("Combo: %s",currentCombo);
        if (!Access.getInstance().getModuleManager().isEnabled(this.getClass())) {
            setWidth(0);
            setHeight(0);
            return;
        }
        FontDrawer fontRenderer = FontManager.M18;
        if (backgroundValue.getValue() && !blur.getValue()) RoundedUtil.drawRound(x,y,fontRenderer.getStringWidth(text) + 1.5F,fontRenderer.getHeight(),backgroundRadiusValue.getValue().intValue(),new Color(0,0,0,80));
        setWH(fontRenderer.getStringWidth(text),fontRenderer.getHeight());
        fontRenderer.drawStringWithShadow(text, x, y + 0.5,-1);
        if (colorText.getValue()) {
            fontRenderer.drawGradientStringWithShadow("Combo", x, y + 0.5);
        }
    }

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
