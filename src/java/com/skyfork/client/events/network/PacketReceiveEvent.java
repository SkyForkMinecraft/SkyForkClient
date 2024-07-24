package com.skyfork.client.events.network;

import net.minecraft.network.Packet;
import com.skyfork.client.events.base.Event;

public class PacketReceiveEvent extends Event.EventCancellable {

    private final Packet<?> packet;

    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }
}
