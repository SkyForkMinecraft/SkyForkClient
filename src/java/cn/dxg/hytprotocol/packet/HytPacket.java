//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Administrator\Downloads\Minecraft1.12.2 Mappings"!

//Decompiled by Procyon!

package cn.dxg.hytprotocol.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

import java.io.IOException;

public interface HytPacket
{
    public static final Minecraft mc = Minecraft.getMinecraft();
    
    String getChannel();
    
    void process(final ByteBuf p0) throws IOException;
}
