//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "C:\Users\Administrator\Downloads\Minecraft1.12.2 Mappings"!

//Decompiled by Procyon!

package com.skyfork.api.dxg;

import org.lwjgl.util.vector.Vector2f;
import com.skyfork.client.events.base.Event;

public class LookEvent implements Event
{
    private Vector2f rotation;
    
    public LookEvent(Vector2f rotation) {
        this.rotation = rotation;
    }
    
    public Vector2f getRotation() {
        return this.rotation;
    }
    
    public void setRotation(final Vector2f rotation) {
        this.rotation = rotation;
    }
}