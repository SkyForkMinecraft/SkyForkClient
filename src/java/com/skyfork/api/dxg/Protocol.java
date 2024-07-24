package com.skyfork.api.dxg;

import com.skyfork.api.dxg.hytprotocol.GermGui;
import net.minecraft.network.play.server.*;
import com.skyfork.client.Access;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import io.netty.buffer.*;
import com.skyfork.client.annotations.event.EventTarget;
import com.skyfork.client.events.network.PacketReceiveEvent;
import com.skyfork.client.events.network.PacketSendEvent;
import com.skyfork.client.events.update.TickEvent;
import org.yaml.snakeyaml.*;
import java.util.*;

public class Protocol implements Access.InstanceAccess
{
    private String field_3481;
    public static String field_3285;
    public static String field_955;
    private final LinkedList<S3FPacketCustomPayload> storedPayloads;
    
    public Protocol() {
        this.storedPayloads = new LinkedList<>();
    }
    
    @EventTarget
    public void onPacketReceive(final PacketReceiveEvent event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof S3FPacketCustomPayload) {
            mc.addScheduledTask(() -> this.storedPayloads.add((S3FPacketCustomPayload)packet));
        }
    }
    
    @EventTarget
    public void onTick(final TickEvent event) {
        final S3FPacketCustomPayload payloadPacket = this.storedPayloads.pollFirst();
        final String channelName = payloadPacket.getChannelName();
        switch (channelName) {
            case "REGISTER": {
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("REGISTER", new PacketBuffer(Unpooled.buffer().writeBytes("FML|HS\u0000FML\u0000FML|MP\u0000FML\u0000FORGE\u0000germplugin-netease\u0000hyt0\u0000armourers".getBytes()))));
                break;
            }
            case "germplugin-netease": {
                final PacketBuffer packetBuffer = new PacketBuffer(payloadPacket.getBufferData());
                if (packetBuffer.readInt() != 73) {
                    break;
                }
                this.method_4441(packetBuffer);
                if (!this.field_3481.equals("gui")) {
                    return;
                }
                final PacketBuffer packetBuffer2 = new PacketBuffer(Unpooled.buffer());
                this.method_6786(packetBuffer2);
                mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("germmod-netease", packetBuffer2));
                final Yaml yaml = new Yaml();
                final Map<String, Object> objectMap = yaml.load(field_955);
                System.out.println(field_955);
                if (objectMap != null) {
                    final PacketBuffer packetBuffer3 = new PacketBuffer(Unpooled.buffer().writeInt(13));
                    mc.addScheduledTask(() -> this.getBg(objectMap));
                    mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("germmod-netease", packetBuffer3));
                    break;
                }
                break;
            }
        }
    }
    
    @EventTarget
    public void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() != null && event.getPacket() instanceof C17PacketCustomPayload) {
            final C17PacketCustomPayload packet = (C17PacketCustomPayload)event.getPacket();
            final String channel = packet.getChannelName();
            final ByteBuf payload = packet.getBufferData();
            final int oldIndex = payload.readerIndex();
            final byte[] data = new byte[payload.readableBytes()];
            payload.readBytes(data);
            payload.readerIndex(oldIndex);
            final String payloadString = new String(data);
            if (channel.equals("MC|Brand") && payloadString.equals("vanilla")) {
                payload.clear();
                payload.writeBytes("fml,forge".getBytes());
            }
        }
    }
    

    public void getBg(final Map<String, Object> objectMap) {
        if (objectMap == null) {
            return;
        }
        final Map<String, Object> newMap = (Map<String, Object>) objectMap.get(field_3285);
        if (newMap != null) {
            final Map<String, Object> bgMap = (Map<String, Object>) newMap.get(field_3285 + "_bg");
            final int width = Integer.parseInt((String) bgMap.get("width"));
            final int height = Integer.parseInt((String) bgMap.get("height"));
            Map<String, Object> buttonMap = null;
            for (final Map.Entry<String, Object> stringObjectEntry : newMap.entrySet()) {
                buttonMap = (Map<String, Object>) stringObjectEntry.getValue();
            }
            Map<String, Object> scrollableParts = null;
            if (buttonMap != null) {
                scrollableParts = (Map<String, Object>) buttonMap.get("scrollableParts");
            }
            final GermGui germGui = new GermGui((float)width, (float)height, scrollableParts);
            mc.displayGuiScreen(germGui);
        }
    }
    
    public void method_6786(final PacketBuffer packetBuffer) {
        packetBuffer.writeInt(4);
        packetBuffer.writeInt(0);
        packetBuffer.writeInt(0);
        packetBuffer.writeString(field_3285);
        packetBuffer.writeString(field_3285);
        packetBuffer.writeString(field_3285);
    }
    
    public void method_4441(final PacketBuffer packetBuffer) {
        this.field_3481 = packetBuffer.readStringFromBuffer(32767);
        field_3285 = packetBuffer.readStringFromBuffer(32767);
        field_955 = packetBuffer.readStringFromBuffer(9999999);
    }
}
