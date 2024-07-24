// Decompiled with: CFR 0.152
// Class Version: 8
package com.skyfork.api.dxg;

import java.text.DecimalFormat;

import com.skyfork.api.imflowow.LoadWorldEvent;
import com.skyfork.api.yapeteam.notification.NotificationType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import com.skyfork.client.Access;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.network.PacketReceiveEvent;
import com.skyfork.client.value.impl.BooleanValue;

public class CheaterDetector implements Access.InstanceAccess {
    public BooleanValue reachValue = new BooleanValue("长臂猿检测", true);
    public BooleanValue noslowAValue = new BooleanValue("无减速检测", true);

    public static final DecimalFormat DF_1 = new DecimalFormat("0.000000");
    int vl;

    @EventTarget
    public void onWorld(LoadWorldEvent event) {
        vl = 0;
    }

    @EventTarget
    public void onPacket(PacketReceiveEvent event) {
        if (mc.thePlayer.ticksExisted % 6 == 0) {
            S19PacketEntityStatus s19;
            if (event.getPacket() instanceof S19PacketEntityStatus && reachValue.getValue() && (s19 = (S19PacketEntityStatus) event.getPacket()).getOpCode() == 2) {
                new Thread(() -> checkCombatHurt(s19.getEntity(mc.theWorld))).start();
            }
            if (event.getPacket() instanceof S14PacketEntity && noslowAValue.getValue()) {
                S14PacketEntity packet = (S14PacketEntity) event.getPacket();
                Entity entity = packet.getEntity(mc.theWorld);
                if (!(entity instanceof EntityPlayer)) {
                    return;
                }
                new Thread(() -> checkPlayer((EntityPlayer) entity)).start();
            }
        }
    }

    private void checkCombatHurt(Entity entity) {
        if (!(entity instanceof EntityLivingBase)) {
            return;
        }
        Entity attacker = null;
        int attackerCount = 0;
        for (Entity worldEntity : mc.theWorld.getLoadedEntityList()) {
            if (worldEntity == mc.thePlayer) return;
            if (!(worldEntity instanceof EntityPlayer) || worldEntity.getDistanceToEntity(entity) > 7.0f || ((Object) worldEntity).equals(entity))
                continue;
            ++attackerCount;
            attacker = worldEntity;
        }
        if (attacker == null || attacker.equals(entity)) {
            return;
        }
        double reach = attacker.getDistanceToEntity(entity);
        String prefix = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.AQUA + "黑客检测" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + attacker.getName() + EnumChatFormatting.WHITE + " failed ";
        if (reach > 3.0) {
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText((prefix + EnumChatFormatting.AQUA + "长臂猿" + EnumChatFormatting.WHITE + " (vl:" + attackerCount + ".0)" + EnumChatFormatting.GRAY + ": " + DF_1.format(reach) + " blocks")));
            Access.getInstance().getNotificationManager().post(String.format("%s 是长臂猿外挂!!",entity.getDisplayName().getFormattedText()), NotificationType.WARNING);
        }
    }

    private void checkPlayer(EntityPlayer player) {
        if (player.equals(mc.thePlayer)) {
            return;
        }
        String prefix = EnumChatFormatting.GRAY + "[" + EnumChatFormatting.AQUA + "黑客检测" + EnumChatFormatting.GRAY + "] " + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + player.getName() + EnumChatFormatting.WHITE + " failed ";
        if (player.isUsingItem() && (player.posX - player.lastTickPosX > 0.2 || player.posZ - player.lastTickPosZ > 0.2) && !player.isPotionActive(Potion.moveSpeed)) {
            mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText((prefix + EnumChatFormatting.AQUA + "无减速 (Prediction)" + EnumChatFormatting.WHITE + " (vl:" + vl + ".0)")));
            Access.getInstance().getNotificationManager().post(String.format("%s 是无减速外挂!!",player.getDisplayName().getFormattedText()), NotificationType.WARNING);
            ++vl;
        }
        if (!mc.theWorld.loadedEntityList.contains(player) || !player.isEntityAlive()) {
            vl = 0;
        }
    }
}