/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/SkidderMC/FDPClient/
 */
package com.skyfork.api.langya.modules.misc.particles;

import java.util.LinkedList;

public final class EvictingList<T> extends LinkedList<T> {

    private final int maxSize;

    public EvictingList(final int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(final T t) {
        if (size() >= maxSize) removeFirst();
        return super.add(t);
    }

}