package com.skyfork.api.dxg.hytprotocol;

import com.skyfork.api.dxg.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class Sender
{
    public static final String JsonCloseGUI = "{\"packet_sub_type\":\"null\",\"packet_data\":\"null\",\"packet_type\":\"gui_close\"}";
    public static final String JsonOpenGUI = "{\"packet_sub_type\":\"null\",\"packet_data\":\"null\",\"packet_type\":\"opengui\"}";
    
    public static void sendJson(final String json) throws IOException {
        final byte[] data = encode(json);
        final ByteBuf buf = Unpooled.wrappedBuffer(data);
        final C17PacketCustomPayload packet = new C17PacketCustomPayload("VexView", new PacketBuffer(buf));
        PacketUtil.send((Packet<?>)packet);
    }
    
    public static void sendButtonClicked(final String id) {
        try {
            sendJson(getButtionClickJson(id));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static String getButtionClickJson(final String id) {
        return "{\"packet_sub_type\":\"" + id + "\",\"packet_data\":\"null\",\"packet_type\":\"button\"}";
    }
    
    private static byte[] encode(final String json) throws IOException {
        final ByteArrayInputStream in = new ByteArrayInputStream(json.getBytes("UTF-8"));
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        final GZIPOutputStream out = new GZIPOutputStream(bout);
        final byte[] array = new byte[256];
        int read;
        while ((read = in.read(array)) >= 0) {
            out.write(array, 0, read);
        }
        out.close();
        out.finish();
        return bout.toByteArray();
    }
    
    public static String getTextSetJson(final String id, final String text) {
        return "{\"packet_sub_type\":\"" + id + "\",\"packet_data\":\"" + text + "\",\"packet_type\":\"fieldtext\"}";
    }
}
