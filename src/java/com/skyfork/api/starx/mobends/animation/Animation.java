//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Administrator\Downloads\Minecraft1.12.2 Mappings"!

//Decompiled by Procyon!

package com.skyfork.api.starx.mobends.animation;

import com.skyfork.api.starx.mobends.data.EntityData;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;

public abstract class Animation
{
    public abstract void animate(final EntityLivingBase p0, final ModelBase p1, final EntityData p2);
    
    public abstract String getName();
}
